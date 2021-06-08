package com.bighero.speaky.ui.detail.module.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
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
        val TAG: String = ListBabFragment::class.java.simpleName
        fun newInstance(): ListBabFragment = ListBabFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]

        galleryAdapter = GalleryAdapter()
        listAdapter = BabAdapter()
        getBab()
    }

    private fun getBab() {
        viewModel.getBab().observe(requireActivity(), {
            //printa(it)
        })
    }
/*
    private fun printa(it: ModuleResponse?) {
        it?.module?.let {
            detailBinding.rvGalleryModule.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            galleryAdapter.setModule()
            moduleAdapter.notifyDataSetChanged()
            moduleAdapter.setOnClickCallback(this)
            _binding?.detailContent?.rvAnotherModule?.setHasFixedSize(true)
            _binding?.detailContent?.rvAnotherModule?.adapter = moduleAdapter
        }

        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("exception", it)
            }
        }
    }

 */

    private fun showLoading(i: Boolean) {
        binding.progressBar.isVisible = i
        binding.content.isInvisible = i
    }
}