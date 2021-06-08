package com.bighero.speaky.ui.assessment.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bighero.speaky.data.entity.assesment.AssessmentEntity
import com.bighero.speaky.data.source.FirebaseRepository
import com.bighero.speaky.data.source.remote.network.ApiConfig
import com.bighero.speaky.data.source.remote.response.assesment.AssesementResponse
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ResultViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    private val _assessment = MutableLiveData<AssesementResponse>()
    val assessment: LiveData<AssesementResponse> = _assessment

    private val _disfluency = MutableLiveData<AssesementResponse.Disfluency>()
    val disfluency: LiveData<AssesementResponse.Disfluency> = _disfluency

    private val _blink = MutableLiveData<AssesementResponse.Blink>()
    val blink: LiveData<AssesementResponse.Blink> = _blink

    private val _gaze = MutableLiveData<AssesementResponse.EyeGaze>()
    val gaze: LiveData<AssesementResponse.EyeGaze> = _gaze

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "ResultViewModel"
    }

    fun findAssessment(url: String, id: String) {
        val date = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US).format(System.currentTimeMillis()).toString()
        val client = ApiConfig.getApiService().getAssessment(url, id)
        client.enqueue(object : retrofit2.Callback<AssesementResponse> {
            override fun onResponse(
                call: Call<AssesementResponse>,
                response: Response<AssesementResponse>
            ) {
                if (response.isSuccessful) {
                    _assessment.value = response.body()
                    _disfluency.value = response.body()?.disfluency
                    _blink.value = response.body()?.blink
                    _gaze.value = response.body()?.gaze

                    val assessment =  AssessmentEntity(
                        donwloadUrl = url,
                        score = response.body()!!.score,
                        timeStamp = date,
                        gaze = response.body()?.gaze!!.value,
                        blink = response.body()?.blink!!.value,
                        disfluency = response.body()?.disfluency!!.value,
                    )
                    setHistory(assessment)
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AssesementResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun setHistory(assessmentEntity: AssessmentEntity) {
        return firebaseRepository.setUser(assessmentEntity)
    }
}