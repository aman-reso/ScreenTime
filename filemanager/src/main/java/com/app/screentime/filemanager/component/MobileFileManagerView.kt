package com.app.screentime.filemanager.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.app.screentime.filemanager.model.FileItem
import com.app.screentime.filemanager.repository.FileManagerRepository.StorageInfo
import com.app.screentime.filemanager.viewmodel.FileFilterCategory
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor

/**
 * Mobile file manager home view using the same design as StorageDashboardScreen
 */
@Composable
fun MobileFileManagerView(
    storageInfo: StorageInfo?,
    recentFiles: List<FileItem>,
    categories: List<CategoryItem>,
    folders: List<FileItem> = emptyList(),
    onRecentFileClick: (FileItem) -> Unit,
    onCategoryClick: (FileFilterCategory) -> Unit,
    onFolderClick: (FileItem) -> Unit = {},
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit,
    onNotificationClick: () -> Unit,
    isGridView: Boolean = true,
    onToggleViewMode: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // Pure black base
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // Header
            item {
                HeaderSection(
                    onSearchClick = onSearchClick,
                    onAddClick = onAddClick,
                    onNotificationClick = onNotificationClick
                )
            }

            // Storage Space
            storageInfo?.let { info ->
                item {
                    StorageSpaceCard(storageInfo = info)
                }
            }

            // Recently Opened
            item {
                RecentlyOpenedSection(
                    files = recentFiles,
                    onFileClick = onRecentFileClick
                )
            }

            // Folders section
            if (folders.isNotEmpty()) {
                item {
                    FoldersSection(
                        folders = folders,
                        onFolderClick = onFolderClick,
                        isGridView = isGridView,
                        onToggleViewMode = onToggleViewMode
                    )
                }
            }

            // Categories
            item {
                CategoriesSection(
                    categories = categories,
                    onCategoryClick = onCategoryClick,
                    isGridView = isGridView,
                    onToggleViewMode = onToggleViewMode
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onSearchClick: () -> Unit,
    onAddClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search icon
        GlassIconButton(
            icon = Icons.Outlined.Search,
            contentDescription = "Search",
            onClick = onSearchClick
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Add icon
            GlassIconButton(
                icon = Icons.Filled.Add,
                contentDescription = "Add",
                onClick = onAddClick
            )

            // Notifications icon with red dot
            Box {
                GlassIconButton(
                    icon = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    onClick = onNotificationClick
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF453A))
                        .border(2.dp, Color(0xFF000000), CircleShape)
                )
            }
        }
    }
}

@Composable
private fun GlassIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(Color(0xFF1C1C1E))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color(0xFFE5E5EA),
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
private fun StorageSpaceCard(storageInfo: StorageInfo) {
    val totalGB = storageInfo.totalBytes / (1024.0 * 1024.0 * 1024.0)
    val usedGB = storageInfo.usedBytes / (1024.0 * 1024.0 * 1024.0)

    // Mock data for categories to match the UI reference
    val photosGB = 34.0
    val documentsGB = 68.0
    val videosGB = 26.0
    val otherGB = usedGB - (photosGB + documentsGB + videosGB)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ODSText(
            text = "Storage Space",
            color = HexColor("#E5E5EA"),
            style = DSTextStyles.bodyL
        )

        // Usage Text
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            ODSText(
                text = "${usedGB.toInt()}GB",
                color = HexColor("#FFFFFF"),
                style = DSTextStyles.titleM
            )
            Spacer(modifier = Modifier.width(8.dp))
            ODSText(
                text = "Used from ${totalGB.toInt()}",
                color = HexColor("#8E8E93"),
                style = DSTextStyles.bodyMRegular,
                modifier = Modifier.padding(bottom = 6.dp) // Optical alignment
            )
        }

        // Progress Bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            // Calculate weights ensuring they're all positive
            val safeTotalGB = totalGB.coerceAtLeast(1.0) // Prevent division by zero
            val safeOtherGB = otherGB.coerceAtLeast(0.0) // Ensure non-negative
            
            // Calculate weights with minimum value to ensure positive
            val photosWeight = maxOf(0.001f, (photosGB / safeTotalGB).toFloat())
            val documentsWeight = maxOf(0.001f, (documentsGB / safeTotalGB).toFloat())
            val videosWeight = maxOf(0.001f, (videosGB / safeTotalGB).toFloat())
            val otherWeight = maxOf(0.001f, (safeOtherGB / safeTotalGB).toFloat())
            
            // Photos (Orange)
            Box(
                modifier = Modifier
                    .weight(photosWeight)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFF9F0A))
            )
            Spacer(modifier = Modifier.width(4.dp))
            
            // Documents (Blue)
            Box(
                modifier = Modifier
                    .weight(documentsWeight)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0A84FF))
            )
            Spacer(modifier = Modifier.width(4.dp))
            
            // Videos (Green)
            Box(
                modifier = Modifier
                    .weight(videosWeight)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .background(Color(0xFF32D74B))
            )
            
            // Remaining (Solid Grey)
            Box(
                modifier = Modifier
                    .weight(otherWeight)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(Color(0xFF2C2C2E))
            )
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem("Photos", "${photosGB.toInt()}GB", HexColor("#FF9F0A"))
            LegendItem("Documents", "${documentsGB.toInt()}GB", HexColor("#0A84FF"))
            LegendItem("Videos", "${videosGB.toInt()}GB", HexColor("#32D74B"))
        }
    }
}

@Composable
private fun LegendItem(label: String, size: String, color: HexColor) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color.getColor())
        )
        Spacer(modifier = Modifier.width(8.dp))
        ODSText(
            text = label,
            color = HexColor("#8E8E93"),
            style = DSTextStyles.microcopyRegular
        )
        Spacer(modifier = Modifier.width(4.dp))
        ODSText(
            text = size,
            color = HexColor("#FFFFFF"),
            style = DSTextStyles.microcopyBold
        )
    }
}

@Composable
private fun RecentlyOpenedSection(
    files: List<FileItem>,
    onFileClick: (FileItem) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "Recently Opened",
                color = HexColor("#FFFFFF"),
                style = DSTextStyles.bodyL
            )
            ODSText(
                text = "View All >",
                color = HexColor("#8E8E93"),
                style = DSTextStyles.bodySRegular,
                modifier = Modifier.clickable { }
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(files) { file ->
                GlassFileCard(file, onClick = { onFileClick(file) })
            }
        }
    }
}

@Composable
private fun FoldersSection(
    folders: List<FileItem>,
    onFolderClick: (FileItem) -> Unit,
    isGridView: Boolean = true,
    onToggleViewMode: () -> Unit = {}
) {
    val filteredFolders = folders.filter { it.isDirectory }
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "Folders",
                color = HexColor("#FFFFFF"),
                style = DSTextStyles.bodyL
            )

            // Toggle Switch
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1C1C1E))
                    .padding(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Grid button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .then(
                                if (isGridView) {
                                    Modifier
                                        .background(Color(0xFF3A3A3C))
                                        .border(0.5.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                } else {
                                    Modifier.clickable { onToggleViewMode() }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GridView,
                            contentDescription = "Grid",
                            tint = if (isGridView) Color.White else Color(0xFF636366),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    // List button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .then(
                                if (!isGridView) {
                                    Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF3A3A3C))
                                        .border(0.5.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                } else {
                                    Modifier.clickable { onToggleViewMode() }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.List,
                            contentDescription = "List",
                            tint = if (!isGridView) Color.White else Color(0xFF636366),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Grid or List view based on toggle
        if (isGridView) {
            // Grid view - horizontal scroll
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredFolders) { folder ->
                    GlassFileCard(folder, onClick = { onFolderClick(folder) })
                }
            }
        } else {
            // List view - vertical scroll
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp), // Limit height for list view
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredFolders) { folder ->
                    FolderListItem(folder, onClick = { onFolderClick(folder) })
                }
            }
        }
    }
}

@Composable
private fun FolderListItem(folder: FileItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1C1C1E).copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Folder icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A84FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Folder,
                    contentDescription = folder.name,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Folder name
            ODSText(
                text = folder.name,
                color = HexColor("#FFFFFF"),
                style = DSTextStyles.bodySRegular,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun GlassFileCard(file: FileItem, onClick: () -> Unit) {
    val fileType = getFileTypeFromName(file.name)
    val iconColor = getFileTypeColor(fileType)
    val iconImageVector = getFileTypeIcon(fileType)

    Box(
        modifier = Modifier
            .width(170.dp)
            .height(88.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF1C1C1E).copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                // Enhanced Glow
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(iconColor.copy(alpha = 0.5f), Color.Transparent),
                                radius = 70f
                            )
                        )
                )

                // Icon Container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconImageVector,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                ODSText(
                    text = file.name,
                    color = HexColor("#FFFFFF"),
                    style = DSTextStyles.bodySBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                ODSText(
                    text = if (file.isDirectory) "${file.size} item" else formatFileSize(file.size),
                    color = HexColor("#8E8E93"),
                    style = DSTextStyles.microcopyRegular
                )
            }
        }
    }
}

@Composable
private fun CategoriesSection(
    categories: List<CategoryItem>,
    onCategoryClick: (FileFilterCategory) -> Unit,
    isGridView: Boolean = true,
    onToggleViewMode: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSText(
                text = "Categories",
                color = HexColor("#FFFFFF"),
                style = DSTextStyles.bodyL
            )

            // Toggle Switch
            Box(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1C1C1E))
                    .padding(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Grid button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .then(
                                if (isGridView) {
                                    Modifier
                                        .background(Color(0xFF3A3A3C))
                                        .border(0.5.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                } else {
                                    Modifier.clickable { onToggleViewMode() }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.GridView,
                            contentDescription = "Grid",
                            tint = if (isGridView) Color.White else Color(0xFF636366),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    // List button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .then(
                                if (!isGridView) {
                                    Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF3A3A3C))
                                        .border(0.5.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                } else {
                                    Modifier.clickable { onToggleViewMode() }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.List,
                            contentDescription = "List",
                            tint = if (!isGridView) Color.White else Color(0xFF636366),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Grid Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF0C0C0E))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(32.dp)
                )
        ) {
            // Grid Pattern
            Canvas(modifier = Modifier.matchParentSize()) {
                val step = 50.dp.toPx()
                val color = Color.White.copy(alpha = 0.05f)

                // Draw vertical lines
                for (x in 0..size.width.toInt() step step.toInt()) {
                    drawLine(
                        color = color,
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(x.toFloat(), size.height),
                        strokeWidth = 1f
                    )
                }

                // Draw horizontal lines
                for (y in 0..size.height.toInt() step step.toInt()) {
                    drawLine(
                        color = color,
                        start = Offset(0f, y.toFloat()),
                        end = Offset(size.width, y.toFloat()),
                        strokeWidth = 1f
                    )
                }
            }

            // Categories Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1
                if (categories.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (categories.isNotEmpty()) {
                            GlassCategoryCard(
                                category = categories[0],
                                onClick = { onCategoryClick(categories[0].filter) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (categories.size > 1) {
                            GlassCategoryCard(
                                category = categories[1],
                                onClick = { onCategoryClick(categories[1].filter) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                // Row 2
                if (categories.size > 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (categories.size > 2) {
                            GlassCategoryCard(
                                category = categories[2],
                                onClick = { onCategoryClick(categories[2].filter) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (categories.size > 3) {
                            GlassCategoryCard(
                                category = categories[3],
                                onClick = { onCategoryClick(categories[3].filter) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassCategoryCard(
    category: CategoryItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category.title)

    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF242426))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icon with Bloom
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                // Bloom
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(categoryColor.copy(alpha = 0.5f), Color.Transparent),
                                radius = 90f
                            )
                        )
                )

                // Icon Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(categoryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                ODSText(
                    text = category.title,
                    color = HexColor("#FFFFFF"),
                    style = DSTextStyles.bodySBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                ODSText(
                    text = "${category.itemCount} items",
                    color = HexColor("#8E8E93"),
                    style = DSTextStyles.microcopyRegular
                )
            }
        }
    }
}

data class CategoryItem(
    val title: String,
    val icon: ImageVector,
    val itemCount: Int,
    val filter: FileFilterCategory
)

private fun getFileTypeFromName(fileName: String): String {
    val extension = fileName.substringAfterLast('.', "").lowercase()
    return when (extension) {
        "pdf" -> "PDF"
        "jpg", "jpeg", "png", "gif" -> "Image"
        "mp4", "avi", "mov" -> "Video"
        else -> "File"
    }
}

private fun getFileTypeIcon(fileType: String): ImageVector {
    return when (fileType.uppercase()) {
        "PDF" -> Icons.Outlined.Description
        "IMAGE" -> Icons.Outlined.Image
        "VIDEO" -> Icons.Outlined.VideoLibrary
        else -> Icons.Outlined.Description
    }
}

private fun getFileTypeColor(fileType: String): Color {
    return when (fileType.uppercase()) {
        "PDF" -> Color(0xFF4285F4) // Blue
        "IMAGE" -> Color(0xFFFF9800) // Orange
        "VIDEO" -> Color(0xFF4CAF50) // Green
        else -> Color(0xFF9E9E9E) // Gray
    }
}

private fun getCategoryColor(categoryName: String): Color {
    return when (categoryName.uppercase()) {
        "VIDEOS" -> Color(0xFF4CAF50) // Green
        "DOCUMENTS" -> Color(0xFF4285F4) // Blue
        "IMAGES" -> Color(0xFFFF9800) // Orange
        "MUSIC" -> Color(0xFFE91E63) // Red/Pink
        else -> Color(0xFF9E9E9E) // Gray
    }
}

private fun formatFileSize(bytes: Long): String {
    if (bytes < 1024) return "${bytes}B"
    val kb = bytes / 1024.0
    if (kb < 1024) return String.format("%.1f KB", kb)
    val mb = kb / 1024.0
    if (mb < 1024) return String.format("%.1f MB", mb)
    val gb = mb / 1024.0
    return String.format("%.1f GB", gb)
}
