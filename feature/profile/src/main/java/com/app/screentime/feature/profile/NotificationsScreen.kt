package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

data class NotificationItem(
    val id: String,
    val title: String,
    val timeAgo: String,
    val message: String,
    val category: String, // "Matches", "Messages", "Activity"
    val isUnread: Boolean,
    val actionText: String? = null,
    val avatarUrl: String? = null,
    val iconResName: String? = null,
    val targetScreen: String? = null,
    val targetId: String? = null,
    val isSectionHeader: Boolean = false
)

/**
 * Notifications Screen (Figma node-id 83-4).
 * Dynamically displays FCM notifications stored in Room database via NotificationsViewModel.
 * Strictly adheres to Telekom ODS components, ZONA design standards, and GEMINI.md rules:
 * - 100% ODS components & ODSLazyColumn.
 * - Colors picked from `scheme: ODSTheme`.
 * - Max text size 16sp (bodyMBold, bodySBold, microcopyBold).
 * - Spacing and padding from ODSVariables.
 */
@Composable
fun NotificationsScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: NotificationsViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> }
) {
    val state by viewModel.uiState.collectAsState()

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            gap = ODSVariables.spacingComponent2
        ) {
            GenericHeader(
                props = GenericHeaderProps(
                    title = "Notifications",
                    showBackArrow = true,
                    onBackClick = onBack,
                    titleAlignment = HeaderTitleAlignment.CENTER
                ),
                scheme = scheme
            )

            // ── Filter Tabs Row (Horizontally Scrollable) ─────────────────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = ODSVariables.spacingLayout1, vertical = ODSVariables.spacingComponent2),
                gap = ODSVariables.spacingComponent3,
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.tabs.forEach { tab ->
                    val isSelected = state.selectedTab == tab
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { viewModel.selectTab(tab) }
                        ),
                        padding = ODSPadding(
                            top = 8.dp,
                            bottom = 8.dp,
                            left = 16.dp,
                            right = 16.dp
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        border = if (!isSelected) ODSBorder(
                            width = 1.dp,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ) else null,
                        verticalAlignment = Alignment.CenterVertically,
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicAccent else scheme.basicBackgroundCard
                            )
                        )
                    ) {
                        ODSText(
                            text = tab,
                            style = ODSTextStyles.bodySBold,
                            color = if (isSelected) scheme.basicBackgroundCard else scheme.basicTextRecessive
                        )
                    }
                }
            }

            // ── Notifications List using ODSLazyColumn ────────────────────
            if (state.notifications.isEmpty()) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "No notifications yet",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextRecessive
                    )
                }
            } else {
                ODSLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        top = ODSVariables.spacingComponent3,
                        bottom = ODSVariables.spacingComponent5
                    ),
                    gap = ODSVariables.spacingComponent3
                ) {
                    items(state.notifications.size) { index ->
                        val item = state.notifications[index]
                        if (item.isSectionHeader) {
                            ODSRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = ODSVariables.spacingComponent2),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSText(
                                    text = item.title,
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        } else {
                            NotificationListItem(
                                item = item,
                                scheme = scheme,
                                onClick = {
                                    viewModel.markAsRead(item.id)
                                    when (item.targetScreen) {
                                        "chat" -> onNavigateToChat(item.targetId ?: item.id, item.title)
                                        "profile" -> onNavigateToProfile(item.targetId ?: item.id, item.title)
                                        else -> {
                                            if (item.category == "Messages") {
                                                onNavigateToChat(item.targetId ?: item.id, item.title)
                                            } else if (item.category == "Matches") {
                                                onNavigateToProfile(item.targetId ?: item.id, item.title)
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationListItem(
    item: NotificationItem,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    val iconDrawableRes = when (item.iconResName) {
        "coin_icon" -> com.telekom.odsystem.R.drawable.coin_icon
        else -> com.telekom.odsystem.R.drawable.ic_info
    }

    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        gap = 12.dp,
        padding = ODSPadding(all = 16.dp),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        verticalAlignment = Alignment.CenterVertically,
        background = listOf(
            ODSColorModel(
                hexColor = if (item.isUnread) scheme.basicBackgroundSubtle else scheme.basicBackgroundCard
            )
        )
    ) {
        if (!item.avatarUrl.isNullOrBlank()) {
            ODSBox(
                modifier = Modifier.size(56.dp),
                cornerRadius = ODSCorners(all = 28.dp),
                clipContent = true
            ) {
                ODSImage(
                    imageModel = ODSImageModel(
                        url = item.avatarUrl,
                        contentDescription = item.title
                    ),
                    modifier = Modifier.size(56.dp),
                    cornerRadius = ODSCorners(all = 28.dp),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            ODSBox(
                modifier = Modifier.size(56.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                cornerRadius = ODSCorners(all = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = iconDrawableRes,
                        contentDescription = item.title
                    ),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSText(
                    text = item.title,
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicTextDominant
                )
                ODSText(
                    text = item.timeAgo,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }

            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = item.message,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )

            if (!item.actionText.isNullOrBlank()) {
                ODSRow(
                    modifier = Modifier.padding(top = 4.dp),
                    padding = ODSPadding(
                        top = 6.dp,
                        bottom = 6.dp,
                        left = 12.dp,
                        right = 12.dp
                    ),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                ) {
                    ODSText(
                        text = item.actionText,
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicBackgroundCard
                    )
                }
            }
        }

        if (item.isUnread) {
            ODSBox(
                modifier = Modifier.size(8.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = 4.dp)
            )
        }
    }
}
