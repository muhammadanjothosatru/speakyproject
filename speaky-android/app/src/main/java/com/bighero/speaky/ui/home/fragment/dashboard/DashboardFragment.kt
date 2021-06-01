package com.bighero.speaky.ui.home.fragment.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.databinding.FragmentDashboardBinding
import com.bighero.speaky.ui.assesment.AssesmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var uId : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = Firebase.database.reference
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        uId= auth.currentUser!!.uid
        binding.btTest.setOnClickListener {
            startActivity(Intent(activity, AssesmentActivity::class.java))
        }
        database.child("users").child(uId).child("name").get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            binding.name.text= it.value.toString()
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}