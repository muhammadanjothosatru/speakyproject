package com.bighero.speaky.ui.detail.praktik

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ActivityDetailPracticeBinding
import com.bighero.speaky.ui.detail.module.DetailModuleActivity
import com.bighero.speaky.ui.home.fragment.practice.PracticeViewModel
import com.bighero.speaky.util.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.TimeUnit

class DetailPracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPracticeBinding
    private lateinit var detailPracticeViewModel: DetailPracticeViewModel
    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        detailPracticeViewModel = ViewModelProvider(this,factory)[DetailPracticeViewModel::class.java]
        val bundle = intent.extras
        if (bundle != null) {
            val practiceId = bundle.getString(DetailModuleActivity.EXTRA_TITLE)
            if (practiceId != null) {
                setPractice(practiceId)
            }
        }

        supportActionBar?.title = ""

    }

    @SuppressLint("SetTextI18n")
    private fun setPractice(practiceId: String) {
        detailPracticeViewModel.getPraticeById(practiceId).observe(this, { response ->
            response.pratice?.let {
                binding.tvTitle.text = it.judul
                binding.tvDesc.text = it.judul
                binding.tvDuration.text = TimeUnit.MILLISECONDS.toSeconds(it.ilustrasi.durasi).toString() + " " +
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