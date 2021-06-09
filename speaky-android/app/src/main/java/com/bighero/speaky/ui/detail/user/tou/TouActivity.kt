package com.bighero.speaky.ui.detail.user.tou

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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