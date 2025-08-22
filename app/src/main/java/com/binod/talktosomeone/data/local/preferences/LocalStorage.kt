package com.binod.talktosomeone.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.content.edit
import com.binod.talktosomeone.BuildConfig
import com.google.gson.Gson
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

@Suppress("UNCHECKED_CAST")
class LocalStorage(
    context: Context,
    name: String,
    private val secure: Boolean
) {

    private val gson = Gson()
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private val keyAlias = "LocalStorageKey"

    init {
        if (secure) {
            initKeyStore()
        }
    }

    /** Creates AES key in Android Keystore if it doesn't exist */
    private fun initKeyStore() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (!keyStore.containsAlias(keyAlias)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                keyAlias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256) // AES-256
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    /** Encrypt string using AES/GCM */
    private fun encrypt(input: String): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(input.toByteArray(Charset.forName("UTF-8")))
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    /** Decrypt string using AES/GCM */
    private fun decrypt(input: String): String {
        val raw = Base64.decode(input, Base64.DEFAULT)
        val iv = raw.copyOfRange(0, 12) // GCM IV length
        val encrypted = raw.copyOfRange(12, raw.size)
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKey = keyStore.getKey(keyAlias, null) as SecretKey
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, Charset.forName("UTF-8"))
    }

    fun contains(key: String): Boolean = sharedPref.contains(key)

    fun <T> set(key: String, data: T?) {
        sharedPref.edit {
            if (data == null || (data is String && data.isEmpty())) {
                remove(key)
            } else {
                val valueToStore = when (data) {
                    is String -> data
                    is Boolean, is Float, is Int, is Long, is Set<*> -> gson.toJson(data)
                    else -> gson.toJson(data)
                }
                val finalValue = if (secure) encrypt(valueToStore) else valueToStore
                putString(key, finalValue)
            }
        }
    }

    operator fun <T> get(key: String, anonymousClass: Class<T>): T {
        val storedValue = sharedPref.getString(key, null) ?: return when (anonymousClass) {
            String::class.java -> "" as T
            Boolean::class.java -> false as T
            Float::class.java -> 0f as T
            Int::class.java -> 0 as T
            Long::class.java -> 0L as T
            else -> gson.fromJson("", anonymousClass)
        }

        val decryptedValue = if (secure) decrypt(storedValue) else storedValue

        return when (anonymousClass) {
            String::class.java -> decryptedValue as T
            Boolean::class.java -> gson.fromJson(decryptedValue, Boolean::class.java) as T
            Float::class.java -> gson.fromJson(decryptedValue, Float::class.java) as T
            Int::class.java -> gson.fromJson(decryptedValue, Int::class.java) as T
            Long::class.java -> gson.fromJson(decryptedValue, Long::class.java) as T
            else -> gson.fromJson(decryptedValue, anonymousClass)
        }
    }

    fun delete(key: String) = sharedPref.edit { remove(key) }

    fun deleteAll() = sharedPref.edit { clear() }

    companion object {
        const val DEFAULT_PREF_NAME = "share_prefs" + BuildConfig.LIBRARY_PACKAGE_NAME
        const val DEFAULT_SECURED_PREF_NAME =
            "secured_share_prefs" + BuildConfig.LIBRARY_PACKAGE_NAME
    }
}