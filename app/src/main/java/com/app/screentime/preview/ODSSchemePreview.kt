package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.allSchemes

/**
 * Preview screen showing all ODS schemes in boxes with their background colors.
 * Each scheme is displayed in a box with its basicBackground color.
 */
@Composable
fun ODSSchemePreview(
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = neutralScheme.basicBackground))
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(
                horizontal = DSVariables.spacingComponent3,
                vertical = DSVariables.spacingComponent3
            ),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
            verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
        ) {
            items(allSchemes) { scheme ->
                SchemeBox(scheme = scheme)
            }
        }
    }
}

@Composable
private fun SchemeBox(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
        cornerRadius = ODSCorners(all = DSVariables.radiusMedium),
        border = ODSBorder(
            width = DSVariables.strokes1,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        ),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxSize(),
            gap = DSVariables.spacingComponent2,
            verticalAlignment = Alignment.Top
        ) {
            // Scheme Name
            ODSText(
                text = scheme.name,
                style = DSTextStyles.subtitle,
                color = scheme.basicText,
                modifier = Modifier.fillMaxWidth()
            )

            // Background Color Info
            ODSText(
                text = "Background",
                style = DSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )

            // Color Swatch
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
                cornerRadius = ODSCorners(all = DSVariables.radiusSmall)
            ) {}

            // Accent Color Info
            ODSText(
                text = "Accent",
                style = DSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )

            // Accent Color Swatch
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                cornerRadius = ODSCorners(all = DSVariables.radiusSmall)
            ) {}
        }
    }
}

@Preview(showBackground = true, name = "ODS Schemes Preview")
@Composable
private fun ODSSchemePreviewPreview() {
    ODSSchemePreview()
}

