package com.app.screentime.feature.call

import androidx.lifecycle.ViewModel
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiveViewModel @Inject constructor(
    val api: ChattyApi,
    val sessionManager: SessionManager
) : ViewModel()
