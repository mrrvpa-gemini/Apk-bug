package com.manta.whatsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.manta.whatsapp.utils.StorageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        lifecycleScope.launch {
            delay(2000)
            val token = StorageManager.getToken()
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            }
            finish()
        }
    }
}