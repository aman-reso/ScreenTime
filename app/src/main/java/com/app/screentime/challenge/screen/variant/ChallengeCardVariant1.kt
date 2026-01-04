package com.app.screentime.challenge.screen.variant

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme

@Composable
fun CurvedCryptoCard(
    title: String,
    subtitle: String? = null,
    date: String,
    tags: List<String> = emptyList(),
    imageUrl: String? = null,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = lagoonSecondaryScheme
) {
    val interactionSource = remember { MutableInteractionSource() }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(CurvedRoundedCardShape())
            .customClickable(
                interactionSource = interactionSource,
                onClick = {},
                role = Role.Button,
                isPressed = {}
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 0.dp), // Using custom shape instead
        padding = ODSPadding(all = DSVariables.spacingComponent4)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (contentColumnRef, imageRef) = createRefs()
            val guideline = createGuidelineFromStart(0.7f)

            // Content Column - left section (0.7f)
            ODSColumn(
                modifier = Modifier.constrainAs(contentColumnRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                gap = DSVariables.spacingComponent3
            ) {
                // Title at top - subtle
                ODSText(
                    text = title,
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle below title
                subtitle?.let {
                    ODSText(
                        text = it,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                ODSText(
                    text = date,
                    style = DSTextStyles.microcopyBold,
                    color = scheme.basicTextRecessive
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tags at bottom
                if (tags.isNotEmpty()) {
                    ODSWrap(
                        modifier = Modifier.fillMaxWidth(),
                        verticalGap = DSVariables.spacingComponent3,
                        horizontalGap = DSVariables.spacingComponent3,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        tags.forEach { tag ->
                            ODSTagStatic(
                                scheme = scheme,
                                props = ODSTagStaticProps(
                                    label = tag,
                                    type = ODSTagStaticType.PROMOTION
                                )
                            )
                        }
                    }
                }
            }

            // Image Section - right section (0.3f)
            ODSBox(
                modifier = Modifier.constrainAs(imageRef) {
                    start.linkTo(guideline, margin = DSVariables.spacingComponent4)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
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
                        background = listOf(ODSColorModel(scheme.basicBackgroundSubtle)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = com.telekom.odsystem.R.drawable.achievement_type_standard_size_standard,
                                contentDescription = "Challenge icon",
                                tint = scheme.basicTextRecessive
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

class CurvedRoundedCardShape(
    private val curveHeight: Float = 16f,
    private val cornerRadius: Float = 16f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val w = size.width
        val h = size.height
        val r = cornerRadius
        val c = curveHeight

        val path = Path().apply {

            // ─── Start: Top-Left (after corner)
            moveTo(r, 0f)

            // Top outward curve
            quadraticBezierTo(
                w / 2f,
                c,
                w - r,
                0f
            )

            // Top-Right corner
            arcTo(
                rect = Rect(w - 2 * r, 0f, w, 2 * r),
                startAngleDegrees = -90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Right side
            lineTo(w, h - r)

            // Bottom-Right corner
            arcTo(
                rect = Rect(w - 2 * r, h - 2 * r, w, h),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom outward curve
            quadraticBezierTo(
                w / 2f,
                h - c,
                r,
                h
            )

            // Bottom-Left corner
            arcTo(
                rect = Rect(0f, h - 2 * r, 2 * r, h),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Left side
            lineTo(0f, r)

            // Top-Left corner
            arcTo(
                rect = Rect(0f, 0f, 2 * r, 2 * r),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            close()
        }

        return Outline.Generic(path)
    }
}


@Preview(showBackground = true)
@Composable
fun CurvedCryptoCardPreview() {
    ODSBox(
        modifier = Modifier
            .wrapContentHeight(),
        background = listOf(ODSColorModel(neutralScheme.basicBackgroundSubtle))
    ) {
        CurvedCryptoCard(
            title = "Solana",
            subtitle = "A high-performance blockchain supporting builders around the world",
            date = "Dec 15, 2024",
            tags = listOf("Crypto", "Blockchain", "DeFi"),
            imageUrl = "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI"
        )
    }
}
