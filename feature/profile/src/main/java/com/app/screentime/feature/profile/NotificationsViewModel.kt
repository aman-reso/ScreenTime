package com.app.screentime.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.screentime.feature.profile.data.local.NotificationEntity
import com.app.screentime.feature.profile.domain.usecase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val selectedTab: String = "All",
    val tabs: List<String> = listOf("All", "Matches", "Messages", "Activity")
)

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getNotificationsUseCase: GetNotificationsUseCase
) : ViewModel() {

    private val _selectedTab = MutableStateFlow("All")

    val uiState: StateFlow<NotificationsUiState> = combine(
        getNotificationsUseCase(),
        _selectedTab
    ) { entities, currentTab ->
        val filtered = if (currentTab == "All") {
            entities
        } else {
            entities.filter { it.category.equals(currentTab, ignoreCase = true) }
        }

        val items = filtered.map { entity ->
            NotificationItem(
                id = entity.id,
                title = entity.title,
                timeAgo = formatTimeAgo(entity.timestamp),
                message = entity.message,
                category = entity.category,
                isUnread = entity.isUnread,
                actionText = entity.actionText,
                avatarUrl = entity.avatarUrl,
                iconResName = entity.iconResName,
                targetScreen = entity.targetScreen,
                targetId = entity.targetId
            )
        }

        NotificationsUiState(
            notifications = items,
            selectedTab = currentTab
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationsUiState()
    )

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun markAsRead(id: String) {
        viewModelScope.launch {
            getNotificationsUseCase.markAsRead(id)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            getNotificationsUseCase.clearAll()
        }
    }

    private fun formatTimeAgo(timestamp: Long): String {
        val diffMs = System.currentTimeMillis() - timestamp
        if (diffMs <= 0) return "Just now"
        val seconds = diffMs / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days == 1L -> "Yesterday"
            else -> "${days}d ago"
        }
    }
}
