package com.manta.whatsapp.models

data class Message(
    val id: String,
    val senderId: String,
    val recipient: String,
    val content: String,
    val status: String,
    val timestamp: Long,
    val type: String = "text"
)

data class SyncData(val messages: List<Message>, val lastSync: Long)