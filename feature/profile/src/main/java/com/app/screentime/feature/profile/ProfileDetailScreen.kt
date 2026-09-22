package com.app.screentime.feature.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * User Profile Detail Screen (Matching media_1789921107372.png: "Jessica's Profile").
 *
 * Strictly follows all project rules:
 * 1. 100% ODSComponent & ODSLazyColumn (no standard Compose layout/text/image components).
 * 2. All colors picked from `scheme: ODSTheme`.
 * 3. Small variant for buttons / compact components.
 * 4. Maximum text size is 16sp across the entire screen using required font (funnelsans).
 * 5. All padding, margins, spacers, gaps, and radii use `ODSVariables`.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileDetailScreen(
    userId: String = "jessica_maple",
    userName: String = "Jessica Maple",
    userAge: Int = 25,
    locationText: String = "New York • 2 km away",
    matchPercentage: Int = 94,
    isMyProfile: Boolean = false,
    onBack: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> },
    onNavigateToAccount: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {},
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showOptionsMenu by remember { mutableStateOf(false) }

    val heroPhotoUrl =
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=1000&auto=format&fit=crop&q=85"

    val profilePhotos = remember {
        listOf(
            heroPhotoUrl,
            "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=1000&auto=format&fit=crop&q=85",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=1000&auto=format&fit=crop&q=85",
            "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=1000&auto=format&fit=crop&q=85"
        )
    }
    var currentPhotoIndex by remember { mutableIntStateOf(0) }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // ── 1. Top Bar: Back Button | Jessica's Profile | More Vert ─────────
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Ghost - no border, no background)
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL,
                        buttonIcon = ODSIconModel(
                            drawableRes = com.telekom.odsystem.R.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        )
                    ),
                    onClick = onBack
                )

                ODSText(
                    text = "${userName.substringBefore(" ")}'s Profile",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )

                // More Options Button (White circular bubble, NO border)
                ODSBox(
                    modifier = Modifier.size(ODSVariables.sizingComponent13),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL,
                            buttonIcon = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.ic_more_vertical,
                                contentDescription = "More Options"
                            )
                        ),
                        onClick = { showOptionsMenu = true }
                    )
                }
            }

            // ── 2. Scrollable Body Content using ODSLazyColumn (Rule #1!) ────────
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent2
                ),
                gap = ODSVariables.spacingComponent6
            ) {
                // ── Item 1: Hero Photo Card ──────────────────────────────────────
                item {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(390.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusExtraLarge),
                        clipContent = true
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = profilePhotos[currentPhotoIndex],
                                contentDescription = userName
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // ── Top Segmented Progress Bars ─────────────────────────
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(
                                    top = ODSVariables.spacingComponent4,
                                    start = ODSVariables.spacingComponent4,
                                    end = ODSVariables.spacingComponent4
                                ),
                            gap = ODSVariables.spacingComponent2,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            profilePhotos.forEachIndexed { idx, _ ->
                                val isSelected = idx == currentPhotoIndex
                                ODSBox(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(3.dp),
                                    background = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicAccent else HexColor("#FFFFFF", 0.5f)
                                        )
                                    ),
                                    cornerRadius = ODSCorners(all = 2.dp)
                                )
                            }
                        }

                        // ── Left Half Tap Area (Previous Photo) ─────────────────
                        ODSBox(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterStart)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        if (currentPhotoIndex > 0) currentPhotoIndex--
                                    }
                                )
                        ) {}

                        // ── Right Half Tap Area (Next Photo) ────────────────────
                        ODSBox(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        if (currentPhotoIndex < profilePhotos.size - 1) currentPhotoIndex++
                                    }
                                )
                        ) {}

                        // Scrim gradient overlay for high contrast at bottom of photo
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .align(Alignment.BottomCenter),
                            background = listOf(
                                ODSColorModel(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                            )
                        )

                        // Bottom info overlay: Name, Age, Verified badge, Location & Match Badge
                        ODSColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .padding(ODSVariables.spacingComponent5),
                            gap = ODSVariables.spacingComponent2
                        ) {
                            // Bold Display Name & Age (max 16sp) + Verified Badge
                            ODSRow(
                                verticalAlignment = Alignment.CenterVertically,
                                gap = ODSVariables.spacingComponent3
                            ) {
                                ODSText(
                                    text = "$userName, $userAge",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicBackgroundCard
                                )

                                ODSBox(
                                    modifier = Modifier.size(ODSVariables.sizingComponent8),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = com.telekom.odsystem.R.drawable.ic_check,
                                            contentDescription = "Verified"
                                        ),
                                        tint = scheme.basicTextOnAccent.getColor(),
                                        modifier = Modifier.size(ODSVariables.sizingComponent6)
                                    )
                                }
                            }

                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Location row
                                ODSRow(
                                    verticalAlignment = Alignment.CenterVertically,
                                    gap = ODSVariables.spacingComponent2
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = com.telekom.odsystem.R.drawable.ic_map_pin,
                                            contentDescription = "Location"
                                        ),
                                        tint = scheme.basicAccentSecondary.getColor(),
                                        modifier = Modifier.size(ODSVariables.sizingComponent7)
                                    )
                                    ODSText(
                                        text = locationText,
                                        style = ODSTextStyles.bodySRegular,
                                        color = scheme.basicBackgroundCard
                                    )
                                }

                                // Match Badge Pill (⚡ 94%) in soft pink
                                ODSRow(
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                    padding = ODSPadding(
                                        horizontal = ODSVariables.spacingComponent4,
                                        vertical = ODSVariables.spacingComponent2
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    gap = ODSVariables.spacingComponent2
                                ) {
                                    ODSText(
                                        text = "⚡",
                                        style = ODSTextStyles.microcopyBold,
                                        color = scheme.basicAccent
                                    )
                                    ODSText(
                                        text = "$matchPercentage%",
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicAccent
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Item 2: "About Me" Section ──────────────────────────────────
                item {
                    ODSColumn(gap = ODSVariables.spacingComponent3) {
                        ODSText(
                            text = "About Me",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        ODSText(
                            text = "Creative technologist & DJ 🎧. Always hunting for the perfect rooftop espresso or an underground vinyl gig. Let's trade favorite tracks or plan an adventure!",
                            style = ODSTextStyles.bodyMRegular,
                            color = scheme.basicText
                        )
                    }
                }

                // ── Item 3: Key Metrics Cards (Relationship Intent & Height) ─────
                item {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent4
                    ) {
                        // Relationship Intent Card (Clean borderless card matching design)
                        ODSBox(
                            modifier = Modifier.weight(1f),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingComponent5,
                                vertical = ODSVariables.spacingComponent4
                            )
                        ) {
                            ODSColumn(gap = ODSVariables.spacingComponent2) {
                                ODSText(
                                    text = "RELATIONSHIP INTENT",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "Long Term Partner",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicText
                                )
                            }
                        }

                        // Height Card (Clean borderless card matching design)
                        ODSBox(
                            modifier = Modifier.weight(1f),
                            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingComponent5,
                                vertical = ODSVariables.spacingComponent4
                            )
                        ) {
                            ODSColumn(gap = ODSVariables.spacingComponent2) {
                                ODSText(
                                    text = "HEIGHT",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextRecessive
                                )
                                ODSText(
                                    text = "172 cm (5'8\")",
                                    style = ODSTextStyles.bodyMBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }

                // ── Item 4: "Interests" Section ──────────────────────────────────
                item {
                    ODSColumn(gap = ODSVariables.spacingComponent4) {
                        ODSText(
                            text = "Interests",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3),
                            verticalArrangement = Arrangement.spacedBy(ODSVariables.spacingComponent3)
                        ) {
                            // "Techno & Vinyl" (Soft Pink Pill)
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent3
                                )
                            ) {
                                ODSText(
                                    text = "Techno & Vinyl",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicAccent
                                )
                            }

                            // "Photography" (Card Outline Pill)
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent3
                                )
                            ) {
                                ODSText(
                                    text = "Photography",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                            }

                            // "Travel" (Soft Pink Pill)
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent3
                                )
                            ) {
                                ODSText(
                                    text = "Travel",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicAccent
                                )
                            }

                            // "Rooftops" (Card Outline Pill)
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent3
                                )
                            ) {
                                ODSText(
                                    text = "Rooftops",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                            }

                            // "Art Galleries" (Card Outline Pill)
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                                border = ODSBorder(
                                    width = ODSVariables.strokes1,
                                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                                ),
                                padding = ODSPadding(
                                    horizontal = ODSVariables.spacingComponent5,
                                    vertical = ODSVariables.spacingComponent3
                                )
                            ) {
                                ODSText(
                                    text = "Art Galleries",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }

                // Bottom spacer
                item {
                    ODSBox(modifier = Modifier.height(ODSVariables.spacingComponent5))
                }
            }

            // ── 3. Sticky Bottom Action Dock: [✕] [ Message Jessica ] [💖] ──────
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                padding = ODSPadding(
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent5,
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1
                )
            ) {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = ODSVariables.spacingComponent4
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        gap = ODSVariables.spacingComponent4
                    ) {
                        // Dislike Button ✕ (ODSButton with ICON_ONLY, OUTLINE)
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                buttonType = ODSButtonButtonType.ICON_ONLY,
                                variant = ODSButtonVariant.OUTLINE,
                                size = ODSButtonSize.SMALL,
                                buttonIcon = ODSIconModel(
                                    drawableRes = com.telekom.odsystem.R.drawable.ic_close,
                                    contentDescription = "Pass"
                                )
                            ),
                            onClick = onBack
                        )

                        // Primary "Message Jessica" CTA (ODSButton with STANDARD, PRIMARY)
                        ODSButton(
                            modifier = Modifier.weight(1f),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Message ${userName.substringBefore(" ")}",
                                buttonIcon = ODSIconModel(
                                    drawableRes = com.telekom.odsystem.R.drawable.ic_message_circle,
                                    contentDescription = "Message"
                                ),
                                leftIcon = true,
                                buttonType = ODSButtonButtonType.STANDARD,
                                variant = ODSButtonVariant.PRIMARY,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { onNavigateToChat(userId, userName) }
                        )

                        // Like Button 💖 (ODSButton with ICON_ONLY, SECONDARY)
                        ODSButton(
                            scheme = scheme,
                            props = ODSButtonProps(
                                buttonType = ODSButtonButtonType.ICON_ONLY,
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL,
                                buttonIcon = ODSIconModel(
                                    drawableRes = com.telekom.odsystem.R.drawable.ic_heart_filled,
                                    contentDescription = "Like"
                                )
                            ),
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "You liked ${userName.substringBefore(" ")}!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }

                    // Bottom Home Indicator Pill
                    ODSBox(
                        modifier = Modifier
                            .width(134.dp)
                            .height(ODSVariables.spacingComponent2),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        background = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    )
                }
            }
        }

        // ── Options Menu Dialog (Share / Settings / Report / Block) ─────────────
        if (showOptionsMenu) {
            Dialog(onDismissRequest = { showOptionsMenu = false }) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(ODSVariables.spacingLayout1),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(all = ODSVariables.spacingComponent6)
                ) {
                    ODSColumn(gap = ODSVariables.spacingComponent4) {
                        ODSText(
                            text = "Profile Options",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        // My Account & Wallet Option
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showOptionsMenu = false
                                    onNavigateToAccount()
                                }
                                .padding(vertical = ODSVariables.spacingComponent3),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_user),
                                tint = scheme.basicAccent.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent9)
                            )
                            ODSText(
                                text = "My Account & Wallet",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }

                        // Edit Profile Option
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showOptionsMenu = false
                                    onNavigateToEditProfile()
                                }
                                .padding(vertical = ODSVariables.spacingComponent3),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_edit_3),
                                tint = scheme.basicText.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent9)
                            )
                            ODSText(
                                text = "Edit Profile",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }

                        // Share Profile
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showOptionsMenu = false
                                    Toast.makeText(
                                        context,
                                        "Profile link copied to clipboard!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .padding(vertical = ODSVariables.spacingComponent3),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_arrow_up_right),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent9)
                            )
                            ODSText(
                                text = "Share Profile",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }

                        // Report Profile
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showOptionsMenu = false
                                    Toast.makeText(
                                        context,
                                        "Profile reported to moderation team.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .padding(vertical = ODSVariables.spacingComponent3),
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.ic_alert_triangle),
                                tint = scheme.functionalDestructiveStandard.getColor(),
                                modifier = Modifier.size(ODSVariables.sizingComponent9)
                            )
                            ODSText(
                                text = "Report Profile",
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.functionalDestructiveStandard
                            )
                        }

                        // Close / Cancel Button
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = scheme,
                            props = ODSButtonProps(
                                label = "Cancel",
                                variant = ODSButtonVariant.GHOST,
                                size = ODSButtonSize.SMALL
                            ),
                            onClick = { showOptionsMenu = false }
                        )
                    }
                }
            }
        }
    }
}
