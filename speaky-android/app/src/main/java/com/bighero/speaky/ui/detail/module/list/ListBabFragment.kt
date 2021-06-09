package com.bighero.speaky.ui.detail.module.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bighero.speaky.ui.detail.module.adapter.BabAdapter
import com.bighero.speaky.ui.detail.module.adapter.GalleryAdapter
import com.bighero.speaky.ui.home.fragment.history.HistoryViewModel
import com.bighero.speaky.util.ViewModelFactory


class ListBabFragment : Fragment() {

    private var _binding: FragmentListBabBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailModuleViewModel
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var listAdapter: BabAdapter

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
    ): View? {
        _binding = FragmentListBabBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]
        val moduleId = arguments?.getString(EXTRA_ID)
        if (moduleId != null) {
            showBab(moduleId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]
        galleryAdapter = GalleryAdapter()
        listAdapter = BabAdapter()
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
}