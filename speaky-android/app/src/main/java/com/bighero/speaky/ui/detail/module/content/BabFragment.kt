package com.bighero.speaky.ui.detail.module.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.FragmentBabBinding
import com.bighero.speaky.ui.detail.module.DetailModuleViewModel
import com.bighero.speaky.util.ViewModelFactory

class BabFragment : Fragment() {

    private lateinit var viewModel: DetailModuleViewModel

    private var _binding : FragmentBabBinding? = null
    private val binding get() = _binding

    companion object {
        val TAG: String = BabFragment::class.java.simpleName

        fun newInstance(): BabFragment {
            return BabFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(requireActivity(), factory)[DetailModuleViewModel::class.java]


        }
    }
}