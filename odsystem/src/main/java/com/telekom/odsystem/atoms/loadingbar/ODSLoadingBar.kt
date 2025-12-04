package com.telekom.odsystem.atoms.loadingbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.foundations.offset
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSLoadingBar composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 */
@Composable
fun ODSLoadingBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {

    val style = ODSLoadingBarStyle().getStyle(scheme = scheme)

    ODSBox(
        modifier = modifier,
        clipContent = style.zStackClipContent != false,
        contentAlignment = style.zStackContentAlignment
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = style.cornerRadius,
            clipContent = style.clipContent != false,
            verticalAlignment = style.verticalAlignment,
            horizontalAlignment = style.horizontalAlignment,
            verticalArrangement = style.verticalArrangement
        ) {
        }

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = style.strokeAbsoluteContentAlignment ?: Alignment.TopStart)
                .offset(offset = style.strokeAbsoluteOffset)
                .height(style.strokeHeight ?: Dp.Unspecified),
            color = style.strokeBackground?.get(0)?.hexColor?.getColor() ?: Color.Transparent,
            strokeCap = StrokeCap.Round,
        )
    }
}
