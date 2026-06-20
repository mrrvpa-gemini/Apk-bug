package com.manta.whatsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.manta.whatsapp.databinding.ActivityDashboardBinding
import com.manta.whatsapp.utils.StorageManager

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val user = StorageManager.getUser()
        binding.tvUsername.text = user?.username ?: "User"
        binding.tvRole.text = (user?.role ?: "USER").uppercase()
        binding.btnLogout.setOnClickListener {
            StorageManager.clearAll()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_whatsapp -> {
                    supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, ui.fragments.WhatsAppFragment()).commit()
                    true
                }
                R.id.nav_pairing -> {
                    startActivity(Intent(this, PairingCodeActivity::class.java))
                    true
                }
                R.id.nav_logs -> {
                    supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, ui.fragments.LogsFragment()).commit()
                    true
                }
                R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.navHostFragment, ui.fragments.SettingsFragment()).commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNav.selectedItemId = R.id.nav_whatsapp
    }
}