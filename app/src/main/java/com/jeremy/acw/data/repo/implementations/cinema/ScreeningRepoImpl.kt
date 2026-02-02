package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.ScreeningRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class ScreeningRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ScreeningRepo {
    private val dbRef = firestore.collection("theatres")

    override suspend fun fetchAllScreenings(theatreId: String, hallId: String): List<Screening> {
        val snapshot = dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Screening::class.java)
        }
    }

    override suspend fun fetchScreening(theatreId: String, hallId: String,  id: String): Screening {
        return dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .document(id)
            .get().await()
            .toObject(Screening::class.java)
            ?: throw IllegalStateException("Screening doesn't exist")
    }

    override suspend fun createScreening(theatreId: String, hallId: String, screening: Screening) {
        val docRef = dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .document()
        val newScreening = screening.copy(id = docRef.id)
        docRef.set(newScreening.toMap()).await()
    }

    override suspend fun deleteScreening(theatreId: String, hallId: String, id: String) {
        dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .document(id)
            .delete().await()
    }

    override suspend fun updateSeats(
        theatreId: String,
        hallId: String,
        id: String,
        fetched: Map<String, SeatStatus>,
        seats: Map<String, SeatStatus>
    ) {
        val seatsToUpdate = seats.filter { (seatId, newStatus) ->
            fetched[seatId] != newStatus
        }

        if (seatsToUpdate.isEmpty()) return

        val docRef = dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .document(id)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val currentDbSeats = snapshot.get("seats") as? Map<String, String> ?: emptyMap()
            for ((seatId, _) in seatsToUpdate) {
                val currentStatusInDb = currentDbSeats[seatId]
                val expectedStatus = fetched[seatId]?.name
                if (currentStatusInDb != expectedStatus) {
                    throw IllegalStateException("Conflict: Seat $seatId is no longer $expectedStatus (Now: $currentStatusInDb)")
                }
            }
            val updates = seatsToUpdate.mapKeys { "seats.${it.key}" }.mapValues { it.value.name }
            transaction.update(docRef, updates)
        }.await()
    }

    override suspend fun fetchMovieScreening(movieId: String): List<Screening> {
        val now = Timestamp.now()

        return firestore
            .collectionGroup("screenings")
            .whereEqualTo("movieId", movieId)
            .whereGreaterThan("startTime", now)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Screening::class.java) }
    }
}