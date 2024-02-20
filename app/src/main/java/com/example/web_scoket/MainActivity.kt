package com.example.web_scoket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSendMessage = findViewById<Button>(R.id.btnSendMessage)
        val btnDisconnect = findViewById<Button>(R.id.btnDisconnect)
        val etMessage = findViewById<EditText>(R.id.etMessage)

        val webSocketUrl = "wss://ws.postman-echo.com/raw"
        btnSendMessage.setOnClickListener {
            WebSocketManager.sendMessage(etMessage.text.toString())
        }
        btnDisconnect.setOnClickListener {
            WebSocketManager.disconnectWebSocket()
        }

        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                // WebSocket connection opened
                println("WebSocket connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                // Received a message
//                Toast.makeText(this@MainActivity, text,Toast.LENGTH_SHORT).show()
                println("Received message: $text")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                // WebSocket connection closed
                println("WebSocket connection closed. Code: $code, Reason: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                // WebSocket connection failure
                println("WebSocket connection failure: ${t.message}")
            }

        }
        WebSocketManager.connectWebSocket(webSocketUrl, webSocketListener)

    }
}