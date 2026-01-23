package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.filemanager.repository.FileManagerRepository.StorageInfo
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun StorageInfoView(
    storageInfo: StorageInfo,
    scheme: ODSTheme
) {
    val totalGB = storageInfo.totalBytes / (1024.0 * 1024.0 * 1024.0)
    val usedGB = storageInfo.usedBytes / (1024.0 * 1024.0 * 1024.0)
    val availableGB = storageInfo.availableBytes / (1024.0 * 1024.0 * 1024.0)
    val usedPercentage =
        (storageInfo.usedBytes.toDouble() / storageInfo.totalBytes.toDouble() * 100).toInt()

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent2
            ),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = DSVariables.spacingComponent2
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSText(
                    text = stringResource(R.string.storage),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = stringResource(R.string.storage_used_total, usedGB, totalGB),
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }

            // Progress bar representation
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                background = listOf(ODSColorModel(scheme.basicTextRecessive)),
                cornerRadius = ODSCorners(all = 4.dp)
            ) {
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth(usedPercentage / 100f)
                        .fillMaxHeight(),
                    background = listOf(ODSColorModel(scheme.functionalSuccessStandard)),
                    cornerRadius = ODSCorners(all = 4.dp)
                ) {}
            }

            ODSText(
                text = stringResource(R.string.storage_available, availableGB),
                style = DSTextStyles.oxMicrocopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

