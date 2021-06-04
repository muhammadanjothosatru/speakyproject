package com.bighero.speaky.ui.login.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.UserEntity
import com.bighero.speaky.databinding.FragmentRegisterBinding
import com.bighero.speaky.ui.login.TermActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateInput()
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        if (binding.etName.text.toString().isEmpty()) {
            binding.etName.error = "Please input your name"
            binding.etName.requestFocus()
            return
        }
        if (binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.error = "Please input your email"
            binding.etEmail.requestFocus()
            return
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.error = "Please input your password"
            binding.etPassword.requestFocus()
            return
        }
        if (binding.etConfirmPassword.text.toString().isEmpty()) {
            binding.etConfirmPassword.error = "Please input your confirmation password"
            binding.etConfirmPassword.requestFocus()
            return
        }

        showLoading(true)
        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    writeNewUser(
                        binding.etName.text.toString(),
                        binding.etEmail.text.toString(),
                        getString(R.string.beginner),
                        false
                    )
                    showLoading(false)
                    startActivity(Intent(activity, TermActivity::class.java))
                    activity?.finish()
                } else {
                    Toast.makeText(
                        requireContext(), "Register failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                }
            }

    }

    private fun writeNewUser(name: String, email: String, level: String, status: Boolean) {
        val uId = auth.currentUser!!.uid
        val user = UserEntity(name, email, level, status)

        database.child("users").child(uId).setValue(user)
    }

    @SuppressLint("CheckResult")
    private fun validateInput() {
        val emailStream = RxTextView.textChanges(binding.etEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExistAlert(it)
        }

        val passwordStream = RxTextView.textChanges(binding.etPassword)
            .skipInitialValue()
            .map { password ->
                password.length <6
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(binding.etPassword)
                .map { password ->
                    password.toString() != binding.etConfirmPassword.text.toString()
                },
            RxTextView.textChanges(binding.etConfirmPassword)
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.etPassword.text.toString()
                }
        )
        passwordConfirmationStream.subscribe {
            showPasswordConfirmationAlert(it)
        }

        val invalidFieldsStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmationStream,
            { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid
            }
        )
        invalidFieldsStream.subscribe { isValid ->
            binding.btnRegister.isEnabled = isValid
        }

        binding.masukDisini.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.masuk_disini) {
            val mLoginFragment = LoginFragment()
            val mFragmentManager = fragmentManager
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container, mLoginFragment,LoginFragment::class.java.simpleName)
                commit()
            }
        }
    }

    private fun showEmailExistAlert(isNotValid: Boolean) {
        binding.etEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }

    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        binding.etPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }

    private fun showPasswordConfirmationAlert(isNotValid: Boolean) {
        binding.etConfirmPassword.error = if (isNotValid) getString(R.string.password_not_same) else null
    }

    private fun showLoading(i: Boolean) {
        if (i) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnRegister.isClickable = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.btnRegister.isClickable = true
        }
    }

}