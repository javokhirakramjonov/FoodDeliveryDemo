package uz.gita.fooddeliverydemo.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @[Singleton Provides]
    fun getFireStore(): FirebaseFirestore = Firebase.firestore

    @[Singleton Provides]
    fun getFireBaseAuth(): FirebaseAuth = Firebase.auth

    @[Singleton Provides]
    fun getFirebaseRealTimeDatabase(): FirebaseDatabase = Firebase.database
}