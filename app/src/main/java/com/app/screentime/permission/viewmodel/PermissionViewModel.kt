package com.app.screentime.permission.viewmodel

import androidx.lifecycle.ViewModel
import com.app.screentime.analytics.AnalyticsUseCase
import com.app.screentime.login.usecase.LoginUseCase
import com.app.screentime.preferences.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val analyticsUseCase: AnalyticsUseCase,
    private val preferencesUseCase: PreferencesUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    init {
        analyticsUseCase.trackUsagePermissionScreen()
    }

    /**
     * Check if userId exists
     */
    fun needsRegistration(): Boolean {
        return preferencesUseCase.getUserId() == null
    }

    fun trackLanguageChangeClick() {
        analyticsUseCase.trackLanguageChangeClick()
    }
}

