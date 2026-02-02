package com.jeremy.acw.data.repo.implementations.cinema

import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.data.model.cinema.Booking
import com.jeremy.acw.data.repo.BookingRepo
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class BookingRepoImpl @Inject constructor(
    firestore: FirebaseFirestore
) : BookingRepo {
    private val dbRef = firestore.collection("bookings")

    override suspend fun fetchUsersBookings(uid: String): List<Booking> {
        return dbRef
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(Booking::class.java) }
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