package com.bighero.speaky.ui.detail.user.subs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivitySubsBinding

class SubsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}