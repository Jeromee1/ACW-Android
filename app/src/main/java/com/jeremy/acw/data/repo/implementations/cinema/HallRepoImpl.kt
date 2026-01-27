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
    private val dbRef = firestore.collection("theatres")

    override suspend fun fetchAllHalls(theatreId: String): List<Hall> {
        val snapshot = dbRef
            .document(theatreId)
            .collection("halls")
            .orderBy("hallName")
            .get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Hall::class.java)
        }
    }

    override suspend fun fetchHall(theatreId: String, id: String): Hall {
        return dbRef
            .document(theatreId)
            .collection("halls")
            .document(id)
            .get().await()
            .toObject(Hall::class.java)
            ?: throw IllegalStateException("Hall doesn't exist")
    }

    override suspend fun updateHall(theatreId: String, id: String, hall: Hall) {
        dbRef
            .document(theatreId)
            .collection("halls")
            .document(id)
            .update(hall.toMap()).await()
    }

    override suspend fun deleteHall(theatreId: String, id: String) {
        dbRef
            .document(theatreId)
            .collection("halls")
            .document(id)
            .delete().await()
    }
}