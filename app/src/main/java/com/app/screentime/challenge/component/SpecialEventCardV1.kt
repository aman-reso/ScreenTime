package com.app.screentime.challenge.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import com.app.screentime.network.model.Challenge
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.magentaScheme
import androidx.compose.ui.semantics.Role
import com.telekom.odsystem.R
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardimage.ODSCardImage
import com.telekom.odsystem.organisms.cardimage.ODSCardImageImagePosition
import com.telekom.odsystem.organisms.cardimage.ODSCardImageProps

/**
 * Special event card component for displaying limited-time challenges.
 * Uses ODSCardImage for consistent styling with a vibrant magenta scheme.
 *
 * @param challenge The challenge to display
 * @param modifier Modifier to be applied to the component
 * @param onView Callback when view button is clicked
 * @param scheme ODS theme scheme (defaults to magentaScheme for special events)
 */
@Composable
fun SpecialEventCardV(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onView: () -> Unit,
    scheme: ODSTheme = magentaScheme
) {
    ODSCardImage(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardImageProps(
            imagePosition = ODSCardImageImagePosition.TOP,
            image = if (!challenge.thumbnail.isNullOrEmpty()) {
                ODSImageModel(url = challenge.thumbnail)
            } else null,
            imageAspectRatio = ODSAspectRatio.VALUE_16_9
        ),
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {

                ODSColumn(
                    modifier = Modifier.weight(1f, fill = false),
                    gap = DSVariables.spacingComponent3
                ) {
                    // Title
                    ODSText(
                        text = challenge.title,
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Subtitle / description
                    ODSText(
                        text = challenge.description,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Tags
                    if (!challenge.tags.isNullOrEmpty() || !challenge.tag.isNullOrEmpty()) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent2
                        ) {
                            challenge.tags?.firstOrNull()?.let { tag ->
                                ODSTagStatic(
                                    scheme = scheme,
                                    props = ODSTagStaticProps(
                                        label = tag,
                                        type = ODSTagStaticType.PROMOTION
                                    )
                                )
                            }
                            if (!challenge.tag.isNullOrEmpty()) {
                                ODSTagStatic(
                                    scheme = scheme,
                                    props = ODSTagStaticProps(
                                        label = challenge.tag,
                                        type = ODSTagStaticType.PROMOTION
                                    )
                                )
                            }
                        }
                    }

                    // Reward
                    if (challenge.reward.isNotEmpty()) {
                        ODSRow(
                            gap = DSVariables.spacingComponent2,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.EmojiEvents,
                                    tint = scheme.functionalWarningStandard,
                                    contentDescription = null
                                ),
                                width = DSVariables.sizingComponent5,
                                height = DSVariables.sizingComponent5
                            )
                            ODSText(
                                text = challenge.reward,
                                style = DSTextStyles.bodyMBold,
                                color = scheme.basicText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // SPACE between content and button
                Spacer(modifier = Modifier.width(DSVariables.spacingComponent3))

                // RIGHT SIDE BUTTON — always visible
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            drawableRes = R.drawable.arrow_right_type_standard_size_standard,
                            tint = scheme.basicText,
                            contentDescription = "View details"
                        ),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onView
                )
            }
        },
        onClick = onView
    )
}


@Composable
fun SpecialEventCardV1(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onView: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val interactionSource = remember { MutableInteractionSource() }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .customClickable(
                interactionSource = interactionSource,
                onClick = onView,
                role = Role.Button, isPressed = {}
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            gap = DSVariables.spacingComponent3
        ) {
            if (!challenge.thumbnail.isNullOrEmpty()) {
                ODSBox(
                    modifier = Modifier
                        .width(DSVariables.sizingComponent15)
                        .height(DSVariables.sizingComponent15)
                ) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = if (challenge.thumbnail.isNotEmpty()) {
                            ODSImageModel(url = challenge.thumbnail)
                        } else null,
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            ODSColumn(
                modifier = Modifier.weight(1f),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = challenge.title,
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                ODSText(
                    text = challenge.description,
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!challenge.tags.isNullOrEmpty() || !challenge.tag.isNullOrEmpty()) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent3
                    ) {
                        challenge.tags?.takeIf { it.isNotEmpty() }?.let {
                            ODSTagStatic(
                                scheme = scheme,
                                props = ODSTagStaticProps(
                                    label = challenge.tags[0],
                                    type = ODSTagStaticType.PROMOTION
                                )
                            )
                        }
                        if (!challenge.tag.isNullOrEmpty()) {
                            ODSTagStatic(
                                scheme = scheme,
                                props = ODSTagStaticProps(
                                    label = challenge.tag,
                                    type = ODSTagStaticType.PROMOTION
                                )
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    gap = DSVariables.spacingComponent3
                ) {
                    if (challenge.reward.isNotEmpty()) {
                        ODSRow(
                            gap = DSVariables.spacingComponent2,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.EmojiEvents,
                                    tint = scheme.functionalWarningStandard,
                                    contentDescription = null
                                ),
                                width = DSVariables.sizingComponent5,
                                height = DSVariables.sizingComponent5
                            )
                            ODSText(
                                text = challenge.reward,
                                style = DSTextStyles.bodyMBold,
                                color = scheme.basicText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                drawableRes = R.drawable.arrow_right_type_standard_size_standard,
                                tint = scheme.basicText,
                                contentDescription = "View details"
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            variant = ODSButtonVariant.GHOST,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onView
                    )
                }
            }
        }
    }
}



