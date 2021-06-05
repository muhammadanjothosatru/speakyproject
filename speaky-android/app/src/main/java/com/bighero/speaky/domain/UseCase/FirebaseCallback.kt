package com.bighero.speaky.domain.UseCase

import com.bighero.speaky.data.source.remote.response.HistoryResponse

interface FirebaseCallback {
    fun onResponse(response: HistoryResponse)
}