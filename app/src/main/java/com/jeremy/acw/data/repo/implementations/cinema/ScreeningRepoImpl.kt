package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.enums.cinema.SeatStatus
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.ScreeningRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class ScreeningRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
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
        seats: Map<String, SeatStatus>
    ) {
        dbRef
            .document(theatreId)
            .collection("halls")
            .document(hallId)
            .collection("screenings")
            .document(id)
            .update("seats", seats.mapValues { it.value })
            .await()
    }
}