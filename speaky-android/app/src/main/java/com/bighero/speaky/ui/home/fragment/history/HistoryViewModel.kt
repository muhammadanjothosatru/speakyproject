package com.bighero.speaky.ui.home.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.source.HistoryRepository
import com.bighero.speaky.data.source.remote.response.PractiveResponse
import com.bighero.speaky.domain.UseCase.FirebaseCallback

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

    fun getResponseUsingLiveData() : LiveData<PractiveResponse> {
        return historyRepository.getResponseFromRealtimeDatabaseUsingLiveData()
    }
}