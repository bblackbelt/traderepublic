package com.bblackbelt.exchanger

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bblackbelt.domain.StockUseCase
import com.bblackbelt.exchanger.adapter.StocksAdapter
import com.bblackbelt.exchanger.viewmodel.StocksViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_stocks.*

@AndroidEntryPoint
class StocksActivity : AppCompatActivity() {

    private val viewModel by viewModels<StocksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stocks)

        setSupportActionBar(toolbar)

        val adapter = StocksAdapter()
        stocksRV.adapter = adapter
        val margin = resources.getDimension(R.dimen.margin_16).toInt()
        stocksRV.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(margin, margin, margin, margin)
            }
        })
        stocksRV.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewModel.input.subscribeStockChanges()
        viewModel.output.onStockLoaded().observe(this, Observer {
            adapter.update(it)
        })
        viewModel.output.onError().observe(this, Observer {
            Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) {
                    viewModel.input.reload()
                }.show()
        })
    }
}
