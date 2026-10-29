package com.app.screentime.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Title alignment options for [GenericHeader].
 */
enum class HeaderTitleAlignment {
    START,
    CENTER
}

/**
 * Generic configuration properties for [GenericHeader].
 *
 * @property title Header title text.
 * @property showBackArrow Whether to display the left back navigation button.
 * @property onBackClick Callback when the back arrow is clicked.
 * @property titleAlignment Alignment of the title ([HeaderTitleAlignment.START] or [HeaderTitleAlignment.CENTER]).
 * @property showRightIcon Whether to display the right action icon button.
 * @property rightIconRes Drawable resource for the right icon.
 * @property rightIconDescription Content description for accessibility.
 * @property onRightIconClick Callback when the right icon is clicked.
 * @property height Height of the header bar (defaults to 56.dp).
 */
data class GenericHeaderProps(
    val title: String,
    val showBackArrow: Boolean = true,
    val onBackClick: () -> Unit = {},
    val titleAlignment: HeaderTitleAlignment = HeaderTitleAlignment.CENTER,
    val showRightIcon: Boolean = false,
    val rightIconRes: Int = ODSR.drawable.ic_settings,
    val rightIconDescription: String? = null,
    val onRightIconClick: () -> Unit = {},
    val rightContent: (@Composable () -> Unit)? = null,
    val height: Dp = 42.dp
)

/**
 * Reusable, generic app header built 100% with Telekom ODS components.
 *
 * Adheres strictly to project rules:
 * - 100% ODS components (ODSBox, ODSRow, ODSText, ODSIcon).
 * - No raw Compose Text, Box, Row, or Icon.
 * - Colors derived from [scheme: ODSTheme].
 * - Spacing and sizing tokens from [ODSVariables].
 * - Max typography size 16sp ([ODSTextStyles.bodyMBold]).
 */
@Composable
fun GenericHeader(
    props: GenericHeaderProps,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(props.height),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(props.height)
                .padding(
                    start = ODSVariables.spacingLayout1,
                    end = ODSVariables.spacingLayout1
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ── Left: Back Arrow Button or Symmetrical Spacer ───────────────
            if (props.showBackArrow) {
                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent10)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = props.onBackClick
                        ),
                    contentAlignment = Alignment.Center,
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = ODSR.drawable.ic_arrow_left,
                            contentDescription = "Back"
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }
            } else if (props.titleAlignment == HeaderTitleAlignment.CENTER) {
                // Invisible symmetrical counterweight for centered titles
                ODSBox(
                    modifier = Modifier.size(ODSVariables.sizingComponent10)
                )
            }

            // ── Center / Start: Title Text ─────────────────────────────────
            val textAlignment = when (props.titleAlignment) {
                HeaderTitleAlignment.START -> TextAlign.Start
                HeaderTitleAlignment.CENTER -> TextAlign.Center
            }

            val titleModifier = if (props.titleAlignment == HeaderTitleAlignment.CENTER) {
                Modifier
                    .weight(1f)
                    .padding(horizontal = ODSVariables.spacingComponent2)
            } else {
                Modifier
                    .weight(1f)
                    .padding(start = if (props.showBackArrow) ODSVariables.spacingComponent2 else 0.dp)
            }

            ODSText(
                modifier = titleModifier,
                text = props.title,
                style = com.telekom.odsystem.foundations.ODSTextStyle(
                    fontFamily = ODSR.font.funnelsans_bold,
                    fontSize = 18,
                    lineHeight = 24
                ),
                color = scheme.basicText,
                textAlign = textAlignment,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // ── Right: Custom Right Content, Action Icon Button, or Symmetrical Spacer
            if (props.rightContent != null) {
                ODSBox(
                    modifier = Modifier.size(ODSVariables.sizingComponent10),
                    contentAlignment = Alignment.Center
                ) {
                    props.rightContent.invoke()
                }
            } else if (props.showRightIcon) {
                ODSBox(
                    modifier = Modifier
                        .size(ODSVariables.sizingComponent10)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = props.onRightIconClick
                        ),
                    contentAlignment = Alignment.Center,
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull)
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = props.rightIconRes,
                            contentDescription = props.rightIconDescription
                        ),
                        tint = scheme.basicText.getColor(),
                        modifier = Modifier.size(ODSVariables.sizingComponent8)
                    )
                }
            } else if (props.titleAlignment == HeaderTitleAlignment.CENTER) {
                // Invisible symmetrical counterweight for centered titles
                ODSBox(
                    modifier = Modifier.size(ODSVariables.sizingComponent10)
                )
            }
        }
    }
}
