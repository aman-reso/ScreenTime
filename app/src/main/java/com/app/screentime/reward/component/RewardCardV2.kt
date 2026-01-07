package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.organisms.cardimage.ODSCardImage
import com.telekom.odsystem.organisms.cardimage.ODSCardImageImagePosition
import com.telekom.odsystem.organisms.cardimage.ODSCardImageProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Reward Card V2 Component
 * Displays a reward card with image, tag, title, coin, description, and Claim button on the right
 * Uses ODSCardImage with contentSlot and actionSlot
 */
@Composable
fun RewardCardV2(
    title: String,
    description: String? = null,
    coin: String? = null,
    image: ODSImageModel? = null,
    tag: ODSImageModel? = null,
    onClaimClick: () -> Unit = {},
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSCardImage(
        modifier = modifier,
        scheme = scheme,
        props = ODSCardImageProps(
            imagePosition = ODSCardImageImagePosition.TOP,
            image = image ?: ODSImageModel(
                url = "https://via.placeholder.com/400x200",
                contentDescription = "Reward image"
            ),
            logo = tag, // Tag is displayed as logo overlay
            imageAspectRatio = ODSAspectRatio.VALUE_16_9
        ),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                gap = DSVariables.spacingComponent3
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1
                ) {
                    // Title
                    ODSText(
                        text = title,
                        style = com.telekom.odsystem.DSTextStyles.oxBodyMBold,
                        color = scheme.basicText,
                        maxLines = 2
                    )

                    // Coin
                    coin?.let {
                        ODSText(
                            text = it,
                            style = com.telekom.odsystem.DSTextStyles.oxBodyMRegular,
                            color = scheme.basicText
                        )
                    }

                    // Description
                    description?.let {
                        ODSText(
                            text = it,
                            style = com.telekom.odsystem.DSTextStyles.oxBodySRegular,
                            color = scheme.basicTextRecessive,
                            maxLines = 2
                        )
                    }
                }

                // Claim button on the right
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Claim",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onClaimClick
                )
            }
        },
        onClick = onClick
    )
}

