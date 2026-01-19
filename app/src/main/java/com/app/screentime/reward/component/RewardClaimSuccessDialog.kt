package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Reward Claim Success Dialog Component
 * Displays a success confirmation dialog after claiming a reward
 */
@Composable
fun RewardClaimSuccessDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String = "Claim Successful",
    description: String = "Thank you for using AppTime",
    successImage: ODSImageModel? = null,
    onKeepTradingClick: () -> Unit = {},
    scheme: ODSTheme
) {
    if (showDialog) {
        ODSDialog(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            scheme = scheme,
            onDismissRequest = onDismiss,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            props = ODSDialogProps(
                showCloseButton = true,
                showScrollbar = false,
                title = null,
                bodyText = null
            ),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        successImage?.let {
                            ODSImage(
                                imageModel = it,
                                modifier = Modifier.fillMaxWidth(),
                                height = 200.dp
                            )
                        }

                        ODSBox(
                            modifier = Modifier.size(100.dp),
                            background = listOf(
                                ODSColorModel(
                                    scheme.basicBackground
                                )
                            ),
                            cornerRadius = ODSCorners(all = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.success_type_standard_size_standard,
                                    tint = scheme.functionalSuccessStandard,
                                    contentDescription = "Success checkmark"
                                ),
                                width = 60.dp,
                                height = 60.dp
                            )
                        }
                    }

                    // Title
                    ODSText(
                        text = title,
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Description
                    ODSText(
                        text = description,
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            actionSlot = {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Track Order",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = {
                            onKeepTradingClick()
                            onDismiss()
                        }
                    )
                }
            }
        )
    }
}

