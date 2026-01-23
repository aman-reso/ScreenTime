package com.app.screentime.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.permission.viewmodel.RegistrationUiState
import com.app.screentime.registrations.screen.RegistrationScreenState
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

data class RegistrationStep(
    val id: Int,
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val state: RegistrationScreenState? = null
)

@Composable
fun RegistrationStepsContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    steps: List<RegistrationStep> = emptyList(),
    onStepClick: (Int) -> Unit = {}
) {
    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = ODSPadding(
            vertical = DSVariables.spacingComponent5
        ),
        gap = DSVariables.spacingComponent3
    ) {
        steps.forEachIndexed { index, step ->
            RegistrationStepCard(
                step = step, scheme = scheme, onClick = { onStepClick(index) })
        }
    }
}

@Composable
private fun RegistrationStepCard(
    step: RegistrationStep, scheme: ODSTheme, onClick: () -> Unit
) {
    ODSCardBasic(
        modifier = Modifier.fillMaxWidth(),
        scheme = scheme,
        props = ODSCardBasicProps(isHorizontal = false),
        onClick = onClick,
        contentSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                gap = DSVariables.spacingComponent3
            ) {
                // Left Icon
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = step.icon, tint = scheme.basicText
                    ), width = 24.dp, height = 24.dp
                )
                ODSColumn(
                    modifier = Modifier.weight(1f), gap = 4.dp
                ) {
                    ODSText(
                        text = step.title, style = DSTextStyles.bodyMBold, color = scheme.basicText
                    )
                    ODSText(
                        text = step.subtitle,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // Right Complete Icon
                if (step.state == RegistrationScreenState.SUCCESS) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Filled.CheckCircle,
                            tint = scheme.functionalSuccessStandard
                        ), width = 24.dp, height = 24.dp
                    )
                } else if (step.state == RegistrationScreenState.LOADING) {
                    ODSLoadingSpinner(modifier = Modifier.size(24.dp), scheme = scheme)
                }
            }
        })
}
