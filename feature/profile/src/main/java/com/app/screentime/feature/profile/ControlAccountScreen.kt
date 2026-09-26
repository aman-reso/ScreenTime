package com.app.screentime.feature.profile

import com.app.screentime.core.ui.util.showODSToast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.ZonaColors
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
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
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Control Account Screen.
 * Provides account administration, session controls, and logout functionality.
 *
 * Rules:
 * 1. 100% ODS components & ODSLazyColumn.
 * 2. Colors picked from `scheme: ODSTheme` & `ZonaColors`.
 * 3. Max text size 16sp with Funnel Sans font styles.
 * 4. Padding, margins, and gaps mapped to ODSVariables.
 */
@Composable
fun ControlAccountScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val resolvedName = uiState.displayName.ifBlank { uiState.user?.name ?: "Current User" }
    val resolvedEmail =
        uiState.email.ifBlank { uiState.user?.email ?: uiState.user?.phone ?: "Connected Account" }
    val userId = uiState.user?.id ?: "usr_current"

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // ── 1. Top Bar with Back Chevron & Title ──────────────────────
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
                    verticalAlignment = Alignment.CenterVertically,
                    gap = ODSVariables.spacingComponent3
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier
                            .size(DSVariables.spacingComponent6)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onBack
                            )
                    )

                    ODSText(
                        text = "Control Account",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicTextDominant
                    )
                }
            }

            // ── 2. Scrollable Account Content ──────────────────────────────
            ODSLazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                padding = ODSPadding(
                    left = ODSVariables.spacingLayout1,
                    right = ODSVariables.spacingLayout1,
                    top = ODSVariables.spacingComponent2,
                    bottom = ODSVariables.spacingComponent6
                ),
                gap = ODSVariables.spacingLayout1
            ) {
                // ── Account Overview Card ──────────────────────────────────
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ODSRow(
                                gap = ODSVariables.spacingComponent3,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar circle with first initial
                                ODSBox(
                                    modifier = Modifier.size(44.dp),
                                    cornerRadius = ODSCorners(all = 22.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSText(
                                        text = resolvedName.firstOrNull()?.uppercase() ?: "U",
                                        style = ODSTextStyles.bodyMBold,
                                        color = ZonaColors.TextInverse
                                    )
                                }

                                ODSColumn(gap = 2.dp) {
                                    ODSText(
                                        text = resolvedName,
                                        style = ODSTextStyles.bodyMBold,
                                        color = scheme.basicTextDominant
                                    )
                                    ODSText(
                                        text = resolvedEmail,
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
                            }

                            // Active status badge
                            ODSRow(
                                padding = ODSPadding(
                                    top = 4.dp,
                                    bottom = 4.dp,
                                    left = 8.dp,
                                    right = 8.dp
                                ),
                                cornerRadius = ODSCorners(all = 12.dp),
                                background = listOf(ODSColorModel(hexColor = ZonaColors.ActiveLime))
                            ) {
                                ODSText(
                                    text = "Active",
                                    style = ODSTextStyles.microcopyBold,
                                    color = ZonaColors.TextInverse
                                )
                            }
                        }
                    }
                }

                // ── Session Controls Header ────────────────────────────────
                item {
                    ODSText(
                        text = "Session Controls",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextDominant,
                        modifier = Modifier.padding(top = ODSVariables.spacingComponent2)
                    )
                }

                // ── Logout Action Card ─────────────────────────────────────
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { showLogoutConfirmDialog = true }
                                )
                                .padding(vertical = ODSVariables.spacingComponent2),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ODSRow(
                                gap = ODSVariables.spacingComponent3,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSBox(
                                    modifier = Modifier.size(36.dp),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                    background = listOf(
                                        ODSColorModel(
                                            hexColor = ZonaColors.ActionPrimary.copy(
                                                alpha = 0.15f
                                            )
                                        )
                                    ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_close,
                                            contentDescription = "Logout"
                                        ),
                                        tint = ZonaColors.ActionPrimary.getColor(),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                ODSColumn(gap = 2.dp) {
                                    ODSText(
                                        text = "Log Out",
                                        style = ODSTextStyles.bodySBold,
                                        color = ZonaColors.ActionPrimary
                                    )
                                    ODSText(
                                        text = "Sign out of your account on this device",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
                            }

                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_chevron_right,
                                    contentDescription = "Action"
                                ),
                                tint = ZonaColors.ActionPrimary.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // ── Additional Account Settings ────────────────────────────
                item {
                    ODSText(
                        text = "Security & Privacy",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextDominant,
                        modifier = Modifier.padding(top = ODSVariables.spacingComponent2)
                    )
                }

                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = ODSVariables.spacingComponent3,
                        padding = ODSPadding(all = ODSVariables.spacingComponent4),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ),
                        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
                    ) {
                        // Switch Account Row
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        context.showODSToast("Switch account requires logging in again")
                                        showLogoutConfirmDialog = true
                                    }
                                )
                                .padding(vertical = ODSVariables.spacingComponent2),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ODSRow(
                                gap = ODSVariables.spacingComponent3,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSBox(
                                    modifier = Modifier.size(32.dp),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_user,
                                            contentDescription = "Switch Account"
                                        ),
                                        tint = scheme.basicAccent.getColor(),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                ODSColumn(gap = 2.dp) {
                                    ODSText(
                                        text = "Switch Account",
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicTextDominant
                                    )
                                    ODSText(
                                        text = "Sign in with a different phone or Google account",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
                            }

                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_chevron_right,
                                    contentDescription = "Navigate"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Divider
                        ODSBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        )

                        // Delete Account (Danger Zone)
                        ODSRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { showDeleteConfirmDialog = true }
                                )
                                .padding(vertical = ODSVariables.spacingComponent2),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ODSRow(
                                gap = ODSVariables.spacingComponent3,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ODSBox(
                                    modifier = Modifier.size(32.dp),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSIcon(
                                        iconModel = ODSIconModel(
                                            drawableRes = R.drawable.ic_alert_triangle,
                                            contentDescription = "Delete Account"
                                        ),
                                        tint = scheme.basicTextRecessive.getColor(),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                ODSColumn(gap = 2.dp) {
                                    ODSText(
                                        text = "Delete Account",
                                        style = ODSTextStyles.bodySBold,
                                        color = scheme.basicTextRecessive
                                    )
                                    ODSText(
                                        text = "Permanently remove your account and data",
                                        style = ODSTextStyles.microcopyRegular,
                                        color = scheme.basicTextRecessive
                                    )
                                }
                            }

                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_chevron_right,
                                    contentDescription = "Navigate"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── Logout Confirmation Dialog ───────────────────────────────────────
        if (showLogoutConfirmDialog) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showLogoutConfirmDialog = false },
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = "Log Out of Account?",
                    bodyText = "Are you sure you want to log out? You can sign back in anytime using your phone or Google account."
                ),
                actionSlot = {
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Log Out",
                            buttonType = ODSButtonButtonType.STANDARD,
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            showLogoutConfirmDialog = false
                            viewModel.logout()
                            onLogout()
                        }
                    )
                }
            )
        }

        // ── Delete Account Dialog ──────────────────────────────────────────
        if (showDeleteConfirmDialog) {
            ODSDialog(
                scheme = scheme,
                onDismissRequest = { showDeleteConfirmDialog = false },
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                props = ODSDialogProps(
                    showCloseButton = true,
                    title = "Delete Account",
                    bodyText = "To delete your account and remove all personal data, please contact support or proceed with account deactivation."
                ),
                actionSlot = {
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Contact Support",
                            buttonType = ODSButtonButtonType.STANDARD,
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            showDeleteConfirmDialog = false
                            context.showODSToast("Redirecting to support...")
                        }
                    )
                }
            )
        }
    }
}
