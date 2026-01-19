package com.app.screentime.permission.component.bottombar

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.app.screentime.config.R
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * BottomBar component with primary button and legal links.
 *
 * @param modifier Modifier to be applied to the component.
 * @param scheme ODS theme scheme for styling.
 * @param props Configuration properties for the component.
 * @param onAllowClick Callback when the allow button is clicked.
 * @param onSurfaceVariantHex Color for separator text.
 */
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    props: BottomBarProps? = null,
    onAllowClick: () -> Unit,
) {
    val style = remember(scheme) {
        BottomBarStyle().getStyle(scheme)
    }
    val tokens = defaultBottomBarTokens
    val context = LocalContext.current
    val finalProps = props ?: remember { BottomBarProps.default(context) }

    ODSBox(
        modifier = modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(scheme.basicBackground)),
        padding = style.padding ?: ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent4
        )
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = style.horizontalAlignment ?: Alignment.CenterHorizontally,
            verticalArrangement = style.verticalArrangement
                ?: Arrangement.spacedBy(tokens.verticalSpacing)
        ) {
            // Allow Access Button
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSButtonProps(
                    label = finalProps.buttonLabel,
                    variant = ODSButtonVariant.SECONDARY, size = ODSButtonSize.SMALL
                ),
                onClick = onAllowClick
            )

            // Legal Links
            if (finalProps.showLegalLinks) {
                ODSRow(
                    horizontalArrangement = style.horizontalArrangement ?: Arrangement.spacedBy(
                        tokens.linkSpacing
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSLink(
                        scheme = scheme,
                        props = ODSLinkProps(
                            label = stringResource(R.string.privacy_policy)
                        ),
                        onClick = {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    finalProps.privacyPolicyUrl.toUri()
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Log.e(
                                    "BottomBar",
                                    "Error opening Privacy Policy",
                                    e
                                )
                            }
                        }
                    )

                    ODSText(
                        text = tokens.separatorText,
                        style = DSTextStyles.bodySRegular,
                        color = style.separatorColor
                    )

                    ODSLink(
                        scheme = scheme,
                        props = ODSLinkProps(
                            label = stringResource(R.string.terms_of_service)
                        ),
                        onClick = {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    finalProps.termsOfServiceUrl.toUri()
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Log.e("BottomBar", "Error opening Terms", e)
                            }
                        }
                    )
                }
            }
        }
    }
}

