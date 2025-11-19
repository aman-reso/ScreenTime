package com.app.screentime.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    val theme: StateFlow<String> = themeRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeType.CLASSIC_LIGHT.name
        )

    fun setTheme(themeType: ThemeType) {
        viewModelScope.launch {
            themeRepository.setTheme(themeType.name)
        }
    }

    fun setTheme(themeString: String) {
        viewModelScope.launch {
            themeRepository.setTheme(themeString)
        }
    }

    fun getAvailableThemes(): List<ThemeType> {
        return ThemeType.values().toList()
    }

    fun getLightThemes(): List<ThemeType> {
        return ThemeType.values().filter { !it.isDark }
    }

    fun getDarkThemes(): List<ThemeType> {
        return ThemeType.values().filter { it.isDark }
    }
}
