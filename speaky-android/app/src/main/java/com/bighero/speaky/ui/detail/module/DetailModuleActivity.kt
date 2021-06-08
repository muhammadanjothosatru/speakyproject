package com.bighero.speaky.ui.detail.module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ActivityDetailModuleBinding
import com.bighero.speaky.ui.detail.module.content.BabFragment
import com.bighero.speaky.ui.detail.module.list.ListBabFragment
import com.bighero.speaky.util.ViewModelFactory

class DetailModuleActivity : AppCompatActivity(), ModuleReaderCallback {
    private lateinit var binding: ActivityDetailModuleBinding
    private lateinit var viewModel: DetailModuleViewModel

    companion object {
        const val EXTRA_TITLE = "extra_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[DetailModuleViewModel::class.java]

        val bundle = intent.extras
        if (bundle != null) {
            val moduleId = bundle.getString(EXTRA_TITLE)
            if (moduleId != null) {
                viewModel.setSelectedModule(moduleId)
                populateFragment()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    private fun populateFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(ListBabFragment.TAG)
        if (fragment == null) {
            fragment = ListBabFragment.newInstance()
            fragmentTransaction.add(R.id.frame_container, fragment, ListBabFragment.TAG)
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    override fun moveTo(position: Int, babId: String) {
        val fragment = BabFragment.newInstance()
        supportFragmentManager.beginTransaction().add(
            R.id.frame_container, fragment, BabFragment.TAG)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}