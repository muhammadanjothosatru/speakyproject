package com.bighero.speaky.ui.detail.module.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bighero.speaky.R
import com.bighero.speaky.databinding.FragmentBabBinding
import com.bighero.speaky.databinding.FragmentListBabBinding

class BabFragment : Fragment() {
    private var _binding: FragmentBabBinding? = null
    private val binding get() = _binding!!
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
        return binding.root
    }
}