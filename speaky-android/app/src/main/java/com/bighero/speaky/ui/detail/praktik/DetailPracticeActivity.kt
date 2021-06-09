package com.bighero.speaky.ui.detail.praktik

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ActivityDetailPracticeBinding
import com.bighero.speaky.ui.detail.module.DetailModuleActivity
import com.bighero.speaky.util.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class DetailPracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPracticeBinding
    private lateinit var detailPracticeViewModel: DetailPracticeViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var uId: String
    private var status: Boolean = false

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        detailPracticeViewModel =
            ViewModelProvider(this, factory)[DetailPracticeViewModel::class.java]
        val bundle = intent.extras
        if (bundle != null) {
            val practiceId = bundle.getString(DetailModuleActivity.EXTRA_TITLE)
            if (practiceId != null) {
                setPractice(practiceId)
            }
        }
        database = Firebase.database.reference
        auth = Firebase.auth
        uId = auth.currentUser!!.uid

        checkStatus(uId)
        binding.btStart.setOnClickListener {
            unlockPractice()
        }

        supportActionBar?.title = ""

    }

    private fun unlockPractice() {
        if (!status) {
            Toast.makeText(this, "Kamu harus beli modul dulu!", Toast.LENGTH_SHORT).show()
        } else {
            binding.btStart.isVisible = false
            binding.gradient.isVisible = false
            binding.imgLock.isVisible = false
            binding.tvLock.isVisible = false
        }
    }

    private fun checkStatus(uId: String) {
        database.child("users").child(uId).child("status").get().addOnSuccessListener {
            status = it.value as Boolean
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPractice(practiceId: String) {
        detailPracticeViewModel.getPraticeById(practiceId).observe(this, { response ->
            response.pratice?.let {
                binding.tvTitle.text = it.judul
                binding.tvDesc.text = it.judul
                binding.tvDuration.text =
                    TimeUnit.MILLISECONDS.toSeconds(it.ilustrasi.durasi).toString() + " " +
                            "Detik"
                binding.tvDesc.text = it.deskripsi
                Glide.with(this)
                    .load(it.cover)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(binding.imagepratice)
            }

            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
        })
    }
}