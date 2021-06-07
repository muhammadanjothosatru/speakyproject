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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class FirebaseRepository(
    rootRef: DatabaseReference,
) : IHistoryRepository {

    private val historyModulRef: DatabaseReference = rootRef.child("UserModul")
    private val moduleRef: DatabaseReference = rootRef.child("ResModul")
    private val userModuleRef: DatabaseReference = rootRef.child("userModul")
    private val userAssessmentRef: DatabaseReference = rootRef.child("UserAssessment")
    private var auth: FirebaseAuth = Firebase.auth
    private var uId = auth.currentUser!!.uid
    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null

        fun getInstance(rootRef: DatabaseReference): FirebaseRepository =
            instance ?: synchronized(this) {
                FirebaseRepository(rootRef).apply { instance = this }
            }
    }

    override fun getHistory() : MutableLiveData<UserAssesmentResponse> {
        val mutableLiveData = MutableLiveData<UserAssesmentResponse>()
        userAssessmentRef.child(uId).get().addOnSuccessListener {
            val response = UserAssesmentResponse()
                response.history = it.children.map { it ->
                    AssessmentEntity(
                        it.child("donwloadUrl").value.toString()
                    )
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
        moduleRef.orderByKey().get().addOnSuccessListener {
            val response = ModuleResponse()
//            response.module = it.children.map { it ->
//                it.getValue(ModuleEntity::class.java)!!
//            }
            it.children.map {
                Log.e("moduleKey",  it.key.toString())
                Log.e("moduleValue",  it.value.toString())
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

}

