package com.app.screentime.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.telekom.odsystem.ODSystem
import com.telekom.odsystem.ODSThemeType
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val theme: StateFlow<String> = themeRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "System"
        )

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