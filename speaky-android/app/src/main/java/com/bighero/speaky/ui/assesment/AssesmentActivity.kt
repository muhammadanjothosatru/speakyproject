package com.bighero.speaky.ui.assesment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.bighero.speaky.databinding.ActivityAssesmentBinding

class AssesmentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAssesmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAssesmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        }
        else{
            binding.btRecord.isEnabled = true
        }

        binding.btRecord.setOnClickListener {
            var i= Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, 123)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==123) {
            binding.videoView.setVideoURI(data?.data)
            binding.videoView.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            binding.btRecord.isEnabled = true
    }

    override fun onBackPressed() {
        this.finish()
    }
}