package com.bighero.speaky.ui.home.fragment.practice

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
import androidx.recyclerview.widget.GridLayoutManager
import com.bighero.speaky.databinding.ContentPracticeBinding
import com.bighero.speaky.databinding.FragmentPracticeBinding
import com.bighero.speaky.ui.detail.praktik.DetailPracticeActivity
import com.bighero.speaky.util.ViewModelFactory

class PracticeFragment : Fragment(), PracticeAdapter.OnItemClickCallback {

    private lateinit var practiceViewModel: PracticeViewModel
    private var _binding: FragmentPracticeBinding? = null
    private lateinit var practiceAdapter: PracticeAdapter
    private lateinit var detailBinding: ContentPracticeBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        practiceViewModel = ViewModelProvider(this, factory)[PracticeViewModel::class.java]
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)
        detailBinding = binding.detailContent
        practiceAdapter = PracticeAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)
        getPractice()
    }

    private fun getPractice() {
        practiceViewModel.getPratice().observe(viewLifecycleOwner, { response ->
            response.pratice?.let {
                if (it.isEmpty()) {
                    Toast.makeText(activity, "Practice belum ada", Toast.LENGTH_LONG).show()
                } else {
                    Log.e("practice", it.toString())
                    binding.detailContent.rvPratice.layoutManager = GridLayoutManager(context, 2)
                    practiceAdapter.setPratice(it)
                    practiceAdapter.notifyDataSetChanged()
                    practiceAdapter.setOnClickCallback(this)
                    binding.detailContent.rvPratice.setHasFixedSize(true)
                    binding.detailContent.rvPratice.adapter = practiceAdapter

                }
            }
            response.exception?.let { exception ->
                exception.message?.let {
                    Log.e("exception", it)
                }
            }
            showLoading(false)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(i: Boolean) {
        binding.progressBar.isVisible = i
        binding.content.isInvisible = i
    }

    override fun onItemClicked(key: String) {
        val intent = Intent(context, DetailPracticeActivity::class.java)
        intent.putExtra(DetailPracticeActivity.EXTRA_TITLE, key)
        context?.startActivity(intent)
    }
}