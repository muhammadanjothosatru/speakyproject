package com.bighero.speaky.ui.detail.assessment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.assesment.UserResultResponse

class DetailResultViewModel(private val mfirebaseRepository: FirebaseRepository) : ViewModel() {
    fun getResult(id: String): LiveData<UserResultResponse> {
        return mfirebaseRepository.getResult(id)
    }
}