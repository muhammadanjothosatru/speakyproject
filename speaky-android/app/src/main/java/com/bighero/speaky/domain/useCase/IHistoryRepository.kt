package com.bighero.speaky.domain.useCase

import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.source.remote.response.HistoryResponse
import com.bighero.speaky.data.source.remote.response.ModuleResponse
import com.bighero.speaky.data.source.remote.response.UserAssesmentResponse
import com.bighero.speaky.data.source.remote.response.UserModuleResponse

interface IHistoryRepository {
    fun getHistory() : MutableLiveData<UserAssesmentResponse>
    fun getModule() : MutableLiveData<ModuleResponse>
    fun userModule() : MutableLiveData<UserModuleResponse>
}