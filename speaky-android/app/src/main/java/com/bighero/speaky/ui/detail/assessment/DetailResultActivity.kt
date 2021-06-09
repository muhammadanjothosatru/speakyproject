package com.bighero.speaky.ui.detail.assessment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.databinding.ActivityDetailResultBinding
import com.bighero.speaky.ui.assessment.AssessmentViewModel
import com.bighero.speaky.util.ViewModelFactory
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class DetailResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultBinding
    private lateinit var detailResultViewModel: DetailResultViewModel
    companion object {
        const val EXTRA_ID = "extra_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(EXTRA_ID)

        val factory = ViewModelFactory.getInstance(this)
        detailResultViewModel = ViewModelProvider(this,factory)[DetailResultViewModel::class.java]
        showLoading(true)
        setHasil(id.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun setHasil(id: String) {
        detailResultViewModel.getResult(id).observe(this, { response ->
            response.history?.let {
                binding.detailContent.blinkValue.text = it.blink.toString() + "kali"
                binding.detailContent.gazeValue.text = it.gaze.toString() + "kali"
                binding.detailContent.disValue.text = it.disfluency.toString() + "kali"
                binding.detailContent.skorValue.text = it.score.toString()
                binding.detailContent.timeValue.text = it.timeStamp
                showLoading(false)
            }

            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
        })
    }

    private fun formattime(times: String): String {
        val outputFormat= SimpleDateFormat("HH:mm", Locale.US)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US)
        val dateFormat = inputFormat.parse(times)
        val date: String = outputFormat.format(dateFormat)
        return date
    }

    private fun formatdate(times: String): String {
        val formater = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm", Locale.US)
        val date = LocalDate.parse(times,formater).toString()
        return date
    }

    private fun showLoading(i: Boolean) {
        binding.progressBar.isVisible = i
    }
}