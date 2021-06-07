package com.bighero.speaky.ui.home.fragment.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bighero.speaky.data.source.remote.response.UserAssesmentResponse
import com.bighero.speaky.databinding.FragmentHistoryBinding
import com.bighero.speaky.util.ViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var homeViewModel: HistoryViewModel
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        historyAdapter = HistoryAdapter()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gethistory()
    }

    private fun gethistory() {
        homeViewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, {
            printa(it)
        })
    }

    private fun printa(response: UserAssesmentResponse) {
        response.history?.let {
                if (it.isEmpty()) {
                    Toast.makeText(activity,"Kamu belum pernah Test",Toast.LENGTH_LONG).show()
                } else {
                    binding.rvHistory.layoutManager = LinearLayoutManager(context)
            historyAdapter.setHistory(it)
            historyAdapter.notifyDataSetChanged()
            binding.rvHistory.setHasFixedSize(true)
            binding.rvHistory.adapter = historyAdapter
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