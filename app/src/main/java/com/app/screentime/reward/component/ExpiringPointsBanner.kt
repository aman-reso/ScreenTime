package com.app.screentime.reward.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Expiring Points Banner Component
 * Shows notification about expiring points
 */
@Composable
fun ExpiringPointsBanner(
    expiringPoints: Int = 20,
    onUseClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    scheme: ODSTheme
) {
    ODSCardBasic(
        modifier = modifier.fillMaxWidth(),
        scheme = scheme,
        contentPadding = ODSPadding(),
        props = ODSCardBasicProps(),
        contentSlot = {
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSListRowStandardProps(
                    icon = ODSIconModel(
                        drawableRes = R.drawable.warning_type_standard,
                        tint = scheme.functionalWarningStandard
                    ),
                    showDescriptionTitle = false,
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "$expiringPoints points will expire soon, please use as soon as possible."
                )
            )
        }
    )
}
