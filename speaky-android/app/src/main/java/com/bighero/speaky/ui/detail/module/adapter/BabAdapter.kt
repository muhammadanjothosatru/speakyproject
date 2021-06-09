package com.bighero.speaky.ui.detail.module.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.databinding.ModuleListBinding

class BabAdapter(private val listener: BabAdapterClickListener):RecyclerView.Adapter<BabAdapter.ViewHolder>() {
    private val listBab = ArrayList<ModuleEntity.Bab>()

    fun setModule(bab: List<ModuleEntity.Bab>){
        this.listBab.clear()
        this.listBab.addAll(bab)
    }

    inner class ViewHolder(private val binding: ModuleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bab: ModuleEntity.Bab) {
            with(binding) {
                titleModule.text = bab.judul
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ModuleListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bab = listBab[position]
        holder.bind(bab)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(holder.adapterPosition, listBab[holder.adapterPosition].judul)
        }

    }

    override fun getItemCount(): Int {
        return listBab.size

    }

    interface BabAdapterClickListener {
        fun onItemClicked(position: Int, babId: String)
    }
}