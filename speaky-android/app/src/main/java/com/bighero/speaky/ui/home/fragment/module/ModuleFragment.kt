package com.bighero.speaky.ui.home.fragment.module

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.data.source.remote.response.module.ModuleResponse
import com.bighero.speaky.databinding.ContentModuleBinding
import com.bighero.speaky.databinding.FragmentModuleBinding
import com.bighero.speaky.ui.detail.module.DetailModuleActivity
import com.bighero.speaky.util.ViewModelFactory

class ModuleFragment : Fragment(), ModuleAdapter.OnItemClickCallback {
    private lateinit var moduleViewModel: ModuleViewModel
    private lateinit var detailBinding: ContentModuleBinding
    private var _binding: FragmentModuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var moduleAdapter: ModuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        moduleViewModel = ViewModelProvider(this, factory)[ModuleViewModel::class.java]
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        detailBinding = binding.detailContent
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        moduleAdapter = ModuleAdapter()
        getModule()

    }

    private fun getModule() {
        moduleViewModel.getModule().observe(viewLifecycleOwner, {
            printa(it)
        })
    }

    private fun printa(response: ModuleResponse) {
        response.module?.let {
            _binding?.detailContent?.rvAnotherModule?.layoutManager = GridLayoutManager(context, 2)
            moduleAdapter.setModule(it)
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
        showLoading(false)
    }

    override fun onItemClicked(id: String) {
        val intent = Intent(context, DetailModuleActivity::class.java)
        intent.putExtra(DetailModuleActivity.EXTRA_TITLE, id)
        context?.startActivity(intent)
    }

    private fun showLoading(i: Boolean) {
        binding.progressBar.isVisible = i
        binding.content.isInvisible = i
    }
}