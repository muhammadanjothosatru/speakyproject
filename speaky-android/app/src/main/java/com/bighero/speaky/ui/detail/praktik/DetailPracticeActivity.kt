package com.bighero.speaky.ui.detail.praktik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bighero.speaky.databinding.ActivityDetailPracticeBinding
import com.bighero.speaky.ui.detail.module.DetailModuleActivity

class DetailPracticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPracticeBinding

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            val practiceId = bundle.getString(DetailModuleActivity.EXTRA_TITLE)
            if (practiceId != null) {
                setPractice(practiceId)
            }
        }

        supportActionBar?.title = ""

    }

    private fun setPractice(practiceId: String) {
        TODO("Not yet implemented")
    }
}