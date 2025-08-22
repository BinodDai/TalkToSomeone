package com.binod.talktosomeone.presentation.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.binod.talktosomeone.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun HandleUiEvents(
    uiEvent: Flow<BaseViewModel.UiEvent>,
    navController: NavController,
    popUpToRoute: String? = null,
    onShowToast: (String) -> Unit = {},
    onDismissDialog: () -> Unit = {},
    onSilentSuccess: (Any?) -> Unit = {}
) {
    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is BaseViewModel.UiEvent.Navigate -> {
                    if (popUpToRoute != null) {
                        navController.navigate(event.route) {
                            popUpTo(popUpToRoute) { inclusive = true }
                        }
                    } else {
                        navController.navigate(event.route)
                    }
                }

                is BaseViewModel.UiEvent.NavigateWithToast -> {
                    onShowToast(event.message)
                    if (popUpToRoute != null) {
                        navController.navigate(event.route) {
                            popUpTo(popUpToRoute) { inclusive = true }
                        }
                    } else {
                        navController.navigate(event.route)
                    }
                }

                is BaseViewModel.UiEvent.ShowToast -> {
                    onShowToast(event.message)
                }

                is BaseViewModel.UiEvent.SilentSuccess -> {
                    onSilentSuccess(event.data)
                }

                BaseViewModel.UiEvent.DismissDialog -> {
                    onDismissDialog()
                }

            }
        }
    }
}