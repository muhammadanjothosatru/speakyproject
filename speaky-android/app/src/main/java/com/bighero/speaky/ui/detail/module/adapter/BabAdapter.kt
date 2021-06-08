package com.bighero.speaky.ui.detail.module.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.databinding.ModuleListBinding

class BabAdapter:RecyclerView.Adapter<BabAdapter.ViewHolder>() {
    private val listModule = ArrayList<ModuleEntity.Bab>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setModule(bab: List<ModuleEntity.Bab>){
        this.listModule.clear()
        this.listModule.addAll(bab)
    }

    inner class ViewHolder(private val binding: ModuleListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bab: ModuleEntity.Bab) {
            with(binding) {
                titleModule.text = bab.judul
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(bab.judul) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ModuleListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listModule[position])
    }

    override fun getItemCount(): Int {
        return listModule.size

    }

    interface OnItemClickCallback {
        fun onItemClicked(bab: String)
    }
}