package com.bighero.speaky.ui.detail.module.content

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.FragmentBabBinding
import com.bighero.speaky.ui.detail.module.DetailModuleViewModel
import com.bighero.speaky.ui.detail.module.list.ListBabFragment
import com.bighero.speaky.util.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BabFragment : Fragment() {

    private lateinit var viewModel: DetailModuleViewModel
    private var _binding : FragmentBabBinding? = null
    private val binding get() = _binding

    companion object {
        val TAG: String = BabFragment::class.java.simpleName
        var BAB_ID = "bab_id"
        var MODULE_ID = "extra_id"
        fun newInstance(babid:String, moduleId:String?): BabFragment {
            val fragment = BabFragment()
            val bundle = Bundle().apply {
                putString(BabFragment.BAB_ID, babid)
                putString(BabFragment.MODULE_ID, moduleId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireActivity())
            viewModel = ViewModelProvider(requireActivity(), factory)[DetailModuleViewModel::class.java]
            val moduleId = arguments?.getString(BabFragment.MODULE_ID)
            val babId = arguments?.getString(BabFragment.BAB_ID)
            if (moduleId != null && babId != null) {
                viewModel.getBabById(babId,moduleId).observe(viewLifecycleOwner,  { response ->
                    response.module?.let {
                        binding?.detailContent?.titleBab?.text = it.judul
                        binding?.detailContent?.deskipsi?.text = it.konten
                        val image = binding?.detailContent?.videoBab
                        Glide.with(requireActivity())
                            .load(it.video)
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                            .error(R.drawable.ic_error)
                            .into(image!!)
                    }

                    response.exception?.let { exception ->
                        exception.message?.let {
                            Log.e("exception", it)
                        }
                    }
                })
            }

        }
    }
}