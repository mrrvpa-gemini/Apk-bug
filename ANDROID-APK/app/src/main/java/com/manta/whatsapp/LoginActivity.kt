package com.manta.whatsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.manta.whatsapp.databinding.ActivityLoginBinding
import com.manta.whatsapp.models.LoginRequest
import com.manta.whatsapp.services.ApiClient
import com.manta.whatsapp.utils.StorageManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            return
        }
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    StorageManager.saveToken(data.token)
                    StorageManager.saveUser(data.user)
                    Toast.makeText(this@LoginActivity, "Login GACOR!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal bro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
    }
}