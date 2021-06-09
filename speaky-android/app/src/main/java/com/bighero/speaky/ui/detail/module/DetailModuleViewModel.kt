package com.bighero.speaky.ui.detail.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.response.module.BabByIdResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleByIdResponse
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse

class DetailModuleViewModel(private val repository: FirebaseRepository) : ViewModel() {

    fun getBab(): LiveData<ModuleResponse> {
        return repository.getModule()
    }

    fun setSelectedModule(moduleId: String): LiveData<ModuleByIdResponse> {
        return repository.getModuleById(moduleId)
    }

    fun getBabById(babId: String, moduleId: String): LiveData<BabByIdResponse> {
        return repository.getBabById(babId, moduleId)
    }
}