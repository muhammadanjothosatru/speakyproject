package com.bighero.speaky.ui.home.fragment.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bighero.speaky.data.entity.AssessmentEntity
import com.bighero.speaky.databinding.ItemListBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private val listHistory = ArrayList<AssessmentEntity>()
    fun setHistory(film: List<AssessmentEntity>){
        this.listHistory.clear()
        this.listHistory.addAll(film)
    }

    class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(historyList: AssessmentEntity) {
            with(binding) {
               tvItemDate.text = formatdate(historyList.timeStamp)
                tvItemTime.text = formattime(historyList.timeStamp)
                point.text = "Score " + historyList.score.toString()
                kategory.text = "Tes"
            }
        }

        private fun formattime(times: String): String {
            val outputFormat= SimpleDateFormat("HH:mm", Locale.US)
            val inputFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.US)
            val dateFormat = inputFormat.parse(times)
            val date: String = outputFormat.format(dateFormat)
            return date
        }

        private fun formatdate(times: String): String {
            val formater = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm", Locale.US)
            val date = LocalDate.parse(times,formater).toString()
            return date
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