package com.app.screentime.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.telekom.odsystem.R as ODSR
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSTextStyle

/**
 * Winter Splash Screen (Matching Figma node-id 71-6).
 */
@Composable
fun WinterSplashScreen(
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = HexColor("#A7344D", 1.0f))),
        contentAlignment = Alignment.Center
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 12.dp,
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.app_name),
                style = ODSTextStyle(fontFamily = ODSR.font.pacifico_regular, fontSize = 54, lineHeight = 54),
                color = HexColor("#FFFFFF", 1.0f),
                textAlign = TextAlign.Center
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.splash_find_your_warmth),
                style = ODSTextStyle(fontFamily = ODSR.font.funnelsans_medium, fontSize = 14, lineHeight = 14),
                color = HexColor("#FFFFFF", 1.0f),
                textAlign = TextAlign.Center
            )
        }
    }
}
