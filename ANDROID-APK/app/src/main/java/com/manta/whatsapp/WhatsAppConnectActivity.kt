package com.manta.whatsapp

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.manta.whatsapp.databinding.ActivityWhatsappConnectBinding
import com.manta.whatsapp.services.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WhatsAppConnectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhatsappConnectBinding
    private var pairingCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsappConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        generateQR()
        binding.btnPair.setOnClickListener { pairWithCode() }
        binding.btnRefreshQR.setOnClickListener { generateQR() }
    }

    private fun generateQR() {
        lifecycleScope.launch {
            try {
                binding.pbQRLoading.visibility = View.VISIBLE
                val response = ApiClient.apiService.requestPairing()
                if (response.isSuccessful) {
                    val data = response.body()
                    pairingCode = data?.pairingCode ?: ""
                    val qrBitmap = BarcodeEncoder().encodeBitmap(pairingCode, BarcodeFormat.QR_CODE, 250, 250)
                    binding.ivQRCode.setImageBitmap(qrBitmap)
                    pollPairingStatus()
                }
            } catch (e: Exception) {
                Toast.makeText(this@WhatsAppConnectActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.pbQRLoading.visibility = View.GONE
            }
        }
    }

    private fun pairWithCode() {
        val code = binding.etPairingCode.text.toString().trim()
        if (code.isEmpty()) {
            Toast.makeText(this, "Masukin kode pairing dulu!", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun pollPairingStatus() {
        lifecycleScope.launch {
            repeat(30) {
                delay(5000)
                try {
                    val response = ApiClient.apiService.checkPairingStatus(pairingCode)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Toast.makeText(this@WhatsAppConnectActivity, "Pairing BERHASIL!", Toast.LENGTH_LONG).show()
                        return@launch
                    }
                } catch (_: Exception) {}
            }
        }
    }
}