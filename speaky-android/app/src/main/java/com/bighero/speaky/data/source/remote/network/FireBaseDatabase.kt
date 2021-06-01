package com.bighero.speaky.data.source.remote.network

import com.bighero.speaky.data.entity.HistoryEntity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseDatabase(private var mDatabase: FirebaseDatabase, private var mReferencePratice :  DatabaseReference, private val history : ArrayList<HistoryEntity>) {

    fun FirebaseDatabaseHelper() {
      mDatabase = FirebaseDatabase.getInstance()
      mReferencePratice = mDatabase.getReference("pratice")
  }

    fun readPractice() {

    }
}
