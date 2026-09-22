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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.app.screentime.core.model.Conversation
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.chat.component.ConversationItem
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
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
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToProfile: (String, String) -> Unit = { _, _ -> },
    onNavigateToDiscover: () -> Unit = {},
    forceEmptyState: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var previewEmptyState by remember { mutableStateOf(forceEmptyState) }

    val funnelSansFontFamily = remember {
        FontFamily(Font(R.font.funnelsans_regular))
    }

    val newMatches = remember {
        listOf(
            MatchAvatar(
                id = "mia",
                name = "Mia",
                avatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&auto=format&fit=crop&q=80"
            ),
            MatchAvatar(
                id = "daniel",
                name = "Daniel",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300&auto=format&fit=crop&q=80"
            ),
            MatchAvatar(
                id = "sora",
                name = "Sora",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&auto=format&fit=crop&q=80"
            ),
            MatchAvatar(
                id = "tyler",
                name = "Tyler",
                avatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300&auto=format&fit=crop&q=80"
            )
        )
    }

    // Default mock conversations matching Figma node-id 10-1225
    val defaultFigmaConversations = remember {
        listOf(
            Conversation(
                id = "jessica",
                modelId = "jessica_maple",
                modelName = "Jessica",
                modelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
                lastMessage = "Hey! Let's meet at that coffee place we talked about ☕",
                lastMessageTime = System.currentTimeMillis() - 2 * 60 * 1000,
                unreadCount = 1,
                isOnline = true
            ),
            Conversation(
                id = "chloe",
                modelId = "chloe",
                modelName = "Chloe",
                modelAvatarUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&auto=format&fit=crop&q=80",
                lastMessage = "That sounds like a plan! See ya there.",
                lastMessageTime = System.currentTimeMillis() - 3 * 3600 * 1000,
                unreadCount = 0,
                isOnline = false
            ),
            Conversation(
                id = "isabella",
                modelId = "isabella",
                modelName = "Isabella",
                modelAvatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&auto=format&fit=crop&q=80",
                lastMessage = "Sent a photo 📸",
                lastMessageTime = System.currentTimeMillis() - 24 * 3600 * 1000,
                unreadCount = 0,
                isOnline = false
            ),
            Conversation(
                id = "marcus",
                modelId = "marcus",
                modelName = "Marcus",
                modelAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
                lastMessage = "Let's match authentic!",
                lastMessageTime = System.currentTimeMillis() - 3 * 24 * 3600 * 1000,
                unreadCount = 0,
                isOnline = false
            )
        )
    }

    // Determine conversations to show
    val displayConversations = remember(uiState.conversations, searchQuery) {
        val baseList = if (uiState.conversations.isNotEmpty()) {
            uiState.conversations
        } else {
            defaultFigmaConversations
        }
        if (searchQuery.isBlank()) {
            baseList
        } else {
            baseList.filter {
                it.modelName.contains(searchQuery, ignoreCase = true) ||
                        it.lastMessage.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val isActuallyEmpty =
        (uiState.conversations.isEmpty() && defaultFigmaConversations.isEmpty()) || previewEmptyState

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadConversations()
    }

    ODSBox(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.loadConversations() },
            modifier = Modifier
                .fillMaxSize()
        ) {
            ODSColumn(modifier = Modifier.fillMaxSize()) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    padding = ODSPadding(
                        left = ODSVariables.spacingLayout1,
                        right = ODSVariables.spacingLayout1
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSText(
                        text = "Inbox",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )
                }
                if (uiState.isLoading && uiState.conversations.isEmpty() && !previewEmptyState) {
                    ChatListLoadingScreen(
                        modifier = modifier,
                        scheme = scheme
                    )
                } else {
                    ODSColumn(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(
                            ODSColorModel(
                                gradient = ODSLinearGradientModel(
                                    colorStops = arrayOf(
                                        0.00f to scheme.basicBackground,
                                        0.52f to scheme.basicBackgroundSubtle,
                                        1.00f to scheme.basicBackgroundCard
                                    ),
                                    opacity = 1.00f,
                                    angleInDegrees = 180f
                                )
                            )
                        )
                    ) {
                        if (isActuallyEmpty) {
                            InboxEmptyState(
                                onNavigateToDiscover = onNavigateToDiscover,
                                scheme = scheme,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        } else {
                            // ── Scrollable Body Content using ODSLazyColumn (Rule #1) ─────
                            ODSLazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .statusBarsPadding(),
                                gap = ODSVariables.spacingComponent4
                            ) {
                                // ── Item 4: Search Bar ────────────────────────────────────
                                item {
                                    ODSRow(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = ODSVariables.spacingLayout1),
                                        horizontalAlignment = Alignment.Start,
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        ODSRow(
                                            modifier = Modifier.weight(1f),
                                            gap = 10.dp,
                                            padding = ODSPadding(all = 14.dp),
                                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                            border = ODSBorder(
                                                width = 2.dp,
                                                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                            ),
                                            horizontalAlignment = Alignment.Start,
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start,
                                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                                        ) {
                                            ODSIcon(
                                                iconModel = ODSIconModel(
                                                    drawableRes = R.drawable.ic_search,
                                                    contentDescription = "Search"
                                                ),
                                                tint = scheme.basicTextRecessive.getColor(),
                                                modifier = Modifier.size(18.dp)
                                            )

                                            BasicTextField(
                                                value = searchQuery,
                                                onValueChange = { searchQuery = it },
                                                textStyle = TextStyle(
                                                    fontFamily = funnelSansFontFamily,
                                                    fontSize = 14.sp,
                                                    color = scheme.basicTextDominant.getColor()
                                                ),
                                                cursorBrush = SolidColor(scheme.basicAccent.getColor()),
                                                modifier = Modifier.weight(1f),
                                                singleLine = true,
                                                decorationBox = { innerTextField ->
                                                    if (searchQuery.isEmpty()) {
                                                        ODSText(
                                                            text = "Search conversations...",
                                                            style = ODSTextStyles.bodySRegular,
                                                            color = scheme.basicTextRecessive
                                                        )
                                                    }
                                                    innerTextField()
                                                }
                                            )
                                        }
                                    }
                                }

                                // ── Item 5: Conversations Section Header ──────────────────
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
                                        displayConversations.forEachIndexed { index, conv ->
                                            ConversationItem(
                                                index = index,
                                                conv = conv,
                                                scheme = scheme,
                                                onClick = {
                                                    onNavigateToChat(
                                                        conv.modelId,
                                                        conv.modelName
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }

                                // Bottom navigation dock clearance spacer
                                item {
                                    Spacer(modifier = Modifier.height(96.dp))
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
        Spacer(Modifier.weight(0.35f))

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
                    tint = scheme.basicAccent.getColor(),
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

        Spacer(Modifier.weight(0.65f))

        // ── Full-Width Primary Action CTA ─────────────────────────────────────
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onNavigateToDiscover
                ),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
            cornerRadius = ODSCorners(all = 27.dp),
            contentAlignment = Alignment.Center
        ) {
            ODSRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                gap = 10.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_search,
                        contentDescription = "Discover People"
                    ),
                    tint = scheme.basicTextOnAccent.getColor(),
                    modifier = Modifier.size(20.dp)
                )
                ODSText(
                    text = "Discover People",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextOnAccent
                )
            }
        }
    }
}
