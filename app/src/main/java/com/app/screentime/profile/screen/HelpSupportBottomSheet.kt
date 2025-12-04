package com.app.screentime.profile.screen

import android.content.Intent
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportBottomSheetContent(
    onDismiss: () -> Unit = {},
    scheme: ODSTheme = neutralScheme
) {
    val context = LocalContext.current

    val supportEmail = "help.testmate@gmail.com"

    ODSBottomSheet(
        showBottomSheet = true,
        onDismissRequest = onDismiss,
        titleSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent2
            ) {
                ODSText(
                    text = stringResource(R.string.help_support),
                    style = DSTextStyles.titleM,
                    color = scheme.basicText
                )
                ODSText(
                    text = stringResource(R.string.help_support_description),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )
            }
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ODSCardBasic(contentSlot = {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.Email,
                                tint = scheme.functionalSuccessStandard,
                                contentDescription = "Email"
                            ),
                            width = DSVariables.sizingComponent10,
                            height = DSVariables.sizingComponent10
                        )
                        ODSColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            ODSText(
                                text = stringResource(R.string.email_support),
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.basicText
                            )
                            ODSBox(height = DSVariables.spacingComponent1) {}
                            ODSText(
                                text = supportEmail,
                                style = DSTextStyles.bodyMBold,
                                color = scheme.functionalSuccessStandard
                            )
                        }
                    }
                }, onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
                        putExtra(Intent.EXTRA_SUBJECT, "AppTime Support Request")
                    }
                    try {
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Send email via"
                            )
                        )
                    } catch (e: Exception) {
                        // No email client available
                    }
                }, scheme = scheme)
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        onCloseClicked = onDismiss
    )
}
