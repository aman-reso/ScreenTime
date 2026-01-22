package com.app.screentime.reward.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.atoms.icon.ODSIconModel
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
                        drawableRes = com.telekom.odsystem.R.drawable.warning_type_standard,
                        tint = scheme.functionalWarningStandard
                    ),
                    showDescriptionTitle = false,
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = stringResource(R.string.expiring_coins_warning, expiringPoints)
                )
            )
        }
    )
}
