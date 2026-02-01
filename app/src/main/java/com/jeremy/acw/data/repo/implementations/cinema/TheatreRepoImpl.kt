package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.enums.cinema.HallSize
import com.jeremy.acw.data.enums.cinema.HallType
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.model.cinema.Theatre
import com.jeremy.acw.data.repo.TheatreRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class TheatreRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : TheatreRepo {
    private val dbRef = firestore.collection("theatres")

    override suspend fun fetchAllTheatres(): List<Theatre> {
        val snapshot = dbRef.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Theatre::class.java)
        }
    }

    override suspend fun fetchTheatre(id: String): Theatre {
        return dbRef.document(id).get().await()
            .toObject(Theatre::class.java)
            ?: throw IllegalStateException("Theatre doesn't exist")
    }

    override suspend fun fetchScreeningsFromTheatre(id: String): Int {
        val snapshot = dbRef.firestore
            .collectionGroup("screenings")
            .whereEqualTo("theatreId", id)
            .get()
            .await()

        return snapshot.size()
    }

    override suspend fun createTheatre(theatre: String) {
        val theatreRef = dbRef.document(theatre)
        val batch = theatreRef.firestore.batch()

        val theatreWithId = Theatre(id = theatreRef.id)
        batch.set(theatreRef, theatreWithId.toMap())

        val hallsRef = theatreRef.collection("halls")

        ('A'..'H').forEach { letter ->
            val hallRef = hallsRef.document()
            val hall = Hall(
                id = hallRef.id,
                theatreId = theatreRef.id,
                hallName = letter.toString(),
                seatLayout = HallSize.MEDIUM.value,
                hallType = HallType.TwoD.value
            )
            batch.set(hallRef, hall.toMap())
        }
        batch.commit().await()
    }

    override suspend fun deleteTheatre(id: String) {
        dbRef.document(id).delete().await()
    }
}