package com.bighero.speaky.ui.home.fragment.practice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bighero.speaky.databinding.FragmentPracticeBinding
import com.bighero.speaky.ui.home.fragment.history.HistoryViewModel
import com.bighero.speaky.util.ViewModelFactory

class PracticeFragment : Fragment() {

    private lateinit var practiceViewModel: PracticeViewModel
    private var _binding: FragmentPracticeBinding? = null
    private lateinit var practiceAdapter: PracticeAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        practiceViewModel = ViewModelProvider(this,factory)[PracticeViewModel::class.java]
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
        practiceAdapter = PracticeAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPratice()
    }

    private fun getPratice() {
        practiceViewModel.getPratice().observe(viewLifecycleOwner, { response ->
            response.pratice?.let {
                if (it.isEmpty()) {
                    Toast.makeText(activity,"Pratice belum ada", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("pratice", it.toString())
                    binding.detailContent.rvPratice.layoutManager = GridLayoutManager(context,2)
                    practiceAdapter.setPratice(it)
                    practiceAdapter.notifyDataSetChanged()
                    binding.detailContent.rvPratice.setHasFixedSize(true)
                    binding.detailContent.rvPratice.adapter = practiceAdapter
                }
            }
            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}