package com.app.screentime.feature.call

import androidx.lifecycle.ViewModel
import com.app.screentime.core.network.api.ChattyApi
import com.app.screentime.core.network.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class LiveViewModel @Inject constructor(
    val api: ChattyApi,
    val sessionManager: SessionManager
) : ViewModel() {

    fun endStream(streamId: String) {
        val token = sessionManager.token ?: ""
        // Use Global IO CoroutineScope so disposal doesn't cancel the network call
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (streamId.isNotBlank()) {
                    api.endLiveStream(token, streamId)
                }
            } catch (_: Exception) {
            }
        }
    }
}

