package com.binod.talktosomeone.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.binod.talktosomeone.domain.model.ConversationStarterType
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.tasks.await
import java.util.Calendar

@Composable
fun isLightTheme(): Boolean = !isSystemInDarkTheme()

fun getGreetingWithEmoji(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Good Morning ☀️"
        in 12..16 -> "Good Afternoon 🌤️"
        in 17..20 -> "Good Evening 🌆"
        in 21..23, in 0..4 -> "Good Night 🌙"
        else -> "Hello"
    }
}

fun getConversationStarterType(title: String): ConversationStarterType {
    return when (title) {
        Constants.QUICK_MATCH -> ConversationStarterType.QUICK_MATCH
        Constants.VENT -> ConversationStarterType.VENT
        Constants.ADVICE -> ConversationStarterType.ADVICE
        Constants.TOPIC -> ConversationStarterType.TOPIC
        else -> ConversationStarterType.QUICK_MATCH
    }
}

suspend fun getFirebaseInstallationId(): String {
    return FirebaseInstallations.getInstance().id.await()
}

fun chatIdFor(user1: String, user2: String): String =
    if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"


@SuppressLint("ServiceCast")
fun copyTextToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Message", text)
    clipboard.setPrimaryClip(clip)
}
