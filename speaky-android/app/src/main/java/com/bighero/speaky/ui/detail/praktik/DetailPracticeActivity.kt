package com.bighero.speaky.ui.detail.praktik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivityDetailPracticeBinding

class DetailPracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPracticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}