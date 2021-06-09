package com.bighero.speaky.ui.detail.module.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.R
import com.bighero.speaky.databinding.FragmentHistoryBinding
import com.bighero.speaky.databinding.FragmentListBabBinding
import com.bighero.speaky.ui.detail.module.DetailModuleViewModel
import com.bighero.speaky.ui.detail.module.ModuleReaderCallback
import com.bighero.speaky.ui.detail.module.adapter.BabAdapter
import com.bighero.speaky.ui.detail.module.adapter.GalleryAdapter
import com.bighero.speaky.ui.home.fragment.history.HistoryViewModel
import com.bighero.speaky.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ListBabFragment : Fragment(), BabAdapter.BabAdapterClickListener,
    GalleryAdapter.GalleryAdapterClickListener {

    private var _binding: FragmentListBabBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailModuleViewModel
    private lateinit var moduleReaderCallback: ModuleReaderCallback
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var listAdapter: BabAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference
    private lateinit var uId: String
    private var status: Boolean = false

    companion object {
        var EXTRA_ID = "extra_id"
        val TAG: String = ListBabFragment::class.java.simpleName
        fun newInstance(moduleId : String): ListBabFragment  {
            val fragment = ListBabFragment()
            val bundle = Bundle().apply {
                putString(EXTRA_ID, moduleId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBabBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]
        val moduleId = arguments?.getString(EXTRA_ID)

        if (moduleId != null) {
            showBab(moduleId)
        }

        galleryAdapter = GalleryAdapter(this)
        listAdapter = BabAdapter(this)

        database = Firebase.database.reference
        auth = Firebase.auth
        uId = auth.currentUser!!.uid

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        database.child("users").child(uId).child("status").get().addOnSuccessListener {
            status = it.value as Boolean
        }
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]

        binding.btUnlock.setOnClickListener {
            unlockModul()
        }
    }

    private fun showBab(moduleId: String) {
        viewModel.setSelectedModule(moduleId).observe(viewLifecycleOwner, { response ->
            response.module?.let {
                Log.d("modul id", it.toString())
                binding.detailContent.judulmodul.text = it.judul
                binding.detailContent.deskripsimodul.text = it.deskripsi

                binding.detailContent.rvListModule.layoutManager = LinearLayoutManager(context)
                listAdapter.setModule(it.bab)
                listAdapter.notifyDataSetChanged()
                binding.detailContent.rvListModule.setHasFixedSize(true)
                binding.detailContent.rvListModule.adapter = listAdapter

                binding.detailContent.rvGalleryModule.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL, false)
                galleryAdapter.setModule(it.bab)
                galleryAdapter.notifyDataSetChanged()
                binding.detailContent.rvGalleryModule.setHasFixedSize(true)
                binding.detailContent.rvGalleryModule.adapter = galleryAdapter

                showLoading(false)
            }

            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
        })

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        moduleReaderCallback = context as ModuleReaderCallback
    }

    override fun onItemClicked(position: Int, babId: String) {
        val moduleId = arguments?.getString(EXTRA_ID)
        moduleReaderCallback.moveTo(position, babId, moduleId)
    }

    private fun unlockModul() {
            if (!status) {
                Toast.makeText(context, "Kamu harus berlangganan dulu!", Toast.LENGTH_SHORT).show()
            } else {
                binding.btUnlock.isVisible = false
                binding.gradient.isVisible = false
                binding.content.isClickable = true
                binding.detailContent.rvGalleryModule.isEnabled = true
                binding.detailContent.rvListModule.isEnabled = true
                binding.imgLock.isVisible = false
                binding.tvLock.isVisible = false

            }
        }
    }