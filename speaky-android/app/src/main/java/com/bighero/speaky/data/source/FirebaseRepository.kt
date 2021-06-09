package com.bighero.speaky.data.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.pratice.PraticeEntity
import com.bighero.speaky.data.entity.assesment.AssementInstruction
import com.bighero.speaky.data.entity.assesment.AssessmentEntity
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import com.bighero.speaky.data.entity.module.BabByEntity
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.data.entity.module.UserModuleEntity
import com.bighero.speaky.data.source.remote.response.pratice.PracticeResponse
import com.bighero.speaky.data.source.remote.response.assesment.APackResponse
import com.bighero.speaky.data.source.remote.response.assesment.InstructionResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse
import com.bighero.speaky.data.source.remote.response.assesment.UserAssesmentResponse
import com.bighero.speaky.data.source.remote.response.assesment.UserResultResponse
import com.bighero.speaky.data.source.remote.response.module.BabByIdResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleByIdResponse
import com.bighero.speaky.data.source.remote.response.module.UserModuleResponse
import com.bighero.speaky.data.source.remote.response.pratice.PracticeByIdResponse
import com.bighero.speaky.domain.useCase.IHistoryRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class FirebaseRepository(
    rootRef: DatabaseReference,
) : IHistoryRepository {

    private val userRef: DatabaseReference = rootRef.child("users")
    private val moduleRef: DatabaseReference = rootRef.child("ResModul")
    private val praticeRef: DatabaseReference = rootRef.child("ResPractice")
    private val userModuleRef: DatabaseReference = rootRef.child("userModul")
    private val userAssessmentRef: DatabaseReference = rootRef.child("UserAssessment")
    private val packAssessmentRef: DatabaseReference = rootRef.child("AssessmentPack")
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
        userAssessmentRef.child(uId).get().addOnSuccessListener { asessement ->
            val response = UserAssesmentResponse()
                response.history = asessement.children.map {
                        AssessmentEntity(
                            donwloadUrl = it.child("donwloadUrl").value.toString(),
                            score = it.child("score").value as Long,
                            timeStamp = it.child("timeStamp").value.toString(),
                            gaze = it.child("gaze").value as Long,
                            blink = it.child("blink").value as Long,
                            disfluency = it.child("disfluency").value as Long,
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
        moduleRef.orderByKey().get().addOnSuccessListener { mod ->
            val response = ModuleResponse()
            response.module = mod.children.map {
                ModuleEntity(
                    key = it.key.toString(),
                    deskripsi = it.child("deskripsi").value.toString(),
                    gambar = it.child("gambar").value.toString(),
                    judul = it.child("judul").value.toString(),
                    bab = it.child("bab").children.map { module ->
                        ModuleEntity.Bab(
                            key = module.key.toString(),
                                konten = module.child("deskripsi").value.toString(),
                                no = module.child("no").value.toString(),
                                time = module.child("time").value as Long,
                                judul = module.child("judul").value.toString(),
                                video = module.child("video").value.toString(),
                                practice = module.child("practice").children.map { prac ->
                                    ModuleEntity.Bab.practices(
                                        key = prac.key.toString(),
                                        time = prac.child("time").value as Long
                                    )
                                }
                            )
                        }
                    )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getUserModule(): MutableLiveData<UserModuleResponse> {
        val mutableLiveData = MutableLiveData<UserModuleResponse>()
        userModuleRef.child(uId).get().addOnSuccessListener {
            val response = UserModuleResponse()
            response.UserModule = it.children.map { mod ->
                UserModuleEntity(
                    key = mod.key.toString(),
                    bab = UserModuleEntity.Bab(
                        key = mod.child("bab").child("bab1").key.toString(),
                        status = mod.child("judul").child("bab1").child("status").toString(),
                    )
                )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getAssesmentPack(): MutableLiveData<APackResponse> {
        val mutableLiveData = MutableLiveData<APackResponse>()
        packAssessmentRef.get().addOnSuccessListener {
            val response = APackResponse()
            response.pack = it.children.map { it ->
                AssessmentPackEntity(
                    id = it.key.toString(),
                    title = it.child("title").value.toString(),
                    type = it.child("type").value.toString(),
                    petunjuk = it.child("petunjuk").value.toString(),
                    guide = it.child("instruction").children.map {
                        it.value.toString()
                    }
                )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getPractice(): MutableLiveData<PracticeResponse> {
        val mutableLiveData = MutableLiveData<PracticeResponse>()
        praticeRef.get().addOnSuccessListener {
            val response = PracticeResponse()
            response.pratice = it.children.map { it ->
                PraticeEntity(
                    key = it.key.toString(),
                   judul = it.child("judul").value.toString(),
                    cover = it.child("cover").value.toString(),
                    deskripsi =it.child("deskripsi").value.toString(),
                    ilustrasi = PraticeEntity.Ilustration(
                            durasi = it.child("ilustrasi").child("durasi").value as Long,
                            link = it.child("ilustrasi").child("link").value.toString(),
                        )

                )
            }
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getInstruction(id:String): MutableLiveData<InstructionResponse> {
        val mutableLiveData = MutableLiveData<InstructionResponse>()
        packAssessmentRef.get().addOnSuccessListener {
            val response = InstructionResponse()
            response.intruction =
                AssementInstruction(
                    type = it.child(id).child("type").value.toString(),
                    intruksi = it.child(id).child("instruction").children.map {
                            it ->it.value.toString()
                    }
                )

            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getModuleById(id:String): MutableLiveData<ModuleByIdResponse> {
        val mutableLiveData = MutableLiveData<ModuleByIdResponse>()
        moduleRef.orderByKey().get().addOnSuccessListener {
            val response = ModuleByIdResponse()
            response.module = ModuleEntity(
                    key = it.child(id).key.toString(),
                    deskripsi = it.child(id).child("deskripsi").value.toString(),
                    gambar = it.child(id).child("gambar").value.toString(),
                    judul = it.child(id).child("judul").value.toString(),
                    bab = it.child(id).child("bab").children.map { module ->
                        ModuleEntity.Bab(
                            key = module.key.toString(),
                            no = module.child("no").value.toString(),
                            time = module.child("time").value as Long,
                            konten = module.child("deskripsi").value.toString(),
                            judul = module.child("judul").value.toString(),
                            video = module.child("video").value.toString(),
                            practice = module.child("practice").children.map { prac ->
                                ModuleEntity.Bab.practices(
                                    key = prac.key.toString(),
                                    time = prac.child("time").value as Long
                                )
                            }
                        )
                    }
                )
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getPraticeById(id: String): MutableLiveData<PracticeByIdResponse> {
        val mutableLiveData = MutableLiveData<PracticeByIdResponse>()
        praticeRef.get().addOnSuccessListener {
            val response = PracticeByIdResponse()
            response.pratice =
                PraticeEntity(
                    key = it.child(id).key.toString(),
                    judul = it.child(id).child("judul").value.toString(),
                    cover = it.child(id).child("cover").value.toString(),
                    deskripsi = it.child(id).child("deskripsi").value.toString(),
                    ilustrasi = PraticeEntity.Ilustration(
                        durasi = it.child(id).child("ilustrasi").child("durasi").value as Long,
                        link = it.child(id).child("ilustrasi").child("link").value.toString(),
                    )
                )

            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getResult(id: String): MutableLiveData<UserResultResponse> {
        val mutableLiveData = MutableLiveData<UserResultResponse>()
        userAssessmentRef.child(uId).get().addOnSuccessListener {
            val response = UserResultResponse()
            response.history = AssessmentEntity(
                    donwloadUrl = it.child(id).child("donwloadUrl").value.toString(),
                    score = it.child(id).child("score").value as Long,
                    timeStamp = it.child(id).child("timeStamp").value.toString(),
                    gaze = it.child(id).child("gaze").value as Long,
                    blink = it.child(id).child("blink").value as Long,
                    disfluency = it.child(id).child("disfluency").value as Long,
                )

            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun getBabById(id: String, moduleId: String): MutableLiveData<BabByIdResponse> {
        val mutableLiveData = MutableLiveData<BabByIdResponse>()
        moduleRef.child(moduleId).child("bab").child(id).get().addOnSuccessListener { module ->
            val response = BabByIdResponse()
            response.module = BabByEntity(
                        key = module.key.toString(),
                        no = module.child("no").value.toString(),
                        time = module.child("time").value as Long,
                        konten = module.child("deskripsi").value.toString(),
                        judul = module.child("judul").value.toString(),
                        video = module.child("video").value.toString(),
                        practice = module.child("practice").children.map { prac ->
                            BabByEntity.practices(
                                key = prac.key.toString(),
                                time = prac.child("time").value as Long
                            )
                        }
                    )
            mutableLiveData.value = response
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        return mutableLiveData
    }

    override fun setUser(assessmentEntity: AssessmentEntity, date:String) {
        userAssessmentRef.child(uId).child(date).setValue(assessmentEntity)
        userRef.child(uId).child("score").setValue(assessmentEntity.score)
    }

}

