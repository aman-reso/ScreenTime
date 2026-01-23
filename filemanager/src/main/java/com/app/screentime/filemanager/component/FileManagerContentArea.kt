package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Groups files by date for display
 */
data class FileDateGroup(
    val dateLabel: String,
    val files: List<FileItem>
)

/**
 * Main content area showing files grouped by date
 */
@Composable
fun FileManagerContentArea(
    files: List<FileItem>,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    selectedFiles: Set<String>,
    onFileClick: (FileItem) -> Unit,
    onFileLongPress: (FileItem) -> Unit,
    onDeleteClick: (FileItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedFiles = groupFilesByDate(files)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent3
        ),
        verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
    ) {
        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
        }

        items(
            items = groupedFiles,
            key = { it.dateLabel }
        ) { group ->
            FileDateGroupSection(
                group = group,
                scheme = scheme,
                isSelectionMode = isSelectionMode,
                selectedFiles = selectedFiles,
                onFileClick = onFileClick,
                onFileLongPress = onFileLongPress,
                onDeleteClick = onDeleteClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
        }
    }
}

@Composable
private fun FileDateGroupSection(
    group: FileDateGroup,
    scheme: ODSTheme,
    isSelectionMode: Boolean,
    selectedFiles: Set<String>,
    onFileClick: (FileItem) -> Unit,
    onFileLongPress: (FileItem) -> Unit,
    onDeleteClick: (FileItem) -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent2
    ) {
        // Date header
        ODSText(
            text = group.dateLabel,
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText,
            modifier = Modifier.padding(
                horizontal = DSVariables.spacingComponent2,
                vertical = DSVariables.spacingComponent1
            )
        )

        // Files in this date group
        group.files.forEach { fileItem ->
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
    }
}

/**
 * Groups files by date (Today, Yesterday, Day before yesterday, Previous 30 Days, Year, etc.)
 */
@Composable
private fun groupFilesByDate(files: List<FileItem>): List<FileDateGroup> {
    val calendar = Calendar.getInstance()
    val today = calendar.clone() as Calendar
    today.set(Calendar.HOUR_OF_DAY, 0)
    today.set(Calendar.MINUTE, 0)
    today.set(Calendar.SECOND, 0)
    today.set(Calendar.MILLISECOND, 0)

    val yesterday = today.clone() as Calendar
    yesterday.add(Calendar.DAY_OF_YEAR, -1)

    val dayBeforeYesterday = today.clone() as Calendar
    dayBeforeYesterday.add(Calendar.DAY_OF_YEAR, -2)

    val thirtyDaysAgo = today.clone() as Calendar
    thirtyDaysAgo.add(Calendar.DAY_OF_YEAR, -30)

    val groups = mutableListOf<FileDateGroup>()
    val todayFiles = mutableListOf<FileItem>()
    val yesterdayFiles = mutableListOf<FileItem>()
    val dayBeforeYesterdayFiles = mutableListOf<FileItem>()
    val recentFiles = mutableListOf<FileItem>()
    val yearFiles = mutableMapOf<Int, MutableList<FileItem>>()

    files.forEach { file ->
        val fileDate = Calendar.getInstance().apply {
            timeInMillis = file.lastModified
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        when {
            fileDate.after(today) || fileDate == today -> {
                todayFiles.add(file)
            }
            fileDate == yesterday -> {
                yesterdayFiles.add(file)
            }
            fileDate == dayBeforeYesterday -> {
                dayBeforeYesterdayFiles.add(file)
            }
            fileDate.after(thirtyDaysAgo) -> {
                recentFiles.add(file)
            }
            else -> {
                val year = fileDate.get(Calendar.YEAR)
                yearFiles.getOrPut(year) { mutableListOf() }.add(file)
            }
        }
    }

    if (todayFiles.isNotEmpty()) {
        groups.add(FileDateGroup(stringResource(R.string.today), todayFiles))
    }
    if (yesterdayFiles.isNotEmpty()) {
        groups.add(FileDateGroup(stringResource(R.string.yesterday), yesterdayFiles))
    }
    if (dayBeforeYesterdayFiles.isNotEmpty()) {
        groups.add(FileDateGroup(stringResource(R.string.day_before_yesterday), dayBeforeYesterdayFiles))
    }
    if (recentFiles.isNotEmpty()) {
        groups.add(FileDateGroup(stringResource(R.string.previous_30_days), recentFiles))
    }

    // Add year groups in descending order
    yearFiles.keys.sortedDescending().forEach { year ->
        groups.add(FileDateGroup(year.toString(), yearFiles[year]!!))
    }

    return groups
}

