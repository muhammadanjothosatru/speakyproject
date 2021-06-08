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
    fun setModule(film: List<ModuleEntity>){
        this.listModule.clear()
        this.listModule.addAll(film)
    }

    class ViewHolder(private val binding: GridListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(module: ModuleEntity) {
            with(binding) {
                judul.text = module.judul
                Glide.with(itemView.context)
                    .load(module.gambar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imgModule)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemGridtMainBinding = GridListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(itemGridtMainBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modulelist = listModule[position]
        holder.bind(modulelist)
    }

    override fun getItemCount(): Int {
        return listModule.size

    }
}