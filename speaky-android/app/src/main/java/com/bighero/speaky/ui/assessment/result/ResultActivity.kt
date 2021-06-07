package com.bighero.speaky.ui.assessment.result

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bighero.speaky.databinding.ActivityResultBinding
import com.bighero.speaky.databinding.ContentResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResultActivity : AppCompatActivity() {
    private val viewModel: ResultViewModel by viewModels()
    private lateinit var binding: ActivityResultBinding
    private lateinit var detailBinding: ContentResultBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var uId: String

    companion object {
        const val EXTRA_TES = "extra_tes"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        detailBinding = binding.detailContent
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = Firebase.auth
        uId = auth.currentUser!!.uid

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val url = extras.getString(EXTRA_TES).toString()
            Log.i("formatTes", url)
            viewModel.findAssessment(url, uId)
        }

        viewModel.assessment.observe(this, { assessment ->

            detailBinding.score.text = assessment.score.toString()
        })
        viewModel.disfluency.observe(this, { disfluency ->
            detailBinding.disValue.text = disfluency.value.toString()
        })
        viewModel.blink.observe(this, { blink ->
            detailBinding.blinkValue.text = blink.value.toString()
        })
        viewModel.isLoading.observe(this, {
            binding.progressBar.isVisible = it
            binding.content.isInvisible = it
        })
    }
}