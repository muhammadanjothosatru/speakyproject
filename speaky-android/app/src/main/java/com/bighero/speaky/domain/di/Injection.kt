package com.bighero.speaky.domain.di

import android.content.Context
import com.bighero.speaky.data.source.HistoryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object Injection {
    fun provideRepository(context: Context): HistoryRepository {
        return HistoryRepository.getInstance(Firebase.database.reference)
    }

}