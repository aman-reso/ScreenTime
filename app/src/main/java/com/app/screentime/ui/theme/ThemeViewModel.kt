package com.app.screentime.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.core.network.preferences.PreferencesManager
import com.app.screentime.customisation.model.ColorOption
import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.ODSThemeType
import com.telekom.odsystem.tokens.tokens.ODSTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val KEY_CUSTOM_SERVICE_COLOR_ID = "custom_service_color_id"

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val theme: StateFlow<String> = themeRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "System"
        )

    private val _headerScheme = MutableStateFlow(getStoredHeaderScheme())
    val headerScheme: StateFlow<ODSTheme> = _headerScheme.asStateFlow()

    init {
        // Load saved header scheme
        _headerScheme.value = getStoredHeaderScheme()
    }

    private fun getStoredHeaderScheme(): ODSTheme {
        val savedColorId = preferencesManager.getString(KEY_CUSTOM_SERVICE_COLOR_ID, null)
        return savedColorId?.let { id ->
            ColorOption.DEFAULT_PALETTE.find { it.id == id }?.scheme
        } ?: ColorOption.DEFAULT_PALETTE[0].scheme
    }

    fun refreshHeaderScheme() {
        _headerScheme.value = getStoredHeaderScheme()
    }

    fun setTheme(themeString: String) {
        viewModelScope.launch {
            themeRepository.setTheme(themeString)
            // Sync with ODSystem
            val odsThemeType = when (themeString.lowercase()) {
                "light" -> ODSThemeType.LIGHT
                "dark" -> ODSThemeType.DARK
                else -> ODSThemeType.SYSTEM
            }
            ODSystem.setTheme(context, odsThemeType)
        }
    }
}