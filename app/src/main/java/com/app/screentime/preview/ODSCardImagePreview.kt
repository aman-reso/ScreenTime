package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardimage.ODSCardImage
import com.telekom.odsystem.organisms.cardimage.ODSCardImageImagePosition
import com.telekom.odsystem.organisms.cardimage.ODSCardImageProps

@Preview(showBackground = true)
@Composable
fun ODSCardImagePreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Card Image - Top Position - Basic
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_16_9
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Challenge Title",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Challenge description goes here",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                onClick = {}
            )

            // Card Image - Top Position - With Action Button
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_16_9
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Reduce Screen Time Challenge",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Reduce your daily screen time by 30% and earn rewards",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                actionSlot = {
                    ODSButton(
                        scheme = neutralScheme,
                        props = ODSButtonProps(
                            label = "Join Challenge",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {}
                    )
                },
                onClick = {}
            )

            // Card Image - Top Position - Challenge Card with Icons
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_16_9
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent3
                    ) {
                        ODSText(
                            text = "30-Day Digital Detox",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Complete 30 days of reduced screen time",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent4
                        ) {
                            ODSRow(
                                gap = DSVariables.spacingComponent1,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = "Duration"
                                    )
                                )
                                ODSText(
                                    text = "30 Days",
                                    style = DSTextStyles.bodySRegular,
                                    color = neutralScheme.basicTextRecessive
                                )
                            }
                            ODSRow(
                                gap = DSVariables.spacingComponent1,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "Reward"
                                    )
                                )
                                ODSText(
                                    text = "Premium Badge",
                                    style = DSTextStyles.bodySRegular,
                                    color = neutralScheme.basicTextRecessive
                                )
                            }
                        }
                    }
                },
                actionSlot = {
                    ODSButton(
                        scheme = neutralScheme,
                        props = ODSButtonProps(
                            label = "Join Now",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {}
                    )
                },
                onClick = {}
            )

            // Card Image - Bottom Position
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.BOTTOM,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_16_9
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Weekly Focus Challenge",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Focus mode for 2 hours daily",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                actionSlot = {
                    ODSButton(
                        scheme = neutralScheme,
                        props = ODSButtonProps(
                            label = "Start Challenge",
                            variant = ODSButtonVariant.OUTLINE
                        ),
                        onClick = {}
                    )
                },
                onClick = {}
            )

            // Card Image - Horizontal Layout
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_1_1,
                    isHorizontal = true
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "App Blocking Master",
                            style = DSTextStyles.titleS,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Block 10 apps this week",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                actionSlot = {
                    ODSButton(
                        scheme = neutralScheme,
                        props = ODSButtonProps(
                            label = "Join",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {}
                    )
                },
                onClick = {}
            )

            // Card Image - With Logo Overlay
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    logo = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_16_9
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Premium Challenge",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Exclusive challenge with special rewards",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                actionSlot = {
                    ODSButton(
                        scheme = neutralScheme,
                        props = ODSButtonProps(
                            label = "Join Premium",
                            variant = ODSButtonVariant.PRIMARY
                        ),
                        onClick = {}
                    )
                },
                onClick = {}
            )

            // Card Image - Different Aspect Ratios
            ODSCardImage(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardImageProps(
                    imagePosition = ODSCardImageImagePosition.TOP,
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    ),
                    imageAspectRatio = ODSAspectRatio.VALUE_4_3
                ),
                contentSlot = {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "4:3 Aspect Ratio",
                            style = DSTextStyles.titleM,
                            color = neutralScheme.basicText
                        )
                        ODSText(
                            text = "Challenge with 4:3 image ratio",
                            style = DSTextStyles.bodyMRegular,
                            color = neutralScheme.basicTextRecessive
                        )
                    }
                },
                onClick = {}
            )
        }
    }
}

