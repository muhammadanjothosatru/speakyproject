package com.bighero.speaky.ui.assesment.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bighero.speaky.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}