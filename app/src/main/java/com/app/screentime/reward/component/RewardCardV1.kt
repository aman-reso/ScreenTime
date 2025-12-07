package com.app.screentime.reward.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Reward Card V1 Component
 * Displays a flexible reward card with image tag, coin/price, title, and action text
 */
@Composable
fun RewardCardV1(
    title: String,
    imageTag: ODSImageModel? = null,
    coinOrPrice: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = (if (modifier == Modifier) {
            modifier.width(180.dp)
        } else {
            modifier
        })
            .customClickable(
                onClick = onClick,
                isPressed = {}
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSColumn(
            gap = DSVariables.spacingComponent2
        ) {
            // Image Tag - always show, use provided or dummy
            ODSImage(
                imageModel = imageTag ?: ODSImageModel(
                    url = "https://via.placeholder.com/180x100",
                    placeholder = null,
                    contentDescription = "Reward image"
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                cornerRadius = ODSCorners(all = 12.dp)
            )

            // Title - max 2 lines
            ODSText(
                text = title,
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Coin or Price (if provided) - shown as normal text
            coinOrPrice?.let {
                ODSText(
                    text = it,
                    style = DSTextStyles.oxBodyMRegular,
                    color = scheme.basicText
                )
            }

            // Action Text (if provided) - Secondary button
            actionText?.let { action ->
                if (onActionClick != null) {
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = action,
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onActionClick
                    )
                } else {
                    ODSText(
                        text = action,
                        style = DSTextStyles.oxBodySRegular,
                        color = scheme.basicTextLink,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

