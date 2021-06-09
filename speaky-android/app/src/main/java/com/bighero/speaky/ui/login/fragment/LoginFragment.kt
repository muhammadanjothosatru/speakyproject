package com.bighero.speaky.ui.login.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bighero.speaky.R
import com.bighero.speaky.databinding.FragmentLoginBinding
import com.bighero.speaky.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.daftarDisini.setOnClickListener(this)
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
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
        showLoading(true)
        auth.signInWithEmailAndPassword(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                    showLoading(false)
                } else {
                    updateUI(null)
                    showLoading(false)
                }
            }


    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            activity?.finish()
        } else {
            Toast.makeText(
                requireContext(), "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.daftar_disini) {
            val mRegisterFragment = RegisterFragment()
            val mFragmentManager = fragmentManager
            mFragmentManager?.beginTransaction()?.apply {
                replace(
                    R.id.frame_container,
                    mRegisterFragment,
                    RegisterFragment::class.java.simpleName
                )
                commit()
            }
        }
    }

    fun showLoading(i: Boolean) {
        if (i) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnLogin.isClickable = false
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.btnLogin.isClickable = true
        }
    }


}