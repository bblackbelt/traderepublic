package com.bblackbelt.data

import com.bblackbelt.data.model.StockDto
import com.bblackbelt.data.model.StockSubscribe
import com.bblackbelt.data.model.StockUnsubscribe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.IOException
import javax.inject.Inject


interface StocksRepository {
    fun loadStocks(isins: List<String>): Observable<StockDto>
    class Impl @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val request: Request,
        private val converterWrapper: JsonConverterWrapper
    ) : StocksRepository {
        override fun loadStocks(isins: List<String>): Observable<StockDto> {
            return Connector(okHttpClient, request, isins.map { StockSubscribe(it) }, converterWrapper)
                .map { converterWrapper.fromJson(it, StockDto::class.java) }
        }
    }
}

private class Connector(
    private val okHttpClient: OkHttpClient,
    private val request: Request,
    private val isins: List<StockSubscribe>,
    private val converterWrapper: JsonConverterWrapper
) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>) {
        val wD = WebSocketDisposable(observer, isins, converterWrapper)
        observer.onSubscribe(wD)
        if (!wD.isDisposed) {
            okHttpClient.newWebSocket(request, wD)
        }
    }

    private class WebSocketDisposable(
        private val upstream: Observer<in String>,
        private val isins: List<StockSubscribe>,
        private val converterWrapper: JsonConverterWrapper
    ) : WebSocketListener(), Disposable {

        private var webSocket: WebSocket? = null

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            this.webSocket = webSocket
            isins.forEach {
                webSocket.send(converterWrapper.toJson(it))
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            upstream.onNext(text)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            upstream.onError(
                if (response != null) {
                    IOException("${response.code} ${response.message}")
                } else {
                    t
                }
            )
            upstream.onComplete()
        }

        @Volatile
        private var isDisposed = false
        override fun isDisposed(): Boolean = isDisposed

        override fun dispose() {
            isDisposed = true
            isins.forEach {
                webSocket?.send(converterWrapper.toJson(StockUnsubscribe(it.subscribe)))
            }
            webSocket?.close(1000, "Goodbye !");
            webSocket = null
        }
    }
}
