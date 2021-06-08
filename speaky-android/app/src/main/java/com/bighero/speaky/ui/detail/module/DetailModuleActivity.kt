package com.bighero.speaky.ui.detail.module

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val bundle = intent.extras
        if (bundle != null) {
            val moduleId = bundle.getString(EXTRA_TITLE)
            if (moduleId != null) {
                populateFragment(moduleId)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }




    private fun populateFragment(moduleId: String) {

        var fragment = supportFragmentManager.findFragmentByTag(ListBabFragment.TAG)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment == null) {
            fragment = ListBabFragment.newInstance(moduleId)
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