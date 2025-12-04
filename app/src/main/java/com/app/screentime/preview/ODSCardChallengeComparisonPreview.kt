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
import com.telekom.odsystem.atoms.cardengagement.ODSCardEngagement
import com.telekom.odsystem.atoms.cardengagement.ODSCardEngagementProps
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardimage.ODSCardImage
import com.telekom.odsystem.organisms.cardimage.ODSCardImageImagePosition
import com.telekom.odsystem.organisms.cardimage.ODSCardImageProps

/**
 * Comparison preview showing ODSCardEngagement vs ODSCardImage
 * for challenge screen use cases
 */
@Preview(showBackground = true)
@Composable
fun ODSCardChallengeComparisonPreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent5
        ) {
            // Section: ODSCardEngagement Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "ODSCardEngagement - For Challenges",
                    style = DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )
                
                // Challenge Example 1 - Engagement Card
                ODSCardEngagement(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardEngagementProps(
                        label = "30-Day Digital Detox Challenge",
                        image = ODSImageModel(
                            drawableRes = android.R.drawable.ic_menu_gallery
                        )
                    ),
                    onClick = {}
                )

                // Challenge Example 2 - Engagement Card
                ODSCardEngagement(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardEngagementProps(
                        label = "Reduce Screen Time by 30%",
                        image = ODSImageModel(
                            drawableRes = android.R.drawable.ic_menu_gallery
                        )
                    ),
                    onClick = {}
                )

                // Challenge Example 3 - Engagement Card
                ODSCardEngagement(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardEngagementProps(
                        label = "Weekly Focus Challenge - 2 hours daily focus mode",
                        image = ODSImageModel(
                            drawableRes = android.R.drawable.ic_menu_gallery
                        )
                    ),
                    onClick = {}
                )
            }

            // Section: ODSCardImage Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                ODSText(
                    text = "ODSCardImage - For Challenges",
                    style = DSTextStyles.titleM,
                    color = neutralScheme.basicText
                )

                // Challenge Example 1 - Image Card with Full Content
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
                                text = "30-Day Digital Detox Challenge",
                                style = DSTextStyles.titleM,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Complete 30 days of reduced screen time and earn premium rewards",
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
                                label = "Join Challenge",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )

                // Challenge Example 2 - Image Card Compact
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
                                text = "Reduce Screen Time by 30%",
                                style = DSTextStyles.titleS,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Earn rewards for reducing daily screen time",
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
                                variant = ODSButtonVariant.OUTLINE
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )

                // Challenge Example 3 - Image Card with Stats
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
                                text = "Weekly Focus Challenge",
                                style = DSTextStyles.titleM,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Use focus mode for 2 hours daily",
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
                                            imageVector = Icons.Default.People,
                                            contentDescription = "Participants"
                                        )
                                    )
                                    ODSText(
                                        text = "1,234 participants",
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
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating"
                                        )
                                    )
                                    ODSText(
                                        text = "4.8",
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
                                label = "Start Challenge",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )
            }

            // Recommendation Section
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Recommendation for Challenge Screen",
                    style = DSTextStyles.titleS,
                    color = neutralScheme.basicText
                )
                ODSText(
                    text = "ODSCardImage is recommended for challenges because:",
                    style = DSTextStyles.bodyMRegular,
                    color = neutralScheme.basicText
                )
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSRow(
                        gap = DSVariables.spacingComponent2,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check"
                            )
                        )
                        ODSText(
                            text = "Supports custom content slots for challenge details",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicText
                        )
                    }
                    ODSRow(
                        gap = DSVariables.spacingComponent2,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check"
                            )
                        )
                        ODSText(
                            text = "Has action slot for Join/Start buttons",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicText
                        )
                    }
                    ODSRow(
                        gap = DSVariables.spacingComponent2,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check"
                            )
                        )
                        ODSText(
                            text = "Better for displaying challenge images with aspect ratio control",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicText
                        )
                    }
                    ODSRow(
                        gap = DSVariables.spacingComponent2,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Check"
                            )
                        )
                        ODSText(
                            text = "More flexible layout options (horizontal, vertical, image top/bottom)",
                            style = DSTextStyles.bodySRegular,
                            color = neutralScheme.basicText
                        )
                    }
                }
            }
        }
    }
}

