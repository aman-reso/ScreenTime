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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.config.R as ConfigR

/**
 * Notification Permission BottomSheet using ODSBottomSheet.
 * 100% ODS components using ODSColumn (NO LazyColumn inside BottomSheet).
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
    var isDeniedState by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            isDeniedState = true
        }
    }

    ODSBottomSheet(
        scheme = scheme,
        showBottomSheet = true,
        props = ODSBottomSheetProps(showHandle = true),
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        contentSlot = {
            if (!isDeniedState) {
                NotificationAskingContent(
                    scheme = scheme,
                    onAllowClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            onPermissionGranted()
                        }
                    }
                )
            } else {
                NotificationDeniedContent(
                    scheme = scheme,
                    onOpenSettings = {
                        openAppSettings(context)
                    },
                    onSkipForNow = onDismiss
                )
            }
        }
    )
}

/**
 * Asking State: "Never Miss a Spark."
 */
@Composable
private fun NotificationAskingContent(
    scheme: ODSTheme,
    onAllowClick: () -> Unit
) {
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth(),
        padding = ODSPadding(
            top = ODSVariables.spacingLayout1,
            left = ODSVariables.spacingLayout1,
            right = ODSVariables.spacingLayout1,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent6
    ) {
        // ── Top Header Section ──────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingLayout1,
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

            // Sub-heading column with title and description
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2,
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Main Title
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(ConfigR.string.perm_never_miss_match_title),
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(ConfigR.string.perm_never_miss_match_subtitle),
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ── Benefit Cards Column ────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingLayout1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            FigmaBenefitCard(
                iconRes = R.drawable.ic_heart,
                title = stringResource(ConfigR.string.perm_when_matched_title),
                subtitle = stringResource(ConfigR.string.perm_when_matched_subtitle),
                scheme = scheme
            )

            FigmaBenefitCard(
                iconRes = R.drawable.ic_message_circle,
                title = stringResource(ConfigR.string.perm_instant_messages_title),
                subtitle = stringResource(ConfigR.string.perm_instant_messages_subtitle),
                scheme = scheme
            )

            FigmaBenefitCard(
                iconRes = R.drawable.ic_zap,
                title = stringResource(ConfigR.string.perm_daily_drops_title),
                subtitle = stringResource(ConfigR.string.perm_daily_drops_subtitle),
                scheme = scheme
            )
        }

        // ── Action Button ───────────────────────────────────────────────────
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
                text = stringResource(ConfigR.string.perm_allow_notifications),
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicBackgroundCard
            )
        }
    }
}

/**
 * Reusable Benefit Card inside the Notification BottomSheet.
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
        gap = ODSVariables.spacingLayout1,
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
        horizontalAlignment = Alignment.Start,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        ODSColumn(
            cornerRadius = ODSCorners(all = ODSVariables.spacingComponent4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            background = listOf(ODSColorModel(hexColor = scheme.basicText)),
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
                    tint = scheme.basicTextOnAccent.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        ODSColumn(
            modifier = Modifier.weight(1f),
            gap = ODSVariables.spacingComponent1,
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = subtitle,
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
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
    ODSColumn(
        modifier = Modifier
            .fillMaxWidth(),
        padding = ODSPadding(
            horizontal = ODSVariables.spacingLayout2,
            top = ODSVariables.spacingComponent3,
            bottom = ODSVariables.spacingComponent6
        ),
        gap = ODSVariables.spacingComponent5
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = ODSVariables.spacingComponent4
        ) {
            ODSBox(
                modifier = Modifier.size(64.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.ic_bell_off),
                    tint = scheme.basicTextOnAccentSecondary.getColor(),
                    modifier = Modifier.size(28.dp)
                )
            }

            ODSText(
                text = stringResource(ConfigR.string.perm_silence_is_lonely_title),
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant,
                textAlign = TextAlign.Center
            )

            ODSText(
                text = stringResource(ConfigR.string.perm_silence_is_lonely_subtitle),
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
                    text = stringResource(ConfigR.string.perm_open_settings),
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
                    text = stringResource(ConfigR.string.perm_skip_for_now),
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicTextRecessive
                )
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
