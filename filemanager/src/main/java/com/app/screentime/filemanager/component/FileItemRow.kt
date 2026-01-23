package com.app.screentime.filemanager.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.VideoFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FileItemRow(
    fileItem: FileItem,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Helper function to check if file is a video
    val isVideoFile = fileItem.mimeType?.startsWith("video/") == true ||
            fileItem.name.endsWith(".mp4", ignoreCase = true) ||
            fileItem.name.endsWith(".avi", ignoreCase = true) ||
            fileItem.name.endsWith(".mkv", ignoreCase = true) ||
            fileItem.name.endsWith(".mov", ignoreCase = true) ||
            fileItem.name.endsWith(".wmv", ignoreCase = true) ||
            fileItem.name.endsWith(".flv", ignoreCase = true) ||
            fileItem.name.endsWith(".3gp", ignoreCase = true) ||
            fileItem.name.endsWith(".webm", ignoreCase = true) ||
            fileItem.name.endsWith(".m4v", ignoreCase = true)

    // Helper function to check if file is an image
    val isImageFile = fileItem.mimeType?.startsWith("image/") == true ||
            fileItem.name.endsWith(".jpg", ignoreCase = true) ||
            fileItem.name.endsWith(".jpeg", ignoreCase = true) ||
            fileItem.name.endsWith(".png", ignoreCase = true) ||
            fileItem.name.endsWith(".gif", ignoreCase = true) ||
            fileItem.name.endsWith(".bmp", ignoreCase = true) ||
            fileItem.name.endsWith(".webp", ignoreCase = true) ||
            fileItem.name.endsWith(".heic", ignoreCase = true) ||
            fileItem.name.endsWith(".heif", ignoreCase = true)

    val icon = when {
        fileItem.isDirectory -> Icons.Outlined.Folder
        isImageFile -> Icons.Outlined.Image
        isVideoFile -> Icons.Outlined.VideoFile
        fileItem.mimeType?.startsWith("audio/") == true -> Icons.Outlined.AudioFile
        fileItem.mimeType == "application/pdf" -> Icons.Outlined.PictureAsPdf
        fileItem.mimeType == "application/vnd.android.package-archive" -> Icons.Filled.Android
        fileItem.name.endsWith(".apk", ignoreCase = true) -> Icons.Filled.Android
        else -> Icons.Outlined.InsertDriveFile
    }

    val context = LocalContext.current
    val sizeText = if (fileItem.isDirectory) {
        ""
    } else {
        formatFileSize(context, fileItem.size)
    }

    val dateText = formatDate(fileItem.lastModified)

    val backgroundColor = if (isSelectionMode && isSelected) {
        val successColor = scheme.functionalSuccessStandard
        val colorWithAlpha = HexColor(successColor.getHexColor(), alpha = 0.15f)
        listOf(ODSColorModel(colorWithAlpha))
    } else {
        listOf(ODSColorModel(scheme.basicBackgroundCard))
    }

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            },
        background = backgroundColor,
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            gap = DSVariables.spacingComponent3
        ) {
            // Left side: Checkbox (in selection mode) or nothing
            if (isSelectionMode) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        imageVector = if (isSelected) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                        tint = if (isSelected) scheme.functionalSuccessStandard else scheme.basicTextRecessive,
                        contentDescription = if (isSelected) stringResource(R.string.selected) else stringResource(
                            R.string.not_selected
                        )
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Middle: Icon/Image preview and name
            ODSRow(
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Show image preview for image files, icon for others
                if (isImageFile && !fileItem.isDirectory) {
                    // Image preview
                    ODSBox(
                        modifier = Modifier.size(48.dp),
                        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = "file://${fileItem.file.absolutePath}",
                                contentDescription = fileItem.name
                            ),
                            modifier = Modifier.size(48.dp),
                            width = 48.dp,
                            height = 48.dp
                        )
                    }
                } else {
                    // File icon for non-image files and directories
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = icon,
                            tint = if (fileItem.isDirectory) {
                                scheme.functionalSuccessStandard
                            } else {
                                scheme.basicText
                            },
                            contentDescription = if (fileItem.isDirectory) stringResource(R.string.folder) else stringResource(
                                R.string.file
                            )
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                }

                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = fileItem.name,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText,
                        maxLines = 1
                    )
                    if (sizeText.isNotEmpty() || dateText.isNotEmpty()) {
                        ODSText(
                            text = buildString {
                                if (sizeText.isNotEmpty()) {
                                    append(sizeText)
                                }
                                if (sizeText.isNotEmpty() && dateText.isNotEmpty()) {
                                    append(" • ")
                                }
                                if (dateText.isNotEmpty()) {
                                    append(dateText)
                                }
                            },
                            style = DSTextStyles.oxMicrocopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }

            // Actions
            if (!isSelectionMode) {
                ODSRow(
                    horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete button
                    ODSBox(
                        modifier = Modifier
                            .size(32.dp)
                            .onClick { onDeleteClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Outlined.Delete,
                                tint = scheme.functionalDestructiveStandard,
                                contentDescription = stringResource(R.string.delete)
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Arrow for directories
                    if (fileItem.isDirectory) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Outlined.ChevronRight,
                                tint = scheme.basicTextRecessive,
                                contentDescription = stringResource(R.string.navigate)
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                // In selection mode, show checkmark or nothing
                if (isSelected) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Outlined.CheckCircle,
                            tint = scheme.functionalSuccessStandard,
                            contentDescription = stringResource(R.string.selected)
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private fun formatFileSize(context: android.content.Context, bytes: Long): String {
    if (bytes < 1024) return context.getString(R.string.file_size_bytes, bytes.toInt())
    val kb = bytes / 1024.0
    if (kb < 1024) return context.getString(R.string.file_size_kb, kb)
    val mb = kb / 1024.0
    if (mb < 1024) return context.getString(R.string.file_size_mb, mb)
    val gb = mb / 1024.0
    return context.getString(R.string.file_size_gb, gb)
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

