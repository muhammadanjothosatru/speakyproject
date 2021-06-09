package com.bighero.speaky.ui.home.fragment.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.pratice.PracticeResponse

class PracticeViewModel(private val mFirebaseRepository: FirebaseRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is practice Fragment"
    }
    val text: LiveData<String> = _text
    fun getPratice() : LiveData<PracticeResponse> {
        return mFirebaseRepository.getPractice()
    }
}