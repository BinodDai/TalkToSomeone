import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.binod.talktosomeone.data.local.preferences.LocalStorage
import com.binod.talktosomeone.data.local.preferences.PrefKeys
import com.binod.talktosomeone.presentation.navigation.Screen
import com.binod.talktosomeone.presentation.ui.screens.advice.AdviceScreen
import com.binod.talktosomeone.presentation.ui.screens.chat.ChatScreen
import com.binod.talktosomeone.presentation.ui.screens.home.HomeScreen
import com.binod.talktosomeone.presentation.ui.screens.messages.MessagesScreen
import com.binod.talktosomeone.presentation.ui.screens.profile.ProfileFormScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val defaultStorage = remember {
        LocalStorage(
            context = context,
            name = LocalStorage.DEFAULT_PREF_NAME,
            secure = false
        )
    }

    val isProfileSetupCompleted =
        defaultStorage[PrefKeys.IS_PROFILE_SETUP_COMPLETED, Boolean::class.java]

    val startDestination = if (isProfileSetupCompleted) {
        Screen.Home.route
    } else {
        Screen.Profile.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Profile.route) { ProfileFormScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Messages.route) { MessagesScreen(navController) }
//        composable(Screen.Vent.route) { VentScreen() }
        composable(Screen.Advice.route) { AdviceScreen(navController = navController) }
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val partnerId = backStackEntry.arguments?.getString("userId") ?: ""
            ChatScreen(
                navController = navController,
                chatPartnerId = partnerId
            )
        }
//        composable(Screen.ChooseTopic.route) { TopicScreen() }
    }
}
