package com.app.screentime.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun EditProfileDialog(
    currentName: String,
    currentEmail: String,
    currentBio: String,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, bio: String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var bio by remember { mutableStateOf(currentBio) }

    Dialog(onDismissRequest = onDismiss) {
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ),
            padding = ODSPadding(all = 20.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 14.dp
            ) {
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSText(
                        text = "Edit Profile",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Default.Close),
                            tint = scheme.basicTextRecessive.getColor()
                        )
                    }
                }

                // Name Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        size = ODSTextFieldSize.SMALL,
                        label = "Display Name",
                        placeholderText = "Enter your display name",
                        inputText = name,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Person)
                    ),
                    onValueChange = { name = it }
                )

                // Email Address Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = "Email Address",
                        placeholderText = "e.g. yourname@example.com",
                        inputText = email,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Email)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { email = it }
                )

                // Bio Field
                ODSTextField(
                    scheme = scheme,
                    props = ODSTextFieldProps(
                        label = "About / Bio",
                        placeholderText = "Tell others about yourself",
                        inputText = bio,
                        leftIcon = ODSIconModel(imageVector = Icons.Outlined.Info)
                    ),
                    onValueChange = { bio = it }
                )

                // Actions
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
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
                        modifier = Modifier.weight(1f),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Save",
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { onSave(name, email, bio) }
                    )
                }
            }
        }
    }
}
