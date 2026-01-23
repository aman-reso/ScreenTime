package com.app.screentime.wallpaper.component

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun WallpaperAddDialog(
    imageUri: Uri,
    scheme: ODSTheme,
    onDismiss: () -> Unit,
    onSave: (name: String, autoSetHome: Boolean, autoSetLock: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("New Wallpaper") }
    val context = LocalContext.current

    ODSDialog(
        scheme = scheme,
        onDismissRequest = onDismiss,
        props = ODSDialogProps(
            title = "Add Wallpaper",
            showCloseButton = true
        ),
        contentSlot = {
            ODSColumn(
                gap = DSVariables.spacingComponent3,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Thumbnail
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Name Input
                ODSTextField(
                    props = ODSTextFieldProps(
                        inputText = name,
                        label = "Wallpaper Name",
                        placeholderText = "Enter name..."
                    ),
                    onValueChange = { name = it },
                    scheme = scheme
                )

                // Quick Set Buttons (Optional/Immediate)
                ODSColumn(gap = DSVariables.spacingComponent2) {
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Save & Set Home",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { onSave(name, true, false) }
                    )
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Save & Set Lock",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { onSave(name, false, true) }
                    )
                    ODSButton(
                        modifier = Modifier.fillMaxWidth(),
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = "Save & Set Both",
                            variant = ODSButtonVariant.SECONDARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = { onSave(name, true, true) }
                    )
                }
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                gap = DSVariables.spacingComponent2
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Cancel",
                        variant = ODSButtonVariant.GHOST,
                        size = ODSButtonSize.LARGE
                    ),
                    onClick = onDismiss
                )
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Save Only",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = { onSave(name, false, false) }
                )
            }
        }
    )
}
