package com.binod.talktosomeone.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binod.talktosomeone.presentation.ui.components.common.SnackbarState
import com.binod.talktosomeone.presentation.ui.components.common.SnackbarType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _snackbarState = MutableStateFlow(SnackbarState())
    val snackbarState = _snackbarState.asStateFlow()

    // Original method for backward compatibility
    fun showSnackbar(message: String) {
        _snackbarState.value = SnackbarState(
            isVisible = true,
            message = message,
            type = SnackbarType.SUCCESS
        )
    }

    // Enhanced method with type support
    fun showSnackbarWithType(message: String, type: SnackbarType = SnackbarType.SUCCESS) {
        _snackbarState.value = SnackbarState(
            isVisible = true,
            message = message,
            type = type
        )
    }

    fun showSuccessSnackbar(message: String) {
        showSnackbarWithType(message, SnackbarType.SUCCESS)
    }

    fun showErrorSnackbar(message: String) {
        showSnackbarWithType(message, SnackbarType.ERROR)
    }

    fun showWarningSnackbar(message: String) {
        showSnackbarWithType(message, SnackbarType.WARNING)
    }

    fun dismissSnackbar() {
        _snackbarState.value = _snackbarState.value.copy(isVisible = false)
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent

    protected fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    protected fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        data class Navigate(val route: String) : UiEvent()
        data class NavigateWithToast(val route: String, val message: String) : UiEvent()
        data class SilentSuccess(val data: Any? = null) : UiEvent()
        object DismissDialog : UiEvent()
    }
}
