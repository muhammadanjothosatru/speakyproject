package com.bighero.speaky.ui.home.fragment.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.ModuleResponse

class ModuleViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is module Fragment"
    }
    val text: LiveData<String> = _text

    fun getModule() : LiveData<ModuleResponse> {
        return firebaseRepository.getModule()
    }
}