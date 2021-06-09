package com.bighero.speaky.domain.useCase

import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.assesment.AssessmentEntity
import com.bighero.speaky.data.source.remote.response.assesment.APackResponse
import com.bighero.speaky.data.source.remote.response.assesment.InstructionResponse
import com.bighero.speaky.data.source.remote.response.assesment.UserAssesmentResponse
import com.bighero.speaky.data.source.remote.response.assesment.UserResultResponse
import com.bighero.speaky.data.source.remote.response.module.BabByIdResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleByIdResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse
import com.bighero.speaky.data.source.remote.response.module.UserModuleResponse
import com.bighero.speaky.data.source.remote.response.pratice.PracticeByIdResponse
import com.bighero.speaky.data.source.remote.response.pratice.PracticeResponse

interface IHistoryRepository {
    fun getHistory(): MutableLiveData<UserAssesmentResponse>
    fun getModule(): MutableLiveData<ModuleResponse>
    fun getUserModule(): MutableLiveData<UserModuleResponse>
    fun getAssesmentPack(): MutableLiveData<APackResponse>
    fun getPractice(): MutableLiveData<PracticeResponse>
    fun getInstruction(id: String): MutableLiveData<InstructionResponse>
    fun getModuleById(id: String): MutableLiveData<ModuleByIdResponse>
    fun getPraticeById(id: String): MutableLiveData<PracticeByIdResponse>
    fun getResult(id: String): MutableLiveData<UserResultResponse>
    fun getBabById(id: String, moduleId: String): MutableLiveData<BabByIdResponse>
    fun setUser(assessmentEntity: AssessmentEntity, date: String)
}