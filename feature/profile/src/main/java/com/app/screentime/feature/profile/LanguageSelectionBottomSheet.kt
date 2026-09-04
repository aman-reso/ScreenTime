package com.app.screentime.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheet
import com.telekom.odsystem.molecules.bottomsheet.ODSBottomSheetProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeader
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderProps
import com.telekom.odsystem.slots.bottomsheetheader.ODSBottomSheetHeaderSize
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBottomSheet(
    selectedLanguage: String,
    scheme: ODSTheme,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages =
        listOf("English", "हिन्दी (Hindi)", "Español", "Français", "Deutsch", "Bahasa Indonesia")

    ODSBottomSheet(
        scheme = scheme,
        showBottomSheet = true,
        props = ODSBottomSheetProps(showHandle = true),
        onDismissRequest = onDismiss,
        onCloseClicked = onDismiss,
        titleSlot = {
            ODSBottomSheetHeader(
                scheme = scheme,
                props = ODSBottomSheetHeaderProps(
                    smallHeading = "Choose Language",
                    subtitle = "Select your preferred app language",
                    size = ODSBottomSheetHeaderSize.SMALL
                )
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                gap = 10.dp
            ) {
                languages.forEach { lang ->
                    val isSelected = selectedLanguage == lang
                    ODSBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(lang)
                                onDismiss()
                            },
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicBackground else scheme.basicBackgroundCard
                            )
                        ),
                        cornerRadius = ODSCorners(all = 12.dp),
                        border = ODSBorder(
                            width = 1.dp,
                            colorList = listOf(
                                ODSColorModel(
                                    hexColor = if (isSelected) scheme.basicAccent else scheme.basicStrokeSubtle
                                )
                            )
                        ),
                        padding = ODSPadding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ODSText(
                                text = lang,
                                style = if (isSelected) ODSTextStyles.bodySBold else ODSTextStyles.bodySRegular,
                                color = if (isSelected) scheme.basicText else scheme.basicTextRecessive
                            )
                            if (isSelected) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Default.Check),
                                    tint = scheme.basicAccent.getColor()
                                )
                            }
                        }
                    }
                }
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Close",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onDismiss
                )
            }
        }
    )
}
