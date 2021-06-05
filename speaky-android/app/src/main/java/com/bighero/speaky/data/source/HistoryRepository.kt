package com.bighero.speaky.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.HistoryEntity
import com.bighero.speaky.data.source.remote.response.HistoryResponse
import com.bighero.speaky.domain.UseCase.IHistoryRepository
import com.google.firebase.database.*

class HistoryRepository(
    rootRef: DatabaseReference,
) : IHistoryRepository {

    private val historyRef: DatabaseReference = rootRef.child("pratice")
    companion object {
        @Volatile
        private var instance: HistoryRepository? = null

        fun getInstance(rootRef: DatabaseReference): HistoryRepository =
            instance ?: synchronized(this) {
                HistoryRepository(rootRef).apply { instance = this }
            }
    }

    override fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<HistoryResponse> {
        val mutableLiveData = MutableLiveData<HistoryResponse>()
        val history = ArrayList<HistoryEntity>()
        historyRef.get().addOnSuccessListener {
            val response = HistoryResponse()
                response.history = it.children.map { it ->
                    it.getValue(HistoryEntity::class.java)!!
                }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }
}

