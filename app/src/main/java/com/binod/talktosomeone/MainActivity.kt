package com.binod.talktosomeone

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import com.binod.talktosomeone.data.remote.api.FirestoreService
import com.binod.talktosomeone.di.DefaultStorage
import com.binod.talktosomeone.di.SecureStorage
import com.binod.talktosomeone.presentation.manager.UserPresenceManager
import com.binod.talktosomeone.presentation.ui.theme.TalkToSomeoneTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var userPresenceManager: UserPresenceManager
    @Inject lateinit var auth: FirebaseAuth

    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            userPresenceManager.setUserOnline()
        } else {
            // User signed out
            userPresenceManager.setUserOffline()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userPresenceManager.setUserOnline()
        setContent {
            TalkToSomeoneTheme {
                AppNavigation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        auth.currentUser?.let { userPresenceManager.setUserOnline() }
    }

    override fun onStop() {
        super.onStop()
        auth.currentUser?.let { userPresenceManager.setUserOffline() }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.removeAuthStateListener(authListener)
        auth.currentUser?.let { userPresenceManager.setUserOffline() }
        userPresenceManager.cleanup()
    }
}
