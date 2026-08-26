package com.app.screentime.molecule.featurecard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.MIN_HEIGHT
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.customClickable
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSFeatureCard composable - A wrapper component above ODS components for displaying feature cards.
 *
 * This component displays a card with:
 * - Icon/image (circular) on the left
 * - Subtitle label (smaller, grey) above title
 * - Title (larger, bold, black)
 * - Right arrow icon for navigation
 * - Wavy background decoration
 * - Clickable interaction
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onClick Callback triggered when card is clicked.
 */
@Composable
fun ODSFeatureCard(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFeatureCardProps = ODSFeatureCardProps(title = ""),
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val style = ODSFeatureCardStyle().getStyle(
        scheme = neutralScheme,
        props = props,
        state = when {
            isPressed -> ODSActions.PRESSED
            isHovered -> ODSActions.HOVERED
            else -> ODSActions.DEFAULT
        }
    )

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        minHeight = style.minHeight ?: MIN_HEIGHT.dp
    ) {
        // Main Card Content
        ODSRow(
            modifier = Modifier
                .fillMaxWidth().wrapContentHeight()
                .customClickable(
                    interactionSource = interactionSource,
                    onClick = onClick,
                    isPressed = { isPressed = it },
                    role = Role.Button
                ),
            padding = style.padding,
            cornerRadius = style.cornerRadius,
            horizontalArrangement = style.contentHorizontalArrangement,
            verticalAlignment = style.contentVerticalAlignment,
            background = style.background,
            clipContent = true
        ) {
            // Content Row
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = style.contentGap,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon Container
                if (props.iconImage != null) {
                    ODSRow(
                        width = style.iconContainerSize,
                        height = style.iconContainerSize,
                        cornerRadius = style.iconCornerRadius,
                        clipContent = true,
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSIcon(
                            tint = style.arrowIconColor?.getColor(),
                            iconModel = props.iconImage,
                            width = style.iconContainerSize,
                            height = style.iconContainerSize,
                        )
                    }
                }

                // Text Content Column
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = style.textContentGap,
                    verticalArrangement = style.textContentVerticalArrangement,
                    horizontalAlignment = Alignment.Start
                ) {
                    if (props.title.isNotEmpty()) {
                        ODSText(
                            text = props.title,
                            style = style.titleStyle,
                            color = style.titleColor
                        )
                    }

                    if (!props.subtitle.isNullOrEmpty()) {
                        ODSText(
                            text = props.subtitle,
                            style = style.subtitleStyle,
                            color = style.subtitleColor
                        )
                    }
                }

                if (props.showArrow) {
                    val arrowIconModel = props.arrowIcon ?: ODSIconModel(
                        drawableRes = R.drawable.arrow_right_type_standard_size_standard
                    )
                    ODSIcon(
                        iconModel = arrowIconModel,
                        width = style.arrowIconSize,
                        height = style.arrowIconSize,
                        tint = style.arrowIconColor?.getColor()
                    )
                }
            }
        }
    }
}
