package com.bighero.speaky.ui.home.fragment.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.databinding.GridListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.collections.ArrayList

class ModuleAdapter: RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {
    private val listModule = ArrayList<ModuleEntity>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setModule(module: List<ModuleEntity>){
        this.listModule.clear()
        this.listModule.addAll(module)
    }

    inner class ViewHolder(private val binding: GridListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(module: ModuleEntity) {
            with(binding) {
                judul.text = module.judul
                Glide.with(itemView.context)
                    .load(module.gambar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imgModule)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(module.key) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemGridtMainBinding = GridListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemGridtMainBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listModule[position])
    }

    override fun getItemCount(): Int {
        return listModule.size

    }

    interface OnItemClickCallback {
        fun onItemClicked(judul: String)
    }
}