package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun FileListView(
    files: List<FileItem>,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    selectedFiles: Set<String>,
    onFileClick: (FileItem) -> Unit,
    onFileLongPress: (FileItem) -> Unit,
    onDeleteClick: (FileItem) -> Unit
) {
    ODSLazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        gap = DSVariables.spacingComponent2,
        padding = ODSPadding(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        )
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
        }

        items(
            items = files,
            key = { it.file.absolutePath }
        ) { fileItem ->
            FileItemRow(
                fileItem = fileItem,
                scheme = scheme,
                isSelectionMode = isSelectionMode,
                isSelected = selectedFiles.contains(fileItem.file.absolutePath),
                onClick = { onFileClick(fileItem) },
                onLongPress = { onFileLongPress(fileItem) },
                onDeleteClick = { onDeleteClick(fileItem) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
        }
    }
}

