package com.bighero.speaky.ui.assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.AssessmentPackEntity
import com.bighero.speaky.databinding.ActivityPraAssessmentBinding
import com.bighero.speaky.databinding.ContentDetailPraBinding

class PraAssessmentActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPraAssessmentBinding
    private lateinit var detailBinding: ContentDetailPraBinding
    private var list = ArrayList<AssessmentPackEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPraAssessmentBinding.inflate(layoutInflater)
        detailBinding = binding.detailContent
        setContentView(binding.root)

        detailBinding.rvTes.setHasFixedSize(true)

        list.addAll(getListPack())
        showRecyclerList()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btSelect.setOnClickListener {
            startActivity(Intent(this, AssessmentActivity::class.java))
            finish()
        }
    }

    private fun getListPack(): ArrayList<AssessmentPackEntity> {
        val dataName = resources.getStringArray(R.array.data_tes)
        val dataGuide = resources.getStringArray(R.array.data_detail_tes)

        val listPack = ArrayList<AssessmentPackEntity>()
        for (position in dataName.indices) {
            val pack = AssessmentPackEntity(
                dataName[position],
                dataGuide[position]
            )
            listPack.add(pack)
        }
        return listPack
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
        detailBinding.detailPetunjuk.text = data.guide
    }
}