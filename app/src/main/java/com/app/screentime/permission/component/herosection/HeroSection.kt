package com.app.screentime.permission.component.herosection

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * HeroSection component displaying animated icon and title.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme ODS theme scheme for styling.
 * @param props Configuration properties for the component.
 * @param primaryColorHex Primary color for the icon and diamond background.
 */
@Composable
fun HeroSection(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: HeroSectionProps = HeroSectionProps()
) {
    val style = remember(scheme) {
        HeroSectionStyle().getStyle(scheme)
    }
    val tokens = defaultHeroSectionTokens

    ODSColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (props.showIcon) {
            AnimatedIcon(
                iconTint = style.iconTint,
                tokens = tokens
            )
            ODSBox(height = tokens.titleSpacing) {}
        }

        ODSText(
            text = props.title,
            style = DSTextStyles.titleS,
            color = style.titleColor ?: scheme.basicText
        )
    }
}

@Composable
private fun AnimatedIcon(
    iconTint: HexColor?,
    tokens: HeroSectionTokens
) {
    if (iconTint == null) return
    ODSBox(
        modifier = Modifier,
        width = tokens.diamondSize,
        height = tokens.diamondSize,
        contentAlignment = Alignment.Center
    ) {
        // Animated diamond background
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = tokens.scaleMin,
            targetValue = tokens.scaleMax,
            animationSpec = infiniteRepeatable(
                animation = tween(tokens.animationDuration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        ODSBox(
            modifier = Modifier,
            width = tokens.diamondSize,
            height = tokens.diamondSize,
            rotate = 45f,
            scale = scale,
            background = listOf(
                com.telekom.odsystem.foundations.ODSColorModel(
                    gradient = com.telekom.odsystem.foundations.ODSLinearGradientModel(
                        Pair(0f, HexColor("FF0080")),
                        Pair(0.5f, HexColor("7928CA")),
                        Pair(1f, HexColor("FF0080")),
                        angleInDegrees = 0f
                    )
                )
            ),
            cornerRadius = ODSCorners(all = DSVariables.radiusExtraExtraLarge),
            contentAlignment = Alignment.Center
        ) {}

        // Floating shield icon
        val floatTransition = rememberInfiniteTransition(label = "float")
        val floatY by floatTransition.animateFloat(
            initialValue = tokens.floatMin,
            targetValue = tokens.floatMax,
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "float"
        )

        ODSColumn(
            modifier = Modifier
                .graphicsLayer {
                    translationY = floatY
                },
            width = tokens.iconContainerSize,
            height = tokens.iconContainerSize,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Default.Security,
                    tint = iconTint
                ),
                width = tokens.iconSize,
                height = tokens.iconSize
            )
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Default.Add,
                    tint = iconTint
                ),
                width = DSVariables.sizingComponent8,
                height = DSVariables.sizingComponent8
            )
        }
    }
}

