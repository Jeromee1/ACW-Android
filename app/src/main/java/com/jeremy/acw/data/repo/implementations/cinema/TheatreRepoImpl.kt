package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun createTheatre(theatre: Theatre) {
        val docRef = dbRef.document()
        val theatre = theatre.copy(id = docRef.id)
        docRef.set(theatre.toMap()).await()
    }

    override suspend fun deleteTheatre(id: String) {
        dbRef.document(id).delete().await()
    }
}