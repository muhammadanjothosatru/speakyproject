package com.bighero.speaky.ui.detail.module.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.module.ModuleEntity
import com.bighero.speaky.databinding.GalleryListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private val listBab = ArrayList<ModuleEntity.Bab>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setModule(bab: List<ModuleEntity.Bab>){
        this.listBab.clear()
        this.listBab.addAll(bab)
    }

    inner class ViewHolder(private val binding: GalleryListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bab: ModuleEntity.Bab) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(bab.video)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imgGalleryModule)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(bab.judul) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBab[position])
    }

    override fun getItemCount(): Int {
        return listBab.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(bab: String)
    }
}