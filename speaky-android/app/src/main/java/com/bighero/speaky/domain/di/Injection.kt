package com.bighero.speaky.domain.di

import android.content.Context
import com.bighero.speaky.data.source.FirebaseRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Injection {
    fun provideRepository(context: Context): FirebaseRepository {
        return FirebaseRepository.getInstance(Firebase.database.reference)
    }

}