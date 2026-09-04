package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
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
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.textfield.ODSTextFieldSize
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
fun ModelVerificationBottomSheet(
    initialName: String,
    initialAge: Int,
    initialCountry: String,
    scheme: ODSTheme,
    onSubmit: (name: String, age: Int, country: String, photoUrl: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var ageText by remember { mutableStateOf(if (initialAge > 0) initialAge.toString() else "22") }
    var country by remember { mutableStateOf(initialCountry.ifBlank { "India" }) }

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
                    largeHeading = "Creator Verification",
                    subtitle = "Fill in your basic details to start receiving calls",
                    size = ODSBottomSheetHeaderSize.LARGE
                )
            )
        },
        contentSlot = {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 14.dp
            ) {
                // 1. Full Name Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        size = ODSTextFieldSize.SMALL,
                        label = "Creator / Stage Name",
                        placeholderText = "Enter your display name",
                        inputText = name,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Person)
                    ),
                    onValueChange = { name = it }
                )

                // 2. Age Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        size = ODSTextFieldSize.SMALL,
                        label = "Age",
                        placeholderText = "Enter your age (18+)",
                        inputText = ageText,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.CalendarToday)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { ageText = it }
                )

                // 3. Country Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        size = ODSTextFieldSize.SMALL,
                        label = "Country",
                        placeholderText = "e.g. India, Indonesia, USA",
                        inputText = country,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Flag)
                    ),
                    onValueChange = { country = it }
                )

                // 4. Photo Section (Optional / Skip for now)
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = 12.dp),
                    border = ODSBorder(
                        width = 1.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ),
                    padding = ODSPadding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 10.dp
                        ) {
                            ODSBox(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape),
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
                                contentAlignment = Alignment.Center
                            ) {
                                ODSIcon(
                                    iconModel = ODSIconModel(imageVector = Icons.Outlined.AddAPhoto),
                                    tint = scheme.functionalDestructiveStandard.getColor()
                                )
                            }
                            ODSColumn(gap = 2.dp) {
                                ODSText(
                                    text = "Profile Photo",
                                    style = ODSTextStyles.bodySBold,
                                    color = scheme.basicText
                                )
                                ODSText(
                                    text = "Optional · Skip for now",
                                    style = ODSTextStyles.microcopyRegular,
                                    color = scheme.basicTextRecessive
                                )
                            }
                        }

                        ODSText(
                            text = "Skipped ✓",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicAccent
                        )
                    }
                }
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                gap = 12.dp
            ) {
                ODSButton(
                    modifier = Modifier.weight(1f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Cancel",
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onDismiss
                )
                ODSButton(
                    modifier = Modifier.weight(1.5f),
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Submit Verification",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = {
                        val parsedAge = ageText.toIntOrNull() ?: 22
                        onSubmit(name, parsedAge, country, null)
                    }
                )
            }
        }
    )
}
