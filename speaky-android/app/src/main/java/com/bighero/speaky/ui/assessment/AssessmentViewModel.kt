package com.bighero.speaky.ui.assessment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.assesment.APackResponse
import com.bighero.speaky.data.source.remote.response.assesment.InstructionResponse

class AssessmentViewModel (
    private val firebaseRepository: FirebaseRepository
): ViewModel(){

    private val _type = MutableLiveData<String>()
    val type : LiveData<String> = _type

    private val _instruction = MutableLiveData<String>()
    val instruction : LiveData<String> = _instruction

    fun getAssesmentPack() : LiveData<APackResponse> {
        return firebaseRepository.getAssesmentPack()
    }

    fun findAssessmentPack(id: String) : LiveData<InstructionResponse> {
        return firebaseRepository.getInstruction(id)
    }
}