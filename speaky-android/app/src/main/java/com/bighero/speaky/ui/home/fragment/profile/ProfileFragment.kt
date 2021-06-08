package com.bighero.speaky.ui.home.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ContentProfileBinding
import com.bighero.speaky.databinding.FragmentProfileBinding
import com.bighero.speaky.ui.login.LoginActivity
import com.bighero.speaky.ui.user.EditProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private lateinit var detailBinding: ContentProfileBinding

    private lateinit var database: DatabaseReference
    private lateinit var uId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        detailBinding = binding.detailContent
        val root: View = binding.root
        database = Firebase.database.reference
        auth = Firebase.auth
        uId = auth.currentUser!!.uid
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        showProfile()
        detailBinding.cvEdit.setOnClickListener(this)
    }

    private fun showProfile() {
        database.child("users").child(uId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.child("name").value}")
            Log.i("firebase", "Got value ${it.child("status").value}")

            detailBinding.profileName.text = it.child("name").value.toString()
            val status = it.child("status").value
            if (status == false) {
                detailBinding.profileStatus.text = getString(R.string.free_user)
            } else {
                detailBinding.profileStatus.text = getString(R.string.premium_user)
            }
            val imgUrl = it.child("imgPhoto").value
            if (imgUrl != null) {
                Glide.with(requireContext())
                    .load(it.child("imgPhoto").value.toString())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.avatar_default)
                    .into(detailBinding.profileImage)
            }
            showLoading(false)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            showLoading(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Firebase.auth.signOut()
                startActivity((Intent(requireActivity(), LoginActivity::class.java)))
                activity?.finish()
                true
            }
            R.id.action_delete -> {
                showLoading(true)
                database.child("users").child(uId).removeValue()
                auth.currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("auth", "User account deleted.")
                        }
                    }
                startActivity(Intent(activity, LoginActivity::class.java))
                showLoading(false)
                activity?.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.cv_edit) {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        else {
            Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(i: Boolean) {
        if (i) {
            binding.progressBar.visibility = View.VISIBLE
            binding.content.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.content.visibility = View.VISIBLE
        }
    }
}