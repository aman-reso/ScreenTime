package com.app.screentime.feature.preferences

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
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
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * Notification Permission BottomSheet.
 * Faithfully matches Figma node-id 10-746 and project design system rules:
 * - 100% ODS components and ODSLazyColumn.
 * - Semantic scheme color tokens.
 * - Max text size 16sp with Funnel Sans typography.
 * - Spacing and corner radii mapped through ODSVariables.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPermissionBottomSheet(
    onDismiss: () -> Unit,
    onPermissionGranted: () -> Unit = onDismiss,
    scheme: ODSTheme = zonaODSTheme,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isDeniedState by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            coroutineScope.launch {
                sheetState.hide()
                onPermissionGranted()
            }
        } else {
            isDeniedState = true
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = scheme.basicBackgroundCard.getColor(),
        scrimColor = scheme.basicTextDominant.getColor().copy(alpha = 0.5f),
        dragHandle = {
            ODSBox(
                modifier = Modifier.padding(
                    top = ODSVariables.spacingComponent4,
                    bottom = ODSVariables.spacingComponent2
                ),
                width = 38.dp,
                height = 4.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicStroke)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusExtraSmall)
            )
        }
    ) {
        if (!isDeniedState) {
            NotificationAskingContent(
                scheme = scheme,
                onAllowClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        coroutineScope.launch {
                            sheetState.hide()
                            onPermissionGranted()
                        }
                    }
                },
                onMaybeLater = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                }
            )
        } else {
            NotificationDeniedContent(
                scheme = scheme,
                onOpenSettings = {
                    openAppSettings(context)
                },
                onSkipForNow = {
                    coroutineScope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                }
            )
        }
    }
}

/**
 * Asking State: "Never Miss a Spark."
 * Implements Figma node-id 10-746.
 */
@Composable
private fun NotificationAskingContent(
    scheme: ODSTheme,
    onAllowClick: () -> Unit,
    onMaybeLater: () -> Unit
) {
    ODSLazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        padding = ODSPadding(
            top = ODSVariables.spacingLayout1,
            left = ODSVariables.spacingLayout1,
            right = ODSVariables.spacingLayout1,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent8 // 32.dp gap matching Figma node-id 10-746
    ) {
        item {
            // ── Top Header Section (Figma node-id 10-746) ───────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingLayout1, // 16.dp
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 80dp Circular Bell Icon Well
                ODSRow(
                    modifier = Modifier.size(80.dp),
                    padding = ODSPadding(all = ODSVariables.spacingLayout1),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                ) {
                    ODSBox(
                        clipContent = true,
                        contentAlignment = Alignment.Center,
                        width = 32.dp,
                        height = 32.dp
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_bell,
                                contentDescription = "Notification Bell"
                            ),
                            tint = scheme.basicAccent.getColor(),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Sub-heading column with badge and descriptions
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3, // 8.dp
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // "instant connection" badge pill
                    ODSRow(
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent2,
                            bottom = ODSVariables.spacingComponent2,
                            left = 10.dp,
                            right = 10.dp
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                        ),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        ODSText(
                            text = "instant connection",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicTextDominant
                        )
                    }

                    // Main Title
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Never Miss a Spark.",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant,
                        textAlign = TextAlign.Center
                    )

                    // Subtitle
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "The ZONA universe moves fast. Turn on notifications so you can claim your energy in real-time.",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        item {
            // ── Benefit Cards Column (Figma node-id 10-746) ─────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingLayout1, // 16.dp
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                // Card 1: When you get matched
                FigmaBenefitCard(
                    iconRes = R.drawable.ic_heart,
                    title = "When you get matched",
                    subtitle = "Don't keep them waiting in the dark.",
                    scheme = scheme
                )

                // Card 2: Instant messages
                FigmaBenefitCard(
                    iconRes = R.drawable.ic_message_circle,
                    title = "Instant messages",
                    subtitle = "Keep the chemistry flowing hot.",
                    scheme = scheme
                )

                // Card 3: Surprise vibes & party pop
                FigmaBenefitCard(
                    iconRes = R.drawable.ic_zap,
                    title = "Surprise vibes & party pop",
                    subtitle = "Exclusive daily drops near you.",
                    scheme = scheme
                )
            }
        }

        item {
            // ── Action Buttons ──────────────────────────────────────────────
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Allow CTA Button
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onAllowClick
                        ),
                    height = 52.dp,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "Allow Notifications",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicBackgroundCard
                    )
                }

                // Maybe Later
                ODSBox(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onMaybeLater
                        )
                        .padding(vertical = ODSVariables.spacingComponent2),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "Maybe Later",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

/**
 * Reusable Benefit Card inside the Notification BottomSheet
 * Matching Figma node-id 10-746.
 */
@Composable
private fun FigmaBenefitCard(
    iconRes: Int,
    title: String,
    subtitle: String,
    scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = ODSVariables.spacingLayout1, // 16.dp
        padding = ODSPadding(all = ODSVariables.spacingLayout1), // 16.dp
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge), // 20.dp
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle))
    ) {
        // 40x40dp white rounded container with 20x20dp vector
        ODSColumn(
            cornerRadius = ODSCorners(all = ODSVariables.spacingComponent4), // 12.dp
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)), // #FFFFFF
            width = 40.dp,
            height = 40.dp
        ) {
            ODSBox(
                clipContent = true,
                contentAlignment = Alignment.Center,
                width = 20.dp,
                height = 20.dp
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = iconRes),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Text Column
        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1, // 2.dp
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicTextDominant
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextDominant
            )
        }
    }
}

/**
 * Denied State: "Silence is Lonely."
 */
@Composable
private fun NotificationDeniedContent(
    scheme: ODSTheme,
    onOpenSettings: () -> Unit,
    onSkipForNow: () -> Unit
) {
    ODSLazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout2,
            top = ODSVariables.spacingComponent3,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent5
    ) {
        item {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = ODSVariables.spacingComponent4
            ) {
                // Muted Bell Icon
                ODSBox(
                    modifier = Modifier.size(64.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.ic_bell_off),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(28.dp)
                    )
                }

                ODSText(
                    text = "Silence is Lonely",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicTextDominant,
                    textAlign = TextAlign.Center
                )

                ODSText(
                    text = "You currently have notifications disabled. Without them, you won't know if someone matches or messages you.",
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )

                // "HOW TO ENABLE" Instructions Card
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(all = ODSVariables.spacingLayout1)
                ) {
                    ODSColumn(gap = ODSVariables.spacingComponent3) {
                        ODSText(
                            text = "HOW TO ENABLE:",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicAccent
                        )

                        ODSRow(gap = ODSVariables.spacingComponent3) {
                            ODSText(
                                text = "1.",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicAccent
                            )
                            ODSText(
                                text = "Tap the button below to open system Settings.",
                                style = ODSTextStyles.bodySRegular,
                                color = scheme.basicTextDominant
                            )
                        }

                        ODSRow(gap = ODSVariables.spacingComponent3) {
                            ODSText(
                                text = "2.",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicAccent
                            )
                            ODSText(
                                text = "Select 'Notifications' and turn 'Allow Notifications' ON.",
                                style = ODSTextStyles.bodySRegular,
                                color = scheme.basicTextDominant
                            )
                        }
                    }
                }

                // Open Settings Button
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onOpenSettings
                        ),
                    height = 50.dp,
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "Open Settings",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicBackgroundCard
                    )
                }

                // Skip for Now Button
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onSkipForNow
                        ),
                    height = 48.dp,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "Skip for Now",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        }
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
