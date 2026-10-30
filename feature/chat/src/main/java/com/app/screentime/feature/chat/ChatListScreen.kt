package com.app.screentime.feature.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.app.screentime.ads.RewardedAdManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.core.ui.theme.FunnelSansFontFamily
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.ui.components.GenericHeader
import com.app.screentime.core.ui.components.GenericHeaderProps
import com.app.screentime.core.ui.components.HeaderTitleAlignment
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.component.ConversationItem
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSLinearGradientModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

data class MatchAvatar(
    val id: String,
    val name: String,
    val avatarUrl: String
)

/**
 * 4-Point Sparkle Star matching Zona design aesthetics.
 */
@Composable
fun FourPointSparkle(
    modifier: Modifier = Modifier,
    color: Color,
    filled: Boolean = true,
    strokeWidth: Float = 4f
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            quadraticTo(w * 0.5f, h * 0.5f, w, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, h)
            quadraticTo(w * 0.5f, h * 0.5f, 0f, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, 0f)
            close()
        }
        if (filled) {
            drawPath(path, color)
        } else {
            drawPath(path, color, style = Stroke(width = strokeWidth))
        }
    }
}

/**
 * Messages Inbox Screen (Matching Figma node-id 10-1225: https://figma.com/design/1TELYpr19qLAv99DexRNpi/Untitled?node-id=10-1225).
 *
 * 100% constructed with Telekom ODS components and Zona tokens.
 * Adheres strictly to GEMINI.md:
 * 1. 100% ODS components & ODSLazyColumn.
 * 2. Colors picked from composable's `scheme: ODSTheme`.
 * 3. Max text size is 16sp with Funnel Sans font.
 * 4. Padding, margins, and gaps mapped to ODSVariables.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ChatListViewModel = hiltViewModel(),
    onNavigateToChat: (String, String, String?) -> Unit = { _, _, _ -> },
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> },
    onNavigateToDiscover: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        activity?.let { RewardedAdManager.preloadAd(it) }
    }

    val isActuallyEmpty = uiState.conversations.isEmpty()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadConversations()
    }


    ODSBox(
        modifier = modifier
            .fillMaxSize()
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                GenericHeader(
                    props = GenericHeaderProps(
                        title = "Chats",
                        showBackArrow = false,
                        titleAlignment = HeaderTitleAlignment.START
                    ),
                    scheme = scheme
                )

                ODSColumn(
                    modifier = Modifier.fillMaxSize(),
                    background = listOf(
                        ODSColorModel(
                            hexColor = scheme.basicBackground
                        )
                    )
                ) {
                    if (uiState.isLoading) {
                        ODSBox(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSLoadingSpinner(
                                scheme = scheme,
                                props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.LARGE)
                            )
                        }
                    } else if (isActuallyEmpty) {
                        InboxEmptyState(
                            onNavigateToDiscover = onNavigateToDiscover,
                            scheme = scheme,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    } else {
                        ODSLazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            gap = ODSVariables.spacingComponent4
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(ODSVariables.spacingComponent3))
                            }
                            item {
                                ODSColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = ODSVariables.spacingLayout1),
                                    gap = ODSVariables.spacingComponent3,
                                    verticalAlignment = Alignment.Top,
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    uiState.conversations.forEachIndexed { index, conv ->
                                        ConversationItem(
                                            index = index,
                                            conv = conv,
                                            showDivider = index < uiState.conversations.lastIndex,
                                            scheme = scheme,
                                            onClick = {
                                                onNavigateToChat(
                                                    conv.modelId,
                                                    conv.modelName,
                                                    conv.id
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Empty Inbox State (Matching Figma design language).
 */
@Composable
private fun InboxEmptyState(
    onNavigateToDiscover: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier
            .padding(horizontal = ODSVariables.spacingLayout2)
            .padding(bottom = ODSVariables.spacingComponent8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Visual Emblem Illustration ───────────────────────────────────────
        ODSBox(
            modifier = Modifier.size(210.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main Deep Circle Disc
            ODSBox(
                modifier = Modifier.size(170.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = 1.5.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                cornerRadius = ODSCorners(all = 85.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_message_circle,
                        contentDescription = null
                    ),
                    tint = scheme.basicTextRecessive.getColor().copy(alpha = 0.35f),
                    modifier = Modifier.size(64.dp)
                )
            }

            // Top-Left Floating Speech Bubble with Sparkles
            ODSBox(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 8.dp, y = 22.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                ODSRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FourPointSparkle(
                        modifier = Modifier.size(10.dp),
                        color = scheme.basicTextOnAccent.getColor(),
                        filled = true
                    )
                    FourPointSparkle(
                        modifier = Modifier.size(14.dp),
                        color = scheme.basicTextOnAccent.getColor(),
                        filled = true
                    )
                    FourPointSparkle(
                        modifier = Modifier.size(10.dp),
                        color = scheme.basicTextOnAccent.getColor(),
                        filled = true
                    )
                }
            }

            // Top-Right Floating Sparkle
            FourPointSparkle(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-12).dp, y = 14.dp)
                    .size(24.dp),
                color = scheme.basicAccent.getColor(),
                filled = true
            )

            // Bottom-Right Floating Bubble with Heart
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-6).dp, y = (-18).dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                padding = ODSPadding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_heart_filled,
                        contentDescription = "Match"
                    ),
                    tint = scheme.basicTextOnAccentSecondary.getColor(),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Bottom-Left Floating Outline Sparkle
            FourPointSparkle(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 10.dp, y = (-14).dp)
                    .size(26.dp),
                color = scheme.basicAccent.getColor(),
                filled = false,
                strokeWidth = 5f
            )
        }

        Spacer(Modifier.height(ODSVariables.spacingComponent8))

        // ── Typography ────────────────────────────────────────────────────────
        ODSText(
            text = "No conversations yet",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(ODSVariables.spacingComponent4))

        ODSText(
            text = "Don't worry! Your matches are out there. Send a wave or start exploring profiles to spark your next chat.",
            style = ODSTextStyles.bodyMRegular,
            color = scheme.basicTextRecessive,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = ODSVariables.spacingLayout1)
        )

        Spacer(Modifier.height(ODSVariables.spacingComponent6))

        // ── Full-Width Primary Action CTA ─────────────────────────────────────
        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = "Discover People",
                variant = ODSButtonVariant.SECONDARY,
                buttonIcon = ODSIconModel(
                    drawableRes = R.drawable.ic_search,
                    contentDescription = "Discover People"
                ),
                leftIcon = true
            ),
            onClick = onNavigateToDiscover
        )
    }
}
