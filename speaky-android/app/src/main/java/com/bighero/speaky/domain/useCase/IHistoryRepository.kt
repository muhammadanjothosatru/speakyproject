package com.bighero.speaky.domain.useCase

import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.entity.assesment.AssessmentEntity
import com.bighero.speaky.data.source.remote.response.assesment.APackResponse
import com.bighero.speaky.data.source.remote.response.assesment.InstructionResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse
import com.bighero.speaky.data.source.remote.response.assesment.UserAssesmentResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleByIdResponse
import com.bighero.speaky.data.source.remote.response.module.UserModuleResponse

interface IHistoryRepository {
    fun getHistory() : MutableLiveData<UserAssesmentResponse>
    fun getModule() : MutableLiveData<ModuleResponse>
    fun getUserModule() : MutableLiveData<UserModuleResponse>
    fun getAssesmentPack() : MutableLiveData<APackResponse>
    fun getInstruction(id:String) : MutableLiveData<InstructionResponse>
    fun getModuleById(id:String) : MutableLiveData<ModuleByIdResponse>
    fun setUser(assessmentEntity: AssessmentEntity)
}