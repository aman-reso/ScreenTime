package com.app.screentime.customisation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.customisation.model.ColorOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomisationUiState(
    val serviceName: String = "My Mobile",
    val selectedColorOption: ColorOption = ColorOption.DEFAULT_PALETTE[0], // Default color
    val availableColors: List<ColorOption> = ColorOption.DEFAULT_PALETTE
)

@HiltViewModel
class CustomisationViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomisationUiState())
    val uiState: StateFlow<CustomisationUiState> = _uiState.asStateFlow()

    fun loadCustomisationData() {
        viewModelScope.launch {
            // Load saved service name
            val savedName = preferencesManager.getCustomServiceName() ?: "My Mobile"
            val savedColorId = preferencesManager.getCustomServiceColorId()
            val selectedColor = savedColorId?.let { id ->
                ColorOption.DEFAULT_PALETTE.find { it.id == id }
            } ?: ColorOption.DEFAULT_PALETTE[0]

            _uiState.value = _uiState.value.copy(
                serviceName = savedName,
                selectedColorOption = selectedColor
            )
        }
    }

    fun updateServiceName(newName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(serviceName = newName)
            preferencesManager.setCustomServiceName(newName)
        }
    }

    fun updateSelectedColor(colorOption: ColorOption) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(selectedColorOption = colorOption)

            // Store color ID for persistence
            preferencesManager.setCustomServiceColorId(colorOption.id)
        }
    }
}

// Extension functions for PreferencesManager
private const val KEY_CUSTOM_SERVICE_NAME = "custom_service_name"
private const val KEY_CUSTOM_SERVICE_COLOR_ID = "custom_service_color_id"

private fun PreferencesManager.getCustomServiceName(): String? {
    return getString(KEY_CUSTOM_SERVICE_NAME, null)
}

private fun PreferencesManager.setCustomServiceName(name: String) {
    putString(KEY_CUSTOM_SERVICE_NAME, name)
}

private fun PreferencesManager.getCustomServiceColorId(): String? {
    return getString(KEY_CUSTOM_SERVICE_COLOR_ID, null)
}

private fun PreferencesManager.setCustomServiceColorId(colorId: String) {
    putString(KEY_CUSTOM_SERVICE_COLOR_ID, colorId)
}
