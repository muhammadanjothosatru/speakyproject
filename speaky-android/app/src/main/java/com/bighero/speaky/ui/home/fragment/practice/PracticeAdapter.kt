package com.bighero.speaky.ui.home.fragment.practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.R
import com.bighero.speaky.data.entity.PraticeEntity
import com.bighero.speaky.databinding.GridListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PracticeAdapter : RecyclerView.Adapter<PracticeAdapter .ViewHolder>() {
    private val listPractice = ArrayList<PraticeEntity>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setPratice(pratice: List<PraticeEntity>){
        this.listPractice.clear()
        this.listPractice.addAll(pratice)
    }

    inner class ViewHolder(private val binding: GridListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(module: PraticeEntity) {
            with(binding) {
                judul.text = module.judul
                Glide.with(itemView.context)
                    .load(module.cover)
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
        holder.bind(listPractice[position])
    }

    override fun getItemCount(): Int {
        return listPractice.size

    }

    interface OnItemClickCallback {
        fun onItemClicked(judul: String)
    }
}