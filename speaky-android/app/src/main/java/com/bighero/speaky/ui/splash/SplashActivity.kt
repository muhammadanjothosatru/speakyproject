package com.bighero.speaky.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bighero.speaky.R
import com.bighero.speaky.ui.home.HomeActivity
import com.bighero.speaky.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if(currentUser != null){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 3000)
    }

}