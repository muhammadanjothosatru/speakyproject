package com.bighero.speaky.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivityTermBinding
import com.bighero.speaky.ui.home.HomeActivity

class TermActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAccept.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }
}