package com.app.screentime.feature.discover.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Preview(showBackground = true, widthDp = 400)
@Composable
fun HomeFeedEmptyCard(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    onRefreshFeed: () -> Unit = {},
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        padding = ODSPadding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            val dashColor = scheme.basicStrokeSubtle.getColor()
            Canvas(modifier = Modifier.size(80.dp)) {
                drawCircle(
                    color = dashColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                6.dp.toPx(),
                                6.dp.toPx()
                            ), 0f
                        )
                    )
                )
            }

            Spacer(Modifier.height(24.dp))

            ODSText(
                text = "That's everyone for now!",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            ODSText(
                text = "You've successfully seen all matches matching your filters. Try expanding your age, distance, or interest criteria.",
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Refresh Feed",
                    variant = ODSButtonVariant.OUTLINE
                )
            ) {
                onRefreshFeed()
            }
        }
    }
}