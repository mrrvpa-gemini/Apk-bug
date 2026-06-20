package com.manta.whatsapp.models

data class WhatsAppSender(
    val id: String,
    val number: String,
    val status: String,
    val lastActive: String,
    val name: String? = null
)

data class AccountsResponse(val accounts: List<WhatsAppSender>)

data class PairingResponse(val pairingCode: String, val expiresIn: Int, val qrData: String? = null)

data class PairingStatusResponse(val success: Boolean, val account: WhatsAppSender? = null)