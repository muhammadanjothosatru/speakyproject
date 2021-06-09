package com.bighero.speaky.ui.detail.praktik

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.pratice.PracticeByIdResponse

class DetailPracticeViewModel(private val mFirebaseRepository: FirebaseRepository) : ViewModel() {


    fun getPraticeById(id:String) : LiveData<PracticeByIdResponse> {
        return mFirebaseRepository.getPraticeById(id)
    }
}