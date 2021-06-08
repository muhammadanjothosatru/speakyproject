package com.bighero.speaky.ui.home.fragment.module

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bighero.speaky.data.source.remote.response.ModuleResponse
import com.bighero.speaky.databinding.FragmentModuleBinding
import com.bighero.speaky.util.ViewModelFactory

class ModuleFragment : Fragment() {
    private lateinit var moduleViewModel: ModuleViewModel
    private var _binding: FragmentModuleBinding? = null
    private val binding get() = _binding!!
    private lateinit var moduleAdapter: ModuleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        moduleViewModel = ViewModelProvider(this, factory)[ModuleViewModel::class.java]
        moduleAdapter = ModuleAdapter()
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            _binding?.detailContent?.rvAnotherModule?.setHasFixedSize(true)
            _binding?.detailContent?.rvAnotherModule?.adapter = moduleAdapter
        }

        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("exception", it)
            }
        }
    }
}