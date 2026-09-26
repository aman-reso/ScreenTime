package com.app.screentime.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.R as ODSR

@Composable
fun NetworkErrorUi(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit = {},
    onGoBackHome: (() -> Unit)? = null,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSColumn(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ODSBox(
            width = 120.dp,
            height = 120.dp,
            cornerRadius = ODSCorners(all = 80.dp),
            contentAlignment = Alignment.Center,
            border = ODSBorder(width = 1.dp, colorList = listOf(ODSColorModel(scheme.basicStroke)))
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = ODSR.drawable.ic_heart_crack,
                    tint = scheme.functionalDestructiveStandard
                ),
                width = 64.dp,
                height = 64.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connection Lost Title
        ODSText(
            text = "Connection Lost",
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description Body
        ODSText(
            text = "We couldn't reach the matches feed. Please verify your connection or try again.",
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error Code Text
        ODSText(
            text = "ERR_CODE: API_RETRY_FAILED_TIMEOUT",
            style = DSTextStyles.microcopyBold,
            color = scheme.basicTextRecessive,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        ODSButton(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSButtonProps(
                label = "Try Again",
                variant = ODSButtonVariant.PRIMARY
            ),
            onClick = onTryAgain
        )

        if (onGoBackHome != null) {
            Spacer(modifier = Modifier.height(12.dp))
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Go Back Home",
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.OUTLINE
                ),
                onClick = onGoBackHome
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun NetworkErrorUiPreview() {
    NetworkErrorUi(
        onTryAgain = {},
        onGoBackHome = {}
    )
}
