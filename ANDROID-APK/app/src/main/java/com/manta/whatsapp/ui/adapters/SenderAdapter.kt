package com.manta.whatsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manta.whatsapp.databinding.ItemSenderBinding
import com.manta.whatsapp.models.WhatsAppSender

class SenderAdapter(
    private val senders: List<WhatsAppSender>,
    private val onEdit: (WhatsAppSender) -> Unit,
    private val onDelete: (WhatsAppSender) -> Unit
) : RecyclerView.Adapter<SenderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSenderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sender = senders[position]
        holder.binding.apply {
            tvNumber.text = sender.number
            tvStatus.text = sender.status.uppercase()
            tvLastActive.text = sender.lastActive
            val statusRes = when (sender.status) {
                "connected", "online" -> com.manta.whatsapp.R.drawable.status_online
                "offline" -> com.manta.whatsapp.R.drawable.status_offline
                else -> com.manta.whatsapp.R.drawable.status_error
            }
            statusIndicator.setBackgroundResource(statusRes)
            btnEdit.setOnClickListener { onEdit(sender) }
            btnDelete.setOnClickListener { onDelete(sender) }
        }
    }

    override fun getItemCount() = senders.size
}