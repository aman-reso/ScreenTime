package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.neutralScheme

/**
 * Challenge Variant Card 6 - Top image, title, description, timing, tags
 * Image at top (100dp), title, description (max 2 lines), timing, tags
 */
@Composable
fun ChallengeVariantCard6(
    title: String,
    description: String? = null,
    startTime: String,
    endTime: String,
    tags: List<String> = emptyList(),
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
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 0.dp
        ) {
            // Top Image - 100dp height
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DSVariables.columns3Columns)
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    ODSImage(
                        modifier = Modifier.fillMaxSize(),
                        imageModel = ODSImageModel(url = imageUrl),
                        cornerRadius = ODSCorners(
                            topLeft = DSVariables.radiusMedium,
                            topRight = DSVariables.radiusMedium,
                            bottomLeft = 0.dp,
                            bottomRight = 0.dp
                        ),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ODSBox(
                        modifier = Modifier.fillMaxSize(),
                        background = listOf(ODSColorModel(cardScheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(
                            topLeft = DSVariables.radiusMedium,
                            topRight = DSVariables.radiusMedium,
                            bottomLeft = 0.dp,
                            bottomRight = 0.dp
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

            // Content Section
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3,
                padding = ODSPadding(
                    top = DSVariables.spacingComponent4,
                    bottom = DSVariables.spacingComponent4,
                    left = DSVariables.spacingComponent4,
                    right = DSVariables.spacingComponent4
                )
            ) {
                // Title
                ODSText(
                    text = title,
                    style = DSTextStyles.bodyMBold,
                    color = cardScheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                // Description (subtitle) - bodySRegular, max 2 lines
                description?.let {
                    ODSText(
                        text = it,
                        style = DSTextStyles.bodySRegular,
                        color = cardScheme.basicTextRecessive,
                        maxLines = 2,
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

                // Tags
                if (tags.isNotEmpty()) {
                    ODSWrap(
                        modifier = Modifier.fillMaxWidth(),
                        verticalGap = DSVariables.spacingComponent3,
                        horizontalGap = DSVariables.spacingComponent3,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        tags.forEach { tag ->
                            ODSTagStatic(
                                scheme = cardScheme,
                                props = ODSTagStaticProps(
                                    label = tag,
                                    type = ODSTagStaticType.STRONG
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeVariantCard6Preview() {
    ChallengeVariantCard6(
        title = "Movies",
        description = "From timeless classics to the latest releases, test your knowledge of the silver screen.",
        startTime = "09:00 AM",
        endTime = "10:00 PM",
        tags = listOf("NEW", "POPULAR"),
        imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI",
        onClick = {}
    )
}

