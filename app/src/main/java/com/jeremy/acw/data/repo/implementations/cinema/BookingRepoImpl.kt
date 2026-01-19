package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.repo.BookingRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class BookingRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : BookingRepo {
    private val dbRef = firestore.collection("bookings")

    override suspend fun fetchAllBookings(): List<Booking> {
        val snapshot = dbRef.get().await()
        return snapshot.documents.mapNotNull {
            it.toObject(Booking::class.java)
        }
    }

    override suspend fun fetchBooking(id: String): Booking {
        return dbRef.document(id).get().await()
            .toObject(Booking::class.java)
            ?: throw IllegalStateException("Booking doesn't exist")
    }

    override suspend fun createBooking(booking: Booking) {
        val docRef = dbRef.document()
        val booking = booking.copy(id = docRef.id)
        docRef.set(booking.toMap()).await()
    }

    override suspend fun deleteBooking(id: String) {
        dbRef.document(id).delete().await()
    }
}