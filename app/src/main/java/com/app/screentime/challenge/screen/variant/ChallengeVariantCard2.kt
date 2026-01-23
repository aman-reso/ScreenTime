package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.ui.theme.ColorPalette
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme

/**
 * Challenge Variant Card 2 - "Partner League" style
 * Image on left (60dp height), title on right, date below title, description, ODS Tag static
 */
@Composable
fun ChallengeVariantCard2(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    date: String,
    countdownText: String? = null,
    tag: String? = null,
    reward: String? = null,
    imageUrl: String? = null,
    onClick: () -> Unit = {},
    cardScheme: ODSTheme
) {
    val interactionSource = remember { MutableInteractionSource() }

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
        padding = ODSPadding(all = DSVariables.spacingComponent4)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (imageContainerRef, titleRef, descriptionRef, dateRef, tagRef, _) = createRefs()
            val imageGuideline = createGuidelineFromStart(0.25f)

            ODSBox(
                modifier = Modifier.constrainAs(imageContainerRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(imageGuideline, margin = DSVariables.spacingComponent4)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(DSVariables.sizingComponent17)
                },
                background = listOf(ODSColorModel(HexColor(0x90EE90))), // Light green
                cornerRadius = ODSCorners(all = DSVariables.radiusSmall)
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = ODSImageModel(url = imageUrl),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(HexColor(0x90EE90))),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
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

            ODSText(
                text = title,
                style = DSTextStyles.bodyMBold,
                color = cardScheme.basicText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(imageGuideline, margin = DSVariables.spacingComponent4)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            // Description (subtitle) below title - bodySRegular, max 2 lines
            description?.let {
                ODSText(
                    text = it,
                    style = DSTextStyles.bodySRegular,
                    color = cardScheme.basicTextRecessive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(descriptionRef) {
                        start.linkTo(imageGuideline, margin = DSVariables.spacingComponent4)
                        top.linkTo(titleRef.bottom, margin = DSVariables.spacingComponent3)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                )
            }

            // Date or countdown below description
            val dateTopRef = if (description != null) descriptionRef else titleRef
            ODSText(
                text = countdownText ?: date,
                style = DSTextStyles.microcopyBold,
                color = cardScheme.basicText,
                modifier = Modifier.constrainAs(dateRef) {
                    start.linkTo(imageGuideline, margin = DSVariables.spacingComponent4)
                    top.linkTo(dateTopRef.bottom, margin = DSVariables.spacingComponent3)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            // Tag below date
            tag?.let {
                ODSRow(
                    modifier = Modifier.constrainAs(tagRef) {
                        start.linkTo(imageGuideline, margin = DSVariables.spacingComponent4)
                        top.linkTo(dateRef.bottom, margin = DSVariables.spacingComponent3)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                ) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeVariantCard2Preview() {
    ChallengeVariantCard2(
        title = "Partner League",
        date = "26 Jan",
        description = "Join the ultimate partner challenge and compete with teams from around the world",
        tag = "NEW",
        imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        onClick = {},
        cardScheme = lagoonSecondaryScheme
    )
}

