package com.example.web_scoket


import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

object WebSocketManager  {

    private var webSocket: WebSocket? = null

    fun connectWebSocket(url: String, listener: WebSocketListener) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS) // Infinite timeout for WebSocket
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
        println("Send Message : "+message)
    }

    fun disconnectWebSocket() {
        webSocket?.close(1000, "User initiated disconnect")
    }

}
