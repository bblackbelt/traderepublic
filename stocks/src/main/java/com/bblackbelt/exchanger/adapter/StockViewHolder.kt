package com.bblackbelt.exchanger.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bblackbelt.exchanger.R
import com.bblackbelt.exchanger.model.StockView

class StockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name = itemView.findViewById<TextView>(R.id.name)
    private val isin = itemView.findViewById<TextView>(R.id.isin)
    private val price = itemView.findViewById<TextView>(R.id.price)

    fun bindView(r: StockView) {
        name.text = r.name
        isin.text = r.isin
        price.text = "%.2f".format(r.price)
    }
}
