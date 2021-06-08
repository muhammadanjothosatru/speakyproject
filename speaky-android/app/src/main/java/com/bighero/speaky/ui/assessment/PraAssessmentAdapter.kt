package com.bighero.speaky.ui.assessment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.assesment.AssessmentPackEntity
import com.bighero.speaky.databinding.PraListBinding

class PraAssessmentAdapter
    (private val listTest: ArrayList<AssessmentPackEntity>):
    RecyclerView.Adapter<PraAssessmentAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AssessmentPackEntity)
    }

    inner class ListViewHolder(private val binding: PraListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pack: AssessmentPackEntity) {
            with(binding){
                titlePack.text = pack.title

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(pack) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = PraListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listTest[position])
    }

    override fun getItemCount(): Int = listTest.size
}