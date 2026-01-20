package com.jeremy.acw.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jeremy.acw.core.service.AuthService
import com.jeremy.acw.data.repo.BookingRepo
import com.jeremy.acw.data.repo.HallRepo
import com.jeremy.acw.data.repo.MovieRepo
import com.jeremy.acw.data.repo.ScreeningRepo
import com.jeremy.acw.data.repo.TheatreRepo
import com.jeremy.acw.data.repo.UserRepo
import com.jeremy.acw.data.repo.implementations.UserRepoImpl
import com.jeremy.acw.data.repo.implementations.cinema.BookingRepoImpl
import com.jeremy.acw.data.repo.implementations.cinema.HallRepoImpl
import com.jeremy.acw.data.repo.implementations.cinema.MovieRepoImpl
import com.jeremy.acw.data.repo.implementations.cinema.ScreeningRepoImpl
import com.jeremy.acw.data.repo.implementations.cinema.TheatreRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuthService(firebaseAuth: FirebaseAuth): AuthService {
        return AuthService(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesUserRepo(authService: AuthService, firestore: FirebaseFirestore): UserRepo {
        return UserRepoImpl(authService, firestore)
    }

    @Provides
    @Singleton
    fun providesMovieRepo(firestore: FirebaseFirestore): MovieRepo {
        return MovieRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesTheatreRepo(firestore: FirebaseFirestore): TheatreRepo {
        return TheatreRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesHallRepo(firestore: FirebaseFirestore): HallRepo {
        return HallRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesScreeningRepo(firestore: FirebaseFirestore): ScreeningRepo {
        return ScreeningRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun providesBookingRepo(firestore: FirebaseFirestore): BookingRepo {
        return BookingRepoImpl(firestore)
    }
}