package com.manta.whatsapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.manta.whatsapp.databinding.ActivityPairingCodeBinding
import com.manta.whatsapp.services.ApiClient
import kotlinx.coroutines.launch

class PairingCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPairingCodeBinding
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPairingCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        generatePairingCode()
        binding.btnCopy.setOnClickListener { copyToClipboard() }
    }

    private fun generatePairingCode() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.requestPairing()
                if (response.isSuccessful) {
                    val data = response.body()
                    val code = data?.pairingCode ?: "ERROR"
                    binding.tvPairingCode.text = code.chunked(4).joinToString("-")
                    startCountdown(data?.expiresIn ?: 300)
                }
            } catch (e: Exception) {
                Toast.makeText(this@PairingCodeActivity, "Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCountdown(seconds: Int) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val mins = millisUntilFinished / 60000
                val secs = (millisUntilFinished % 60000) / 1000
                binding.tvExpires.text = "Berakhir dalam ${mins}:${secs.toString().padStart(2, '0')}"
            }
            override fun onFinish() {
                binding.tvExpires.text = "KADALUARSA"
                generatePairingCode()
            }
        }.start()
    }

    private fun copyToClipboard() {
        val code = binding.tvPairingCode.text.toString().replace("-", "")
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Pairing Code", code))
        Toast.makeText(this, "Kode disalin!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer?.cancel()
    }
}