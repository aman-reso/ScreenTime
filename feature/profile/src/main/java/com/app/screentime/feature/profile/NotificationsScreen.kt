package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
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
    val iconRes: Int? = null,
    val isSectionHeader: Boolean = false
)

/**
 * Notifications Screen (Figma node-id 83-4).
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
    onBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> }
) {
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Matches", "Messages", "Activity")

    val notifications = remember {
        listOf(
            NotificationItem(id = "sec_today", title = "TODAY", timeAgo = "", message = "", category = "", isUnread = false, isSectionHeader = true),
            NotificationItem(
                id = "n1",
                title = "New Match! 🎉",
                timeAgo = "2m ago",
                message = "You and Elena matched! Send her a message to kick off the conversation.",
                category = "Matches",
                isUnread = true,
                actionText = "Say Hello 👋",
                avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300&auto=format&fit=crop&q=80"
            ),
            NotificationItem(
                id = "n2",
                title = "Jessica Ross",
                timeAgo = "15m ago",
                message = "Hey Alex! Are we still on for coffee this Thursday? ☕",
                category = "Messages",
                isUnread = true,
                actionText = "Reply",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&auto=format&fit=crop&q=80"
            ),
            NotificationItem(
                id = "n3",
                title = "Someone liked you!",
                timeAgo = "2h ago",
                message = "A premium subscriber from Manhattan liked your profile. Reveal who they are!",
                category = "Matches",
                isUnread = true,
                actionText = "Reveal Matches ✨",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80"
            ),
            NotificationItem(id = "sec_yest", title = "YESTERDAY", timeAgo = "", message = "", category = "", isUnread = false, isSectionHeader = true),
            NotificationItem(
                id = "n4",
                title = "Profile Views",
                timeAgo = "1d ago",
                message = "Elena and 3 other people viewed your profile yesterday. Keep it active!",
                category = "Activity",
                isUnread = false,
                iconRes = R.drawable.ic_eye
            ),
            NotificationItem(
                id = "n5",
                title = "Security Alert",
                timeAgo = "1d ago",
                message = "Account Secured: Your password was updated successfully. If this wasn't you, contact support immediately.",
                category = "Activity",
                isUnread = false,
                iconRes = R.drawable.ic_info
            ),
            NotificationItem(
                id = "n6",
                title = "Zona Wallet Refreshed",
                timeAgo = "1d ago",
                message = "Your weekly premium allowance has been loaded! +120 ZONA Credits successfully added.",
                category = "Activity",
                isUnread = false,
                iconRes = R.drawable.coin_icon
            )
        )
    }

    val filteredNotifications = remember(selectedTab, notifications) {
        if (selectedTab == "All") {
            notifications
        } else {
            notifications.filter { it.isSectionHeader || it.category.equals(selectedTab, ignoreCase = true) }
        }
    }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Top Bar (Rule #4: Generic 16sp Title) ─────────────────────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ODSRow(
                    gap = ODSVariables.spacingComponent3,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "Notifications",
                        style = ODSTextStyles.bodyMBold, // Max 16sp generic title
                        color = scheme.basicTextDominant
                    )
                    ODSRow(
                        padding = ODSPadding(top = 2.dp, bottom = 2.dp, left = 8.dp, right = 8.dp),
                        cornerRadius = ODSCorners(all = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    ) {
                        ODSText(
                            text = "3 New",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicBackgroundCard
                        )
                    }
                }

                ODSText(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Mark all read */ }
                    ),
                    text = "Mark all read",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicAccent
                )
            }

            // ── Filter Tabs Row ─────────────────────────────────────────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ODSVariables.spacingLayout1),
                gap = ODSVariables.spacingComponent3,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { selectedTab = tab }
                        ),
                        padding = ODSPadding(top = 8.dp, bottom = 8.dp, left = 16.dp, right = 16.dp),
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

            // ── Notifications List using ODSLazyColumn (Rule #1) ────────────
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
                items(filteredNotifications.size) { index ->
                    val item = filteredNotifications[index]
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
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        if (item.category == "Messages") {
                                            onNavigateToChat(item.id, item.title)
                                        } else if (item.category == "Matches") {
                                            onNavigateToProfile(item.id, item.title)
                                        }
                                    }
                                ),
                            gap = 12.dp,
                            padding = ODSPadding(all = 16.dp),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                            verticalAlignment = Alignment.CenterVertically,
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
                        ) {
                            if (item.avatarUrl != null) {
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
                            } else if (item.iconRes != null) {
                                ODSBox(
                                    modifier = Modifier.size(56.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                    cornerRadius = ODSCorners(all = 28.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = item.iconRes,
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

                                if (item.actionText != null) {
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
                }
            }
        }
    }
}
