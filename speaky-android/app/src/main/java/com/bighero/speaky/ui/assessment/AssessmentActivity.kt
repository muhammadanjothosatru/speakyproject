package com.bighero.speaky.ui.assessment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ActivityAssessmentBinding
import com.bighero.speaky.ui.assessment.result.ResultActivity
import com.bighero.speaky.ui.assessment.result.ResultViewModel
import com.bighero.speaky.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAssessmentBinding
    private lateinit var viewModel: AssessmentViewModel

    private lateinit var timer: CountDownTimer
    private var videoCapture: VideoCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var uId: String
    private var storage: FirebaseStorage = Firebase.storage
    var storageRef = storage.reference.child("video")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssessmentBinding.inflate(layoutInflater)

        showLoading(true)
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this,factory)[AssessmentViewModel::class.java]
        setContentView(binding.root)

        auth = Firebase.auth
        uId = auth.currentUser!!.uid
        database = Firebase.database.reference

        val extras = intent.extras
        if (extras != null) {
            val packId = extras.getString(EXTRA_ID).toString()
            Log.i("formatTes", packId)
            showPack(packId)
        }

        binding.btBack.setOnClickListener {
            onBackPressed()
        }

        if (allPermissionsGranted()) {
            startCamera()
            showLoading(false)

        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()

    }

    private fun showLoading(b: Boolean) {
        binding.progressBar.isVisible = b
    }

    private fun showPack(packId:String) {
        viewModel.findAssessmentPack(packId).observe(this, { response ->
            response.intruction?.let { it ->
                        binding.jenisTes.text = it.type
                        binding.topik.text = it.intruksi.random().toString()
                }

            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture
                .get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            videoCapture = VideoCapture.Builder()
                .setTargetResolution(Size(256, 144))
                .setMaxResolution(Size(256, 144))
                .setVideoFrameRate(25)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

        timer = object : CountDownTimer(5000, 1000) {
            @SuppressLint("ResourceAsColor")
            override fun onTick(millisUntilFinished: Long) {
                binding.countDown.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                takeVideo()
            }
        }
        timer.start()


    }

    @SuppressLint("RestrictedApi", "MissingPermission")
    private fun takeVideo() {
        Log.d(TAG, "start")
        // Get a stable reference of the modifiable image capture use case
        val videoCapture = videoCapture ?: return

        timer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.startTime.text = getString(R.string.sisa_waktu)
                binding.countDown.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                videoCapture.stopRecording()
                showLoading(true)
            }
        }
        // Create time-stamped output file to hold the image
        val date = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val cacheFile = File(
            externalCacheDir?.absolutePath, "video-" + date + ".mp4"
        )

        // Create output options object which contains file + metadata
        val outputOptions = VideoCapture.OutputFileOptions.Builder(cacheFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        videoCapture.startRecording(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(cacheFile)
                    val uploadRef = storageRef.child("${uId}/${savedUri.lastPathSegment}")
                    val msg = "Video capture succeeded: $savedUri"

                    Log.d(TAG, msg)
                    val uploadTask = uploadRef.putFile(savedUri)
                    uploadTask.addOnFailureListener {
                        Log.e(TAG, msg)
                    }.addOnSuccessListener { taskSnapshot ->
                        val meta = taskSnapshot.metadata
                        Log.i(TAG, meta.toString())
                    }

                    val urlTask = uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw  it
                            }
                        }
                        uploadRef.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result.toString()
                            Log.i(TAG, downloadUri)
                            binding.progressBar.visibility = View.INVISIBLE
                            val intent = Intent(this@AssessmentActivity, ResultActivity::class.java)
                            intent.putExtra(ResultActivity.EXTRA_TES, downloadUri)
                            startActivity(intent)
                            showLoading(false)
                        } else {
                            Log.e(TAG, "failed")
                        }
                        finish()
                    }
                    Log.i("firebase url", urlTask.toString())
                    cacheFile.delete()
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Log.e(TAG, "Photo capture failed: $message")
                }
            })
        timer.start()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val EXTRA_ID = "extra_title"
        const val EXTRA_DETAIL = "extra_detail"
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Keluar")
            .setMessage("Kamu yakin ingin keluar dari tes?")
            .setCancelable(false)
            .setPositiveButton("Ya") {_,_->
                finish()
            }.setNegativeButton("Tidak") {dialog, _ -> dialog.cancel()}

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}