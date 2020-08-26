package com.bblackbelt.data

import com.bblackbelt.data.model.StockDto
import com.bblackbelt.data.model.StockSubscribe
import com.google.gson.Gson
import io.fabric8.mockwebserver.DefaultMockServer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class StockDataLoaderTest {

    @Test
    fun `test data emitted`() {

        val jsonConverterWrapper = JsonConverterWrapper.Impl(Gson())
        val s = jsonConverterWrapper.toJson(StockSubscribe("abc"))
        val server =  DefaultMockServer()

        val stocksDto = (0..4).map {StockDto("abc", Random.nextFloat()) }

        val events = stocksDto.map {
            jsonConverterWrapper.toJson(it)
        }

        server.expect().get().withPath("/lol")
            .andUpgradeToWebSocket()
            .open()
            .expect(s).andEmit(events[0]).once()
            .expectSentWebSocketMessage(events[0]).andEmit(events[1]).once()
            .expectSentWebSocketMessage(events[1]).andEmit(events[2]).once()
            .expectSentWebSocketMessage(events[2]).andEmit(events[3]).once()
            .expectSentWebSocketMessage(events[3]).andEmit(events[4]).once()
            .done()
            .once()

        server.start()

        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(server.url("/lol")).build()
        val dataLoader = StocksRepository.Impl(okHttpClient, request, jsonConverterWrapper)

        dataLoader.loadStocks(listOf("abc")).test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertValues(*stocksDto.toTypedArray())
            .assertNotComplete()

        server.shutdown()
    }
}
