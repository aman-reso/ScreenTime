package com.app.screentime.feature.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Compact summary card displayed for previously completed preference steps.
 * 100% styled via scheme tokens using ConstraintLayout:
 * - Background: scheme.basicBackgroundCard
 * - Corners: 16.dp
 * - Label: scheme.basicText (left aligned)
 * - Value: scheme.basicTextRecessive (right aligned next to edit icon)
 * - Edit Icon: scheme.basicText (right aligned at end right)
 */
@Composable
fun CompletedStepSummaryCard(
    label: String,
    value: String,
    onEdit: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onEdit
            ),
        padding = ODSPadding(all = 16.dp),
        cornerRadius = ODSCorners(all = 16.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (labelRef, valueRef, iconRef) = createRefs()

            ODSText(
                text = label,
                style = ODSTextStyles.bodySBold,
                color = scheme.basicText,
                modifier = Modifier.constrainAs(labelRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )

            ODSBox(
                modifier = Modifier
                    .size(20.dp)
                    .constrainAs(iconRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_edit_3,
                        contentDescription = "Edit $label"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(16.dp)
                )
            }

            ODSText(
                text = value,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.End,
                modifier = Modifier.constrainAs(valueRef) {
                    end.linkTo(iconRef.start, margin = 8.dp)
                    start.linkTo(labelRef.end, margin = 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}
