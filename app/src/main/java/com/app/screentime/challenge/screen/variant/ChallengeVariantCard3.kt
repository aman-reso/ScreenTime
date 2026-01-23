package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.ui.theme.ColorPalette
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.neutralScheme

/**
 * Challenge Variant Card 3 - "Movies" style
 * ODS scheme background, title, description, button at bottom, image on right
 */
@Composable
fun ChallengeVariantCard3(
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
            top = DSVariables.spacingComponent4,
            left = DSVariables.spacingComponent4,
            right = DSVariables.spacingComponent4,
            bottom = DSVariables.spacingComponent4
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (titleRef, descriptionRef, timingRef, buttonRef, imageRef) = createRefs()
            val guideline = createGuidelineFromStart(0.7f) // 70% for content, 30% for image

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
                        end.linkTo(guideline, margin = DSVariables.spacingComponent4)
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
                            end.linkTo(guideline, margin = DSVariables.spacingComponent4)
                            width = Dimension.fillToConstraints
                        }
                )
            }

            // Timing or countdown
            val timingTopRef = if (description != null) descriptionRef else titleRef
            ODSText(
                text = countdownText ?: "$startTime - $endTime",
                style = DSTextStyles.microcopyBold,
                color = cardScheme.basicTextRecessive,
                modifier = Modifier.constrainAs(timingRef) {
                    start.linkTo(parent.start)
                    top.linkTo(timingTopRef.bottom, margin = DSVariables.spacingComponent3)
                    end.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    width = Dimension.fillToConstraints
                }
            )

            // ODSTagStatic for reward
            reward?.let {
                ODSRow(
                    modifier = Modifier.constrainAs(buttonRef) {
                        start.linkTo(parent.start)
                        top.linkTo(timingRef.bottom, margin = DSVariables.spacingComponent3)
                        bottom.linkTo(parent.bottom, margin = DSVariables.spacingComponent4)
                        end.linkTo(guideline, margin = DSVariables.spacingComponent4)
                        width = Dimension.fillToConstraints
                    },
                    gap = DSVariables.spacingComponent1,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSTagStatic(
                        scheme = scheme,
                        props = ODSTagStaticProps(
                            label = it,
                            type = ODSTagStaticType.STRONG
                        )
                    )
                }
            }

            // Image Section - 30% width (0.3f), full height, rounded on right, teal-green background
            ODSBox(
                height = DSVariables.sizingComponent16,
                modifier = Modifier.constrainAs(imageRef) {
                    start.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                cornerRadius = ODSCorners(
                    all = DSVariables.radiusMedium,
                )
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = ODSImageModel(url = imageUrl),
                        cornerRadius = ODSCorners(
                            all = DSVariables.radiusMedium
                        ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(cardScheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(
                            all = DSVariables.radiusMedium
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.achievement_type_standard_size_standard,
                                contentDescription = "Challenge icon",
                                tint = cardScheme.basicTextOnAccent
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
fun ChallengeVariantCard3Preview() {
    ChallengeVariantCard3(
        modifier = Modifier.wrapContentHeight(),
        title = "Movies",
        description = "From timeless classics to the latest releases, test your knowledge of the silver screen.",
        startTime = "09:00 AM",
        endTime = "10:00 PM",
        participantCount = 1234,
        reward = "+15",
        imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        onClick = {}
    )
}

