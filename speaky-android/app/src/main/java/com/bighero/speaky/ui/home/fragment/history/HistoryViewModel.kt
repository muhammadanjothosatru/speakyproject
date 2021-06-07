package com.bighero.speaky.ui.home.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.UserAssesmentResponse

class HistoryViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

    fun getResponseUsingLiveData() : LiveData<UserAssesmentResponse> {
        return firebaseRepository.getHistory()
    }
}