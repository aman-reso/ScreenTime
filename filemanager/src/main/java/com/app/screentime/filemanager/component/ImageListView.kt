package com.app.screentime.filemanager.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.Description
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.filemanager.model.FileItem
import com.app.screentime.filemanager.util.VideoThumbnailHelper
import com.app.screentime.filemanager.util.DocumentThumbnailHelper
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * List view for images showing thumbnail, name, and size
 */
@Composable
fun ImageListView(
    files: List<FileItem>,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    selectedFiles: Set<String>,
    onFileClick: (FileItem) -> Unit,
    onFileLongPress: (FileItem) -> Unit,
    onDeleteClick: (FileItem) -> Unit,
    showOnlyImages: Boolean = true, // Only filter images when true (for photos)
    modifier: Modifier = Modifier
) {
    // For photos, filter to show only folders and images. For other categories, show all files
    val filteredFiles = if (showOnlyImages) {
        files.filter { fileItem ->
            fileItem.isDirectory || isImageFile(fileItem)
        }
    } else {
        files
    }

    ODSLazyColumn(
        modifier = modifier.fillMaxSize(),
        gap = DSVariables.spacingComponent2,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        items(
            items = filteredFiles,
            key = { it.file.absolutePath }
        ) { fileItem ->
            ImageListItem(
                fileItem = fileItem,
                scheme = scheme,
                isSelectionMode = isSelectionMode,
                isSelected = selectedFiles.contains(fileItem.file.absolutePath),
                onClick = { onFileClick(fileItem) },
                onLongPress = { onFileLongPress(fileItem) },
                onDeleteClick = { onDeleteClick(fileItem) }
            )
        }
    }
}

@Composable
private fun ImageListItem(
    fileItem: FileItem,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current
    val isImage = isImageFile(fileItem)
    val isVideo = isVideoFile(fileItem)
    val isDocument = isDocumentFile(fileItem)
    var videoThumbnailPath by remember(fileItem.file.absolutePath) { mutableStateOf<String?>(null) }
    var documentThumbnailPath by remember(fileItem.file.absolutePath) { mutableStateOf<String?>(null) }
    
    // Load video thumbnail
    LaunchedEffect(fileItem.file.absolutePath) {
        if (isVideo && !fileItem.isDirectory) {
            videoThumbnailPath = VideoThumbnailHelper.getVideoThumbnailPath(context, fileItem.file)
        }
    }
    
    // Load document thumbnail
    LaunchedEffect(fileItem.file.absolutePath) {
        if (isDocument && !fileItem.isDirectory) {
            documentThumbnailPath = DocumentThumbnailHelper.getDocumentThumbnailPath(context, fileItem.file)
        }
    }
    
    val sizeText = if (fileItem.isDirectory) {
        ""
    } else {
        formatFileSize(context, fileItem.size)
    }

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
            .height(72.dp)
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
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Image/video/document thumbnail, folder icon, or file icon
            when {
                isImage && !fileItem.isDirectory -> {
                    ODSBox(
                        modifier = Modifier.size(56.dp),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = "file://${fileItem.file.absolutePath}",
                                contentDescription = fileItem.name
                            ),
                            modifier = Modifier.size(56.dp),
                            width = 56.dp,
                            height = 56.dp
                        )
                    }
                }
                isVideo && !fileItem.isDirectory && videoThumbnailPath != null -> {
                    ODSBox(
                        modifier = Modifier.size(56.dp),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = "file://$videoThumbnailPath",
                                contentDescription = fileItem.name
                            ),
                            modifier = Modifier.size(56.dp),
                            width = 56.dp,
                            height = 56.dp
                        )
                    }
                }
                isDocument && !fileItem.isDirectory && documentThumbnailPath != null -> {
                    ODSBox(
                        modifier = Modifier.size(56.dp),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = "file://$documentThumbnailPath",
                                contentDescription = fileItem.name
                            ),
                            modifier = Modifier.size(56.dp),
                            width = 56.dp,
                            height = 56.dp
                        )
                    }
                }
                fileItem.isDirectory -> {
                    // Folder icon
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = Icons.Outlined.Folder,
                            tint = scheme.functionalSuccessStandard,
                            contentDescription = stringResource(R.string.folder)
                        ),
                        modifier = Modifier.size(56.dp)
                    )
                }
                else -> {
                    // File icon for non-image/video/document files
                    val fileIcon = getFileIcon(fileItem)
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = fileIcon,
                            tint = scheme.basicText,
                            contentDescription = stringResource(R.string.file)
                        ),
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            // Middle: File name
            ODSColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                gap = DSVariables.spacingComponent1
            ) {
                ODSText(
                    text = fileItem.name,
                    style = DSTextStyles.bodySRegular,
                    color = scheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Right: Size and selection checkbox
            ODSRow(
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Size text
                if (sizeText.isNotEmpty()) {
                    ODSText(
                        text = sizeText,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // Selection checkbox
                if (isSelectionMode) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = if (isSelected) Icons.Outlined.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
                            tint = if (isSelected) scheme.functionalSuccessStandard else scheme.basicTextRecessive,
                            contentDescription = if (isSelected) stringResource(R.string.selected) else stringResource(R.string.not_selected)
                        ),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private fun isImageFile(fileItem: FileItem): Boolean {
    val isImageMimeType = fileItem.mimeType?.startsWith("image/") == true
    val isImageExtension = fileItem.name.endsWith(".jpg", ignoreCase = true) ||
            fileItem.name.endsWith(".jpeg", ignoreCase = true) ||
            fileItem.name.endsWith(".png", ignoreCase = true) ||
            fileItem.name.endsWith(".gif", ignoreCase = true) ||
            fileItem.name.endsWith(".bmp", ignoreCase = true) ||
            fileItem.name.endsWith(".webp", ignoreCase = true) ||
            fileItem.name.endsWith(".heic", ignoreCase = true) ||
            fileItem.name.endsWith(".heif", ignoreCase = true)
    return isImageMimeType || isImageExtension
}

private fun isVideoFile(fileItem: FileItem): Boolean {
    val isVideoMimeType = fileItem.mimeType?.startsWith("video/") == true
    val isVideoExtension = fileItem.name.endsWith(".mp4", ignoreCase = true) ||
            fileItem.name.endsWith(".avi", ignoreCase = true) ||
            fileItem.name.endsWith(".mkv", ignoreCase = true) ||
            fileItem.name.endsWith(".mov", ignoreCase = true) ||
            fileItem.name.endsWith(".wmv", ignoreCase = true) ||
            fileItem.name.endsWith(".flv", ignoreCase = true) ||
            fileItem.name.endsWith(".3gp", ignoreCase = true) ||
            fileItem.name.endsWith(".webm", ignoreCase = true) ||
            fileItem.name.endsWith(".m4v", ignoreCase = true)
    return isVideoMimeType || isVideoExtension
}

private fun isDocumentFile(fileItem: FileItem): Boolean {
    val isDocumentMimeType = fileItem.mimeType?.startsWith("application/") == true ||
            fileItem.mimeType == "application/pdf"
    val isDocumentExtension = fileItem.name.endsWith(".pdf", ignoreCase = true) ||
            fileItem.name.endsWith(".doc", ignoreCase = true) ||
            fileItem.name.endsWith(".docx", ignoreCase = true) ||
            fileItem.name.endsWith(".xls", ignoreCase = true) ||
            fileItem.name.endsWith(".xlsx", ignoreCase = true) ||
            fileItem.name.endsWith(".ppt", ignoreCase = true) ||
            fileItem.name.endsWith(".pptx", ignoreCase = true) ||
            fileItem.name.endsWith(".txt", ignoreCase = true) ||
            fileItem.name.endsWith(".rtf", ignoreCase = true)
    return isDocumentMimeType || isDocumentExtension
}

private fun getFileIcon(fileItem: FileItem): androidx.compose.ui.graphics.vector.ImageVector {
    val isVideoFile = fileItem.mimeType?.startsWith("video/") == true ||
            fileItem.name.endsWith(".mp4", ignoreCase = true) ||
            fileItem.name.endsWith(".avi", ignoreCase = true) ||
            fileItem.name.endsWith(".mkv", ignoreCase = true) ||
            fileItem.name.endsWith(".mov", ignoreCase = true)

    return when {
        isVideoFile -> Icons.Outlined.VideoFile
        fileItem.mimeType?.startsWith("audio/") == true -> Icons.Outlined.AudioFile
        fileItem.mimeType == "application/pdf" -> Icons.Outlined.PictureAsPdf
        fileItem.name.endsWith(".apk", ignoreCase = true) -> Icons.Filled.Android
        fileItem.mimeType?.startsWith("application/") == true -> Icons.Outlined.Description
        else -> Icons.Outlined.InsertDriveFile
    }
}

private fun formatFileSize(context: android.content.Context, bytes: Long): String {
    if (bytes < 1024) return "${bytes}B"
    val kb = bytes / 1024.0
    if (kb < 1024) return String.format("%.1f KB", kb)
    val mb = kb / 1024.0
    if (mb < 1024) return String.format("%.1f MB", mb)
    val gb = mb / 1024.0
    return String.format("%.1f GB", gb)
}

