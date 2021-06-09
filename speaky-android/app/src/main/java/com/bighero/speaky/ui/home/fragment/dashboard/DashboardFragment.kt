package com.bighero.speaky.ui.home.fragment.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ContentDashboardBinding
import com.bighero.speaky.databinding.FragmentDashboardBinding
import com.bighero.speaky.ui.assessment.PraAssessmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var detailBinding: ContentDashboardBinding

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!

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
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        detailBinding = binding.detailContent
        database = Firebase.database.reference
        auth = Firebase.auth
        uId = auth.currentUser!!.uid
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        database.child("users").child(uId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.child("level").value}")
            detailBinding.detailLevel.text = it.child("level").value.toString()
            detailBinding.skor.text = it.child("latest score").value.toString()
            showLoading(false)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            showLoading(false)
        }

        database.child("users").child(uId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.child("level").value}")
            detailBinding.detailLevel.text = it.child("level").value.toString()
            detailBinding.skor.text = it.child("latest score").value.toString()
            showLoading(false)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
            showLoading(false)
        }

        detailBinding.btTest.setOnClickListener {
            startActivity(Intent(activity, PraAssessmentActivity::class.java))
            activity?.finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notif_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notif -> {
                Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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