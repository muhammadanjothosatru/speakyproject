package com.bighero.speaky.ui.home.fragment.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bighero.speaky.data.source.remote.response.assesment.UserAssesmentResponse
import com.bighero.speaky.databinding.FragmentHistoryBinding
import com.bighero.speaky.ui.detail.assessment.DetailResultActivity
import com.bighero.speaky.ui.detail.module.DetailModuleActivity
import com.bighero.speaky.util.ViewModelFactory

class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickCallback {

    private lateinit var historyViewModel: HistoryViewModel
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        historyViewModel = ViewModelProvider(this,factory)[HistoryViewModel::class.java]
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        historyAdapter = HistoryAdapter()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showLoading(true)
        gethistory()
    }

    private fun gethistory() {
        historyViewModel.getResponseUsingLiveData().observe(viewLifecycleOwner, {
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
                    historyAdapter.setOnClickCallback(this)
                    binding.rvHistory.setHasFixedSize(true)
                    binding.rvHistory.adapter = historyAdapter
                }
        }
        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("exception", it)
            }
        }
        showLoading(false)
    }

    private fun showLoading(i: Boolean) {
        binding.progressBar.isVisible = i
        binding.rvHistory.isInvisible = i
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(date: String) {
        val intent = Intent(context, DetailModuleActivity::class.java)
        intent.putExtra(DetailResultActivity.EXTRA_ID, date)
        context?.startActivity(intent)
    }
}