package com.bighero.speaky.ui.detail.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse

class DetailModuleViewModel(private val repository: FirebaseRepository): ViewModel() {

    fun getBab() : LiveData<ModuleResponse> {
        return repository.getModule()
    }

    fun setSelectedModule(moduleId: String) {
        TODO("Not yet implemented")
    }
}