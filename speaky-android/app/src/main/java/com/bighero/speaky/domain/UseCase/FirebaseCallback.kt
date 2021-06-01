package com.bighero.speaky.domain.UseCase

import com.bighero.speaky.data.source.remote.response.PractiveResponse

interface FirebaseCallback {
    fun onResponse(response: PractiveResponse)
}