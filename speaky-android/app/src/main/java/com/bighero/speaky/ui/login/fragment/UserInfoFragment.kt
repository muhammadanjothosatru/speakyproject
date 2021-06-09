package com.bighero.speaky.ui.login.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.UserEntity
import com.bighero.speaky.databinding.FragmentUserInfoBinding
import com.bighero.speaky.ui.login.TermActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class UserInfoFragment : Fragment() {
    private var _binding : FragmentUserInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var uId: String

    var uri: Uri? = null

    private var email: String? = null

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("NameFragment", "Photo selected")

            uri = data.data
            binding.avatar.setImageURI(uri)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            email = arguments?.getString(EXTRA_EMAIL)
            Log.i("Get email", email.toString())
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
            uId = auth.currentUser?.uid.toString()
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
                                startActivity(Intent(activity, TermActivity::class.java))
                                showLoading(false)
                                activity?.finish()
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
                        Log.d("firebase","User profil updated")
                        writeNewUser(
                            binding.etName.text.toString(),
                            binding.etUsername.text.toString(),
                            email.toString(),
                            "",
                            getString(R.string.beginner),
                            false
                        )
                        startActivity(Intent(activity, TermActivity::class.java))
                        showLoading(false)
                        activity?.finish()
                    }
                }
        }

    }



    private fun writeNewUser(name: String, uname: String, email: String, ava: String, level: String, status: Boolean) {
        uId = auth.currentUser?.uid.toString()
        Log.i("uId", "Get ${uId}")
        val user = UserEntity(name, uname, email, ava, level, status,0)

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