package com.bighero.speaky.ui.detail.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivityTouBinding

class TouActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTouBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTouBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onBackPressed() {
        finish()
    }
}