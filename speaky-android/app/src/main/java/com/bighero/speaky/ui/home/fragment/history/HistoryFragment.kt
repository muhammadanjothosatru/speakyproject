package com.bighero.speaky.ui.home.fragment.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.data.source.remote.response.PractiveResponse
import com.bighero.speaky.databinding.FragmentHistoryBinding
import com.bighero.speaky.util.ViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var homeViewModel: HistoryViewModel
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getResponseUsingLiveData()
    }
    private fun getResponseUsingLiveData() {
        homeViewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, {
            printa(it)
        })
    }
    private fun printa(response: PractiveResponse) {
        response.history?.let { products ->
            products.forEach{ product ->
                product.title.let {
                    Log.i("title", it)
                }
            }
        }

        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("exception", it)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}