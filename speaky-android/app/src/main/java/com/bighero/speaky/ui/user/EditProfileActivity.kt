package com.bighero.speaky.ui.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.UserEntity
import com.bighero.speaky.databinding.ActivityEditProfileBinding
import com.bighero.speaky.ui.home.HomeActivity
import com.bighero.speaky.ui.login.TermActivity
import com.bighero.speaky.ui.login.fragment.UserInfoFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var uId: String

    private var uri: Uri? = null

    private var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference
        uId = auth.currentUser?.uid.toString()

        showProfile(uId)

        binding.avatar.setOnClickListener {
            val pIntent = Intent(Intent.ACTION_PICK)
            pIntent.type = "image/*"
            startActivityForResult(pIntent, 0)
        }

        binding.gantiFoto.setOnClickListener {
            val pIntent = Intent(Intent.ACTION_PICK)
            pIntent.type = "image/*"
            startActivityForResult(pIntent, 0)
        }

        binding.btNext.setOnClickListener{
            registerUser()
        }
    }

    private fun showProfile(uId: String) {
        database.child("users").child(uId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.child("name").value}")
            Log.i("firebase", "Got value ${it.child("status").value}")

            binding.etName.setText(it.child("name").value.toString())
            binding.etUsername.setText(it.child("username").value.toString())
            val imgUrl = it.child("imgPhoto").value
            if (imgUrl != null) {
                Glide.with(this)
                    .load(it.child("imgPhoto").value.toString())
                    .into(binding.avatar)
            }
            showLoading(false)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            showLoading(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("NameFragment", "Photo selected")

            uri = data.data
            binding.avatar.setImageURI(uri)
        }

    }

    private fun registerUser() {

        if (binding.etName.text.toString().isEmpty()) {
            binding.etName.error = "Please input your name"
            binding.etName.requestFocus()
            return
        }
        if (binding.etUsername.text.toString().isEmpty()) {
            binding.etUsername.error = "Please input your username"
            binding.etUsername.requestFocus()
            return
        }
        showLoading(true)

        if (uri != null) {
            Log.i("uId", "Get $uId")
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now)
            val storageRef = FirebaseStorage.getInstance().getReference("avatar/${uId}/$filename")

            val uploadTask = uri.let { storageRef.putFile(it!!) }
            uploadTask.
            addOnSuccessListener {
                binding.avatar.setImageURI(null)
            }.addOnFailureListener{
                Log.e("upload image", "fail")
            }

            uploadTask.continueWithTask{ task ->
                if (!task.isSuccessful) {
                    task.exception.let {
                        throw it!!
                    }
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val profileUpdates = userProfileChangeRequest {
                        displayName = binding.etName.text.toString()
                        photoUri = Uri.parse(downloadUri.toString())
                    }
                    auth.currentUser!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("firebase","User profil updated")
                                writeNewUser(
                                    binding.etName.text.toString(),
                                    binding.etUsername.text.toString(),
                                    email.toString(),
                                    downloadUri.toString(),
                                    getString(R.string.beginner),
                                    false
                                )
                                startActivity(Intent(this, HomeActivity::class.java))
                                showLoading(false)
                                finish()
                            }
                        }
                } else {
                    Log.e("error firebase", " upload image")
                }
            }


        } else {
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.etName.text.toString()
            }
            auth.currentUser!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("firebase","User profile updated")
                        database.child("users").child(uId).child("name").setValue(binding.etName.text.toString())
                        database.child("users").child(uId).child("username").setValue(binding.etUsername.text.toString())
                        startActivity(Intent(this, HomeActivity::class.java))
                        showLoading(false)
                        finish()
                    }
                }
        }

    }



    private fun writeNewUser(name: String, uname: String, email: String, ava: String, level: String, status: Boolean) {
        uId = auth.currentUser?.uid.toString()
        Log.i("uId", "Get ${uId}")
        val user = UserEntity(name, uname, email, ava, level, status)

        database.child("users").child(uId).setValue(user)
    }


    private fun showLoading(i: Boolean) {
        if (i) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btNext.isClickable = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.btNext.isClickable = true
        }
    }
}