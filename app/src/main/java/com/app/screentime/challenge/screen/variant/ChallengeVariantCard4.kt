package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import com.telekom.odsystem.neutralScheme

/**
 * Challenge Variant Card 4 - "7x4 CHALLENGE" style
 * ODS scheme background, "NEW" tag, title, start time, end time, participant count, reward, image on right
 */
@Composable
fun ChallengeVariantCard4(
    tag: String? = "NEW",
    challengeType: String,
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
    scheme: ODSTheme = neutralScheme
) {
    val interactionSource = remember { MutableInteractionSource() }
    val cardScheme = remember { scheme }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .customClickable(
                interactionSource = interactionSource,
                onClick = onClick,
                role = Role.Button,
                isPressed = {}
            ),
        background = listOf(ODSColorModel(cardScheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
        padding = ODSPadding(
            top = DSVariables.spacingComponent5,
            left = DSVariables.spacingComponent4
        )
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (titleRef, descriptionRef, contentColumnRef, imageRef) = createRefs()
            val guideline = createGuidelineFromStart(0.5f)

            ODSText(
                text = title,
                style = DSTextStyles.bodyMBold,
                color = cardScheme.basicText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )

            // Description (subtitle) - bodySRegular
            description?.let {
                ODSText(
                    text = it,
                    style = DSTextStyles.bodySRegular,
                    color = cardScheme.basicTextRecessive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(descriptionRef) {
                            start.linkTo(parent.start)
                            top.linkTo(titleRef.bottom, margin = DSVariables.spacingComponent2)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }
                )
            }

            // Content Column - left side, center aligned vertically
            val contentTopRef = if (description != null) descriptionRef else titleRef
            ODSColumn(
                modifier = Modifier.constrainAs(contentColumnRef) {
                    start.linkTo(parent.start)
                    top.linkTo(contentTopRef.bottom, margin = DSVariables.spacingComponent2)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    width = Dimension.fillToConstraints
                    verticalBias = 0.5f
                },
                gap = DSVariables.spacingComponent3,
                horizontalAlignment = Alignment.Start
            ) {
                // Timing or countdown
                ODSText(
                    text = countdownText ?: "$startTime - $endTime",
                    style = DSTextStyles.microcopyBold,
                    color = cardScheme.basicText,
                )

                // Tag
                tag?.let {
                    ODSTagStatic(
                        scheme = scheme,
                        props = ODSTagStaticProps(
                            label = it,
                            type = ODSTagStaticType.STRONG
                        )
                    )
                }
            }

            // Image Section - 50% width, below title/subtitle
            ODSBox(
                height = DSVariables.sizingComponent18,
                modifier = Modifier.constrainAs(imageRef) {
                    start.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    top.linkTo(contentTopRef.bottom, margin = DSVariables.spacingComponent2)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = ODSImageModel(url = imageUrl),
                        cornerRadius = ODSCorners(
                            topLeft = 0.dp,
                            topRight = 0.dp,
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
                            topRight = 0.dp,
                            bottomLeft = 0.dp,
                            bottomRight = DSVariables.radiusMedium
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.achievement_type_standard_size_standard,
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
fun ChallengeVariantCard4Preview() {
    ChallengeVariantCard4(
        tag = "NEW",
        challengeType = "7x4 CHALLENGE",
        title = "FULL BODY WORKOUT",
        startTime = "09:00 AM",
        endTime = "10:00 PM",
        participantCount = 567,
        reward = "+15 Points",
        imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        onClick = {}
    )
}

