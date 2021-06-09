package com.bighero.speaky.ui.home.fragment.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse

class ModuleViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    fun getModule(): LiveData<ModuleResponse> {
        return firebaseRepository.getModule()
    }
}