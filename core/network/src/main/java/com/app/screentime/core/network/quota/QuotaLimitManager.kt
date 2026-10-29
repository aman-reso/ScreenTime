package com.app.screentime.core.network.quota

import com.app.screentime.core.model.QuotaLimitInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotaLimitManager @Inject constructor() {

    private val _chatQuota = MutableStateFlow<QuotaLimitInfo?>(null)
    val chatQuota: StateFlow<QuotaLimitInfo?> = _chatQuota.asStateFlow()

    private val _feedQuota = MutableStateFlow<QuotaLimitInfo?>(null)
    val feedQuota: StateFlow<QuotaLimitInfo?> = _feedQuota.asStateFlow()
    val homeQuota: StateFlow<QuotaLimitInfo?> get() = feedQuota

    fun updateChatQuota(quotaInfo: QuotaLimitInfo) {
        _chatQuota.value = quotaInfo
    }

    fun updateFeedQuota(quotaInfo: QuotaLimitInfo) {
        _feedQuota.value = quotaInfo
    }

    fun updateHomeQuota(quotaInfo: QuotaLimitInfo) {
        updateFeedQuota(quotaInfo)
    }

    fun clearChatQuota() {
        _chatQuota.value = null
    }

    fun clearFeedQuota() {
        _feedQuota.value = null
    }

    fun clearHomeQuota() {
        clearFeedQuota()
    }
}
