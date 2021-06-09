package com.bighero.speaky.ui.detail.user.subs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bighero.speaky.databinding.ActivitySubsBinding

class SubsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}