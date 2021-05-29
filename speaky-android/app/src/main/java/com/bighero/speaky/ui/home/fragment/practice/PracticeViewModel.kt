package com.bighero.speaky.ui.home.fragment.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PracticeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is practice Fragment"
    }
    val text: LiveData<String> = _text
}