package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.model.cinema.Screening
import com.jeremy.acw.data.repo.ScreeningRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class ScreeningRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : ScreeningRepo {
    private val dbRef = firestore.collection("screenings")

    override suspend fun fetchAllScreenings(): List<Screening> {
        val snapshot = dbRef.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Screening::class.java)
        }
    }

    override suspend fun fetchScreening(id: String): Screening {
        return dbRef.document(id).get().await()
            .toObject(Screening::class.java)
            ?: throw IllegalStateException("Screening doesn't exist")
    }

    override suspend fun createScreening(screening: Screening) {
        val docRef = dbRef.document()
        val screening = screening.copy(id = docRef.id)
        docRef.set(screening.toMap()).await()
    }

    override suspend fun deleteScreening(id: String) {
        dbRef.document(id).delete().await()
    }
}