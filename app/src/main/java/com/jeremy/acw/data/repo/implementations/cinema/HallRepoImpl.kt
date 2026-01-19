package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.model.cinema.Hall
import com.jeremy.acw.data.repo.HallRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class HallRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : HallRepo {
    private val dbRef = firestore.collection("halls")

    override suspend fun fetchAllHalls(): List<Hall> {
        val snapshot = dbRef.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Hall::class.java)
        }
    }

    override suspend fun fetchHall(id: String): Hall {
        return dbRef.document(id).get().await()
            .toObject(Hall::class.java)
            ?: throw IllegalStateException("Hall doesn't exist")
    }

    override suspend fun createHall(hall: Hall) {
        val docRef = dbRef.document()
        val hall = hall.copy(id = docRef.id)
        docRef.set(hall.toMap()).await()
    }

    override suspend fun deleteHall(id: String) {
        dbRef.document(id).delete().await()
    }
}