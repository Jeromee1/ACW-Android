package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.core.utils.buildUpdateReq
import com.jeremy.acw.data.model.cinema.Movie
import com.jeremy.acw.data.model.forms.UpdateMovieForm
import com.jeremy.acw.data.repo.MovieRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class MovieRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : MovieRepo {
    private val dbRef = firestore.collection("movies")

    override suspend fun fetchAllMovies(): List<Movie> {
        val snapshot = dbRef.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Movie::class.java)
        }
    }

    override suspend fun fetchMovie(id: String): Movie {
        return dbRef.document(id).get().await()
            .toObject(Movie::class.java)
            ?: throw IllegalStateException("Movie doesn't exist")
    }

    override suspend fun createMovie(movie: Movie) {
        val docRef = dbRef.document()
        val movie = movie.copy(id = docRef.id)
        docRef.set(movie.toMap()).await()
    }

    override suspend fun updateMovie(id: String, form: UpdateMovieForm) {
        val updates = buildUpdateReq(form)
        dbRef.document(id).update(updates.toMap()).await()
    }

    override suspend fun deleteMovie(id: String) {
        dbRef.document(id).delete().await()
    }
}