package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import androidx.compose.foundation.layout.size
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Reward Claim Dialog Component
 * Displays a dialog for claiming rewards with title, description, image, tag, coin, and confirm button
 */
@Composable
fun RewardClaimDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    description: String,
    coin: String,
    image: ODSImageModel? = null,
    tag: ODSImageModel? = null,
    onConfirmClick: () -> Unit = {},
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
                showCloseButton = false,
                showScrollbar = false,
                title = null,
                bodyText = null
            ),
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent4,
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        // Main image
                        ODSImage(
                            imageModel = image ?: ODSImageModel(
                                url = "https://via.placeholder.com/400x200",
                                contentDescription = "Reward gift box"
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            height = 200.dp,
                            cornerRadius = ODSCorners(all = 12.dp)
                        )

                        // Tag overlay (if provided)
                        tag?.let {
                            ODSBox(
                                padding = ODSPadding(all = DSVariables.spacingComponent2),
                                modifier = Modifier
                                    .align(androidx.compose.ui.Alignment.TopEnd)
                            ) {
                                ODSImage(
                                    imageModel = it,
                                    width = 48.dp,
                                    height = 48.dp,
                                    cornerRadius = ODSCorners(all = 24.dp)
                                )
                            }
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

                    // Coin/Amount
                    ODSText(
                        text = coin,
                        style = DSTextStyles.titleM,
                        color = scheme.basicAccent,
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
                            label = "Claim reward",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.LARGE
                        ),
                        onClick = {
                            onConfirmClick()
                            onDismiss()
                        }
                    )
                }
            }
        )
    }
}

