package com.example.web_scoket

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "MyChannel"
    private val NOTIFICATION_ID = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSendMessage = findViewById<Button>(R.id.btnSendMessage)
        val btnDisconnect = findViewById<Button>(R.id.btnDisconnect)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        createNotificationChannel();

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
                showNotification(text)
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
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "My Channel"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(descripation : String) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(
            applicationContext
        )
        val intent = Intent(applicationContext, Notification_Activity::class.java
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        // Create a Notification
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )
            .setContentTitle("New Message")
            .setContentText(descripation)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.baseline_notifications_none_24)
            .setSound(soundUri)
        builder.setContentIntent(pendingIntent)

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}