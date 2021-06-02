package com.bighero.speaky.ui.assesment

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.bighero.speaky.R
import com.bighero.speaky.databinding.ActivityAssesmentBinding
import com.bighero.speaky.ui.assesment.fragment.PreviewFragment

class AssesmentActivity : AppCompatActivity() {
    val drawerToogle by lazy {
        ActionBarDrawerToggle(this, binding.draweLayout, binding.toolbar, R.string.drawer_open, R.string.drawer_close)
    }
    private lateinit var binding : ActivityAssesmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssesmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tes"

        binding.navigationView.setNavigationItemSelectedListener {
            selectDrawerItem(it)
            true
        }
        binding.draweLayout.addDrawerListener(drawerToogle)

        val fragment = PreviewFragment.newInstance()
        addFragment(fragment)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToogle.onConfigurationChanged(newConfig)
    }

    private fun selectDrawerItem(item: MenuItem) {
        var fragment: Fragment? = null

        binding.draweLayout.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToogle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fragment_menu, menu)
        return false
    }

    private fun addFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        this.finish()
    }
}