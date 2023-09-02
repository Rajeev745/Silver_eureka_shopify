package com.example.e_commerceapplication.dependencyinjection

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.e_commerceapplication.helper.Constants.INTRODUCTION_SHARED_PREFERENCE
import com.example.e_commerceapplication.utils.FirebaseProductCart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun getFirebaseStore() = Firebase.firestore

    @Provides
    fun provideSharedPreference(
        application: Application
    ) = application.getSharedPreferences(INTRODUCTION_SHARED_PREFERENCE, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseCartProduct(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ) = FirebaseProductCart(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun getStorage() = FirebaseStorage.getInstance().reference
}