package com.manta.whatsapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class SyncService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        startSyncLoop()
    }

    private fun startSyncLoop() {
        scope.launch {
            while (isActive) {
                syncData()
                delay(30000)
            }
        }
    }

    private suspend fun syncData() {
        try {
            // Sync logic
        } catch (e: Exception) { e.printStackTrace() }
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}