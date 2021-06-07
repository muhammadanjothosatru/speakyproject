package com.bighero.speaky.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.AssessmentEntity
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.data.entity.module.UserModuleEntity
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
        userAssessmentRef.child("makan").get().addOnSuccessListener {
            val response = UserAssesmentResponse()
                response.history = it.children.map { it ->
                    Log.e("check",  it.toString())
                    AssessmentEntity(
                        donwloadUrl = it.child("donwloadUrl").value.toString(),
                        score = it.child("score").value as Long,
                        timeStamp = it.child("timestamp").value.toString(),
                        gaze = it.child("gaze").child("value").value as Long,
                        blink = it.child("blink").child("value").value as Long,
                        disfluency = it.child("disfluency").child("value").value as Long,
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
        moduleRef.orderByKey().get().addOnSuccessListener {
            val response = ModuleResponse()
            response.module = it.children.map { it ->
                ModuleEntity(
                    key = it.key.toString(),
                    bab = ModuleEntity.Bab(
                        gambar = it.child("gambar").value.toString(),
                        judul = it.child("judul").value.toString(),
                    )
                )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun userModule(): MutableLiveData<UserModuleResponse> {
        val mutableLiveData = MutableLiveData<UserModuleResponse>()
        userModuleRef.child(uId).get().addOnSuccessListener {
            val response = UserModuleResponse()
            response.UserModule = it.children.map { it ->
                UserModuleEntity(
                    key = it.key.toString(),
                    bab = UserModuleEntity.Bab(
                        key = it.child("bab").child("bab1").key.toString(),
                        status = it.child("judul").child("bab1").child("status").toString(),
                    )
                )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

}

