package com.bighero.speaky.ui.detail.assessment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivityDetailResultBinding

class DetailResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}