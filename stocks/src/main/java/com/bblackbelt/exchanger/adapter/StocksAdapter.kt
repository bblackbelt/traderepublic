package com.bblackbelt.exchanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bblackbelt.exchanger.R
import com.bblackbelt.exchanger.model.StockView

class StocksAdapter: RecyclerView.Adapter<StockViewHolder>() {

    private val dataSet = AsyncListDiffer<StockView>(this, comparator)

    fun update(items: List<StockView>) {
        dataSet.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.stock_row, parent, false)
        return StockViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.currentList.size

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bindView(dataSet.currentList[position])
    }

    companion object {
        private val comparator: DiffUtil.ItemCallback<StockView> =
            object : DiffUtil.ItemCallback<StockView>() {
                override fun areItemsTheSame(oldItem: StockView, newItem: StockView): Boolean =
                    oldItem.isin == newItem.isin
                override fun areContentsTheSame(oldItem: StockView, newItem: StockView): Boolean =
                    oldItem == newItem
            }
    }
}
