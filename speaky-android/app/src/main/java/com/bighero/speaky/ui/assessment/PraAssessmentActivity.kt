package com.bighero.speaky.ui.assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import com.bighero.speaky.data.source.remote.response.assesment.APackResponse
import com.bighero.speaky.databinding.ActivityPraAssessmentBinding
import com.bighero.speaky.databinding.ContentPraBinding
import com.bighero.speaky.util.ViewModelFactory

class PraAssessmentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPraAssessmentBinding
    private lateinit var detailBinding: ContentPraBinding
    private lateinit var assesment :AssessmentViewModel
    private var list = ArrayList<AssessmentPackEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraAssessmentBinding.inflate(layoutInflater)
        detailBinding = binding.detailContent
        setContentView(binding.root)

        detailBinding.rvTes.setHasFixedSize(true)
        val factory = ViewModelFactory.getInstance(this)
        assesment = ViewModelProvider(this,factory)[AssessmentViewModel::class.java]
        getPack()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun getPack() {
        assesment.getAssesmentPack().observe(this, {
            printa(it)
        })
    }

    private fun printa(response: APackResponse) {
        response.pack?.let {
            if (it.isEmpty()) {
                Toast.makeText(this,"Kamu belum pernah Test", Toast.LENGTH_LONG).show()
            } else {
                list.addAll(it)
                showRecyclerList()
            }
        }
        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("exception", it)
            }
        }
    }

    private fun showRecyclerList() {
        detailBinding.rvTes.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = PraAssessmentAdapter(list)
        detailBinding.rvTes.adapter = adapter

        adapter.setOnClickCallback(object : PraAssessmentAdapter.OnItemClickCallback {
            override fun onItemClicked(data: AssessmentPackEntity) {
                showSelectedPack(data)
            }
        })

    }

    private fun showSelectedPack(data: AssessmentPackEntity) {
        detailBinding.detailPetunjuk.text = data.petunjuk
        binding.btSelect.isEnabled = true
        binding.btSelect.setOnClickListener {
            val intent = Intent(this, AssessmentActivity::class.java)
            intent.putExtra(AssessmentActivity.EXTRA_ID, data.id)
            startActivity(intent)
            finish()
        }
    }
}