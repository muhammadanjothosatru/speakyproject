package com.bighero.speaky.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.AssessmentEntity
import com.bighero.speaky.data.entity.HistoryEntity
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.data.entity.module.UserModuleEntity
import com.bighero.speaky.data.source.remote.response.HistoryResponse
import com.bighero.speaky.data.source.remote.response.ModuleResponse
import com.bighero.speaky.data.source.remote.response.UserAssesmentResponse
import com.bighero.speaky.data.source.remote.response.UserModuleResponse
import com.bighero.speaky.domain.useCase.IHistoryRepository
import com.google.firebase.database.*

class FirebaseRepository(
    rootRef: DatabaseReference,
) : IHistoryRepository {

    private val historyRef: DatabaseReference = rootRef.child("UserModul")
    private val moduleRef: DatabaseReference = rootRef.child("ResModul")
    private val userModuleRef: DatabaseReference = rootRef.child("userModul")
    private val userAssesmentRef: DatabaseReference = rootRef.child("UserAssessment")

    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null

        fun getInstance(rootRef: DatabaseReference): FirebaseRepository =
            instance ?: synchronized(this) {
                FirebaseRepository(rootRef).apply { instance = this }
            }
    }

    override fun getHistory() : MutableLiveData<HistoryResponse> {
        val mutableLiveData = MutableLiveData<HistoryResponse>()
        val history = ArrayList<HistoryEntity>()
        historyRef.get().addOnSuccessListener {
            val response = HistoryResponse()
//                response.history = it.children.map { it ->
//                    it.getValue(HistoryEntity::class.java)!!
//                }
            it.children.map {
                Log.e("module",  it.toString())
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getModule(): MutableLiveData<ModuleResponse> {
        val mutableLiveData = MutableLiveData<ModuleResponse>()
        val module = ArrayList<ModuleEntity>()
        moduleRef.get().addOnSuccessListener {
            val response = ModuleResponse()
//            response.module = it.children.map { it ->
//                it.getValue(ModuleEntity::class.java)!!
//            }
            it.children.map {
                Log.e("module",  it.toString())
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun userModule(): MutableLiveData<UserModuleResponse> {
        val mutableLiveData = MutableLiveData<UserModuleResponse>()
        val userModule = ArrayList<UserModuleEntity>()
        userModuleRef.get().addOnSuccessListener {
            val response = UserModuleResponse()
//            response.module = it.children.map { it ->
//                it.getValue(ModuleEntity::class.java)!!
//            }
            it.children.map {
                Log.e("Usermodule",  it.toString())
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun userAssesment(): MutableLiveData<UserAssesmentResponse> {
        val mutableLiveData = MutableLiveData<UserAssesmentResponse>()
        val userAssesment = ArrayList<AssessmentEntity>()
        userAssesmentRef.get().addOnSuccessListener {
            val response = UserAssesmentResponse()
//            response.module = it.children.map { it ->
//                it.getValue(ModuleEntity::class.java)!!
//            }
            it.children.map {
                Log.e("Usermodule",  it.toString())
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }
}

