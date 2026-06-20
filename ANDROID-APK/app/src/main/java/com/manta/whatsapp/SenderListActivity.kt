package com.manta.whatsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.manta.whatsapp.databinding.ActivitySenderListBinding
import com.manta.whatsapp.models.WhatsAppSender
import com.manta.whatsapp.services.ApiClient
import com.manta.whatsapp.ui.adapters.SenderAdapter
import kotlinx.coroutines.launch

class SenderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySenderListBinding
    private lateinit var adapter: SenderAdapter
    private val senders = mutableListOf<WhatsAppSender>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySenderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        loadSenders()
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, WhatsAppConnectActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        adapter = SenderAdapter(senders, onEdit = { /* edit */ }, onDelete = { deleteSender(it) })
        binding.rvSenders.layoutManager = LinearLayoutManager(this)
        binding.rvSenders.adapter = adapter
    }

    private fun loadSenders() {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val response = ApiClient.apiService.getAccounts()
                if (response.isSuccessful) {
                    senders.clear()
                    senders.addAll(response.body()?.accounts ?: emptyList())
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SenderListActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun deleteSender(sender: WhatsAppSender) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.deleteAccount(sender.id)
                if (response.isSuccessful) {
                    senders.remove(sender)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@SenderListActivity, "Sender dihapus", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SenderListActivity, "Gagal hapus: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}