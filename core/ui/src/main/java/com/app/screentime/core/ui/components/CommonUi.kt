package com.app.screentime.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSTextStyle
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme


@Composable
fun PompiereTitle(
    text: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    style: ODSTextStyle = ODSTextStyles.pompiereDisplay,
    color: HexColor? = null,
    textAlign: TextAlign = TextAlign.Start
) {
    ODSText(
        modifier = modifier,
        text = text,
        style = style,
        color = color ?: scheme.basicText,
        textAlign = textAlign
    )
}

@Composable
fun EvermoreTopBar(
    title: String = "EVERM♥RE",
    scheme: ODSTheme = neutralScheme,
    onMenuClick: () -> Unit = {}
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSText(
            text = title,
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier.size(36.dp)
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.ic_more_vertical),
                tint = scheme.basicText.getColor()
            )
        }
    }
}

@Composable
fun OrganicBlobIllustration(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    val lavenderColor = scheme.basicAccentSecondary.getColor()
    val peachColor = scheme.basicBackgroundCardSubtle.getColor()
    val skyColor = scheme.basicStrokeSubtle.getColor()

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = lavenderColor,
                radius = size.width * 0.32f,
                center = Offset(size.width * 0.28f, size.height * 0.45f)
            )
            drawCircle(
                color = peachColor,
                radius = size.width * 0.36f,
                center = Offset(size.width * 0.72f, size.height * 0.52f)
            )
            drawCircle(
                color = skyColor,
                radius = size.width * 0.22f,
                center = Offset(size.width * 0.50f, size.height * 0.28f)
            )
        }

        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            gap = 8.dp
        ) {
            ODSText(
                text = "✦ Live Models Available",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSText(
                text = "Tap any profile to start a high-quality voice call instantly",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FeatureGridCard(
    icon: ImageVector,
    title: String,
    description: String,
    scheme: ODSTheme = neutralScheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 20.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 10.dp
        ) {
            ODSBox(
                modifier = Modifier.size(44.dp),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                cornerRadius = ODSCorners(all = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = icon),
                    tint = scheme.basicText.getColor()
                )
            }
            ODSText(
                text = title,
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSText(
                text = description,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

@Composable
fun TestimonialCard(
    avatarInitial: String,
    name: String,
    age: Int,
    rating: Float = 4.9f,
    quote: String,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 16.dp),
        padding = ODSPadding(all = 20.dp)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 12.dp
                ) {
                    ODSBox(
                        modifier = Modifier.size(46.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                        cornerRadius = ODSCorners(all = 23.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = avatarInitial,
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                    ODSColumn {
                        ODSText(
                            text = name,
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicText
                        )
                        ODSText(
                            text = "$age y.o.",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundSubtle)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    ODSText(
                        text = "★ $rating",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                }
            }
            ODSText(
                text = quote,
                style = ODSTextStyles.bodySRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}
