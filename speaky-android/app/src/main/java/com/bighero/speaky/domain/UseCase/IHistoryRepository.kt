package com.bighero.speaky.domain.UseCase

import androidx.lifecycle.MutableLiveData
import com.bighero.speaky.data.source.remote.response.HistoryResponse

interface IHistoryRepository {
    fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<HistoryResponse>
}