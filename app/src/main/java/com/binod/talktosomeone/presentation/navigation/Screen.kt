package com.binod.talktosomeone.presentation.navigation

import com.binod.talktosomeone.utils.Constants

sealed class Screen(val route: String) {
    object Home : Screen(Constants.HOME)
    object Vent : Screen(Constants.VENT)
    object Advice : Screen(Constants.ADVICE)
    object ChooseTopic : Screen(Constants.TOPIC)
    object Profile : Screen(Constants.PROFILE)


    object Chat : Screen("chat/{userId}") {
        fun withArgs(userId: String?): String {
            return "chat/${userId ?: ""}"
        }
    }
}
