package com.manta.whatsapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.manta.whatsapp.DashboardActivity
import com.manta.whatsapp.R
import com.neovisionaries.ws.client.*

class WhatsAppService : Service() {
    private var websocket: WebSocket? = null
    private val CHANNEL_ID = "manta_x_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Manta'X WhatsApp")
            .setContentText("Service aktif - monitoring WhatsApp")
            .setSmallIcon(R.drawable.ic_whatsapp)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, DashboardActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            .build()
        startForeground(1, notification)
        connectWebSocket()
        return START_STICKY
    }

    private fun connectWebSocket() {
        try {
            val factory = WebSocketFactory()
            websocket = factory.createSocket("wss://your-server.com/sync/user/token")
            websocket?.addListener(object : WebSocketAdapter() {
                override fun onTextMessage(ws: WebSocket, text: String) {}
                override fun onDisconnected(websocket: WebSocket?, serverCloseFrame: WebSocketFrame?, clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
                    Thread.sleep(5000)
                    connectWebSocket()
                }
            })
            websocket?.connectAsynchronously()
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Manta'X Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        websocket?.disconnect()
        super.onDestroy()
    }
}