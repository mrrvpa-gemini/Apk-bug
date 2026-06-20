package com.manta.whatsapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.manta.whatsapp.models.User

object StorageManager {
    private const val PREFS_NAME = "manta_x_secure"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        prefs = EncryptedSharedPreferences.create(
            context, PREFS_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) = prefs.edit().putString("auth_token", token).apply()
    fun getToken(): String? = prefs.getString("auth_token", null)
    fun saveUser(user: User) = prefs.edit().putString("user", gson.toJson(user)).apply()
    fun getUser(): User? = prefs.getString("user", null)?.let { gson.fromJson(it, User::class.java) }
    fun clearAll() = prefs.edit().clear().apply()
}