package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
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
import com.app.screentime.ui.theme.ColorPalette
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSWrap

/**
 * Challenge Variant Card 5 - "FAT BURNING HIIT" style
 * ODS scheme background, title, start time, end time, participant count, reward, image on right
 */
@Composable
fun ChallengeVariantCard5(
    title: String,
    description: String? = null,
    startTime: String,
    endTime: String,
    participantCount: Int,
    reward: String? = null,
    imageUrl: String? = null,
    countdownText: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    scheme: ODSTheme? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cardScheme = remember { scheme ?: ColorPalette.BenefitScheme.pickSchemeRandom() }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .customClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                role = Role.Button,
                isPressed = {}
            ),
        background = listOf(ODSColorModel(cardScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
        padding = ODSPadding(all = 0.dp)
    ) {
        ODSRow(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            gap = 0.dp
        ) {
            // Text Content Section - 70% width
            ODSColumn(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(all = DSVariables.spacingComponent4),
                gap = DSVariables.spacingComponent3,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = title,
                    style = DSTextStyles.bodyMBold,
                    color = cardScheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                // Description (subtitle) - bodySRegular
                description?.let {
                    ODSText(
                        text = it,
                        style = DSTextStyles.bodySRegular,
                        color = cardScheme.basicTextRecessive,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Timing or countdown
                ODSText(
                    text = countdownText ?: "$startTime - $endTime",
                    style = DSTextStyles.microcopyBold,
                    color = cardScheme.basicText,
                    modifier = Modifier.fillMaxWidth()
                )

                // Participant Count and Reward Row
                ODSWrap(
                    modifier = Modifier.fillMaxWidth(),
                    verticalGap = DSVariables.spacingComponent3,
                    horizontalGap = DSVariables.spacingComponent3,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSTagStatic(
                        scheme = cardScheme,
                        props = ODSTagStaticProps(
                            label = participantCount.toString(),
                            icon = ODSIconModel(
                                drawableRes = R.drawable.happy_person_type_bold_size_standard,
                                contentDescription = "Participants"
                            ),
                            type = ODSTagStaticType.STRONG
                        )
                    )

                    // Reward Tag
                    reward?.let {
                        ODSTagStatic(
                            scheme = cardScheme,
                            props = ODSTagStaticProps(
                                label = it,
                                type = ODSTagStaticType.STRONG
                            )
                        )
                    }
                }
            }

            // Image Section - 30% width, extends to right edge with no padding
            ODSBox(
                modifier = Modifier
                    .weight(0.3f),
                height = DSVariables.columns3Columns
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = ODSImageModel(url = imageUrl),
                        cornerRadius = ODSCorners(
                            topLeft = 0.dp,
                            topRight = DSVariables.radiusMedium,
                            bottomLeft = 0.dp,
                            bottomRight = DSVariables.radiusMedium
                        ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(cardScheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(
                            topLeft = 0.dp,
                            topRight = DSVariables.radiusMedium,
                            bottomLeft = 0.dp,
                            bottomRight = DSVariables.radiusMedium
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.achievement_type_standard_size_standard,
                                contentDescription = "Challenge icon",
                                tint = cardScheme.basicTextRecessive
                            ),
                            width = DSVariables.sizingComponent8,
                            height = DSVariables.sizingComponent8
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeVariantCard5Preview() {
    ChallengeVariantCard5(
        title = "FAT BURNING HIIT",
        startTime = "09:00 AM",
        endTime = "10:00 PM",
        participantCount = 890,
        reward = "+15 Points",
        imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        onClick = {}
    )
}

