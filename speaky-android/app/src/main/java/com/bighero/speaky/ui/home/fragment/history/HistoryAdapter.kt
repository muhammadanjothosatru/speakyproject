package com.bighero.speaky.ui.home.fragment.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.AssessmentEntity
import com.bighero.speaky.data.entity.HistoryEntity
import com.bighero.speaky.databinding.ItemListBinding


class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val listHistory = ArrayList<AssessmentEntity>()
    fun setHistory(film: List<AssessmentEntity>){
        this.listHistory.clear()
        this.listHistory.addAll(film)
    }

    class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyList: AssessmentEntity) {
            with(binding) {
               tvItemDate.text = historyList.timeStamp
                tvItemTime.text = historyList.score.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemListMainBinding = ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemListMainBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filmlist = listHistory[position]
        holder.bind(filmlist)
    }

    override fun getItemCount(): Int {
        return listHistory.size

    }
}