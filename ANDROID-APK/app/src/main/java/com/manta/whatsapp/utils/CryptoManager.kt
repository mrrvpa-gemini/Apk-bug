package com.manta.whatsapp.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CryptoManager {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    private fun getKey(): SecretKey {
        val existing = keyStore.getEntry("manta_x_key", null) as? KeyStore.SecretKeyEntry
        return existing?.secretKey ?: generateKey()
    }

    private fun generateKey(): SecretKey {
        return KeyGenerator.getInstance("AES", "AndroidKeyStore").apply {
            init(KeyGenParameterSpec.Builder("manta_x_key",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true).build())
        }.generateKey()
    }

    fun encrypt(data: String): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        return cipher.iv + cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    }

    fun decrypt(encrypted: ByteArray): String {
        val iv = encrypted.copyOfRange(0, 12)
        val cipherText = encrypted.copyOfRange(12, encrypted.size)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(cipherText), Charsets.UTF_8)
    }
}