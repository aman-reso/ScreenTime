package com.app.screentime.filemanager.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.config.R
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Navigation item in sidebar
 */
sealed class SidebarNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Recent : SidebarNavItem("Recents", Icons.Outlined.History)
    object Downloads : SidebarNavItem("Downloads", Icons.Outlined.Download)
    object Documents : SidebarNavItem("Documents", Icons.Outlined.Folder)
    object Images : SidebarNavItem("Images", Icons.Outlined.Image)
    object Videos : SidebarNavItem("Videos", Icons.Outlined.VideoLibrary)
    object Storage : SidebarNavItem("Storage", Icons.Outlined.Storage)
    object Apps : SidebarNavItem("Apps", Icons.Outlined.PhoneAndroid)
}

@Composable
fun FileManagerSidebar(
    selectedItem: SidebarNavItem?,
    scheme: ODSTheme,
    isCollapsed: Boolean,
    onItemClick: (SidebarNavItem) -> Unit,
    onToggleCollapse: () -> Unit,
    folders: List<FileItem> = emptyList(),
    onFolderClick: ((FileItem) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val favourites = listOf(
        SidebarNavItem.Recent,
        SidebarNavItem.Downloads,
        SidebarNavItem.Documents,
        SidebarNavItem.Images,
        SidebarNavItem.Videos,
        SidebarNavItem.Storage,
        SidebarNavItem.Apps
    )

    val sidebarWidth = if (isCollapsed) 60.dp else 200.dp

    ODSBox(
        modifier = modifier
            .width(sidebarWidth)
            .fillMaxHeight(),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard))
    ) {
        ODSLazyColumn (
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(vertical = DSVariables.spacingComponent3),
            verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent4)
        ) {
            // Collapse/Expand button
            item {
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleCollapse() },
                    horizontalArrangement = Arrangement.End,
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent3,
                        vertical = DSVariables.spacingComponent2
                    )
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            imageVector = if (isCollapsed) Icons.Outlined.ChevronRight else Icons.Outlined.ChevronLeft,
                            tint = scheme.basicText,
                            contentDescription = if (isCollapsed) "Expand sidebar" else "Collapse sidebar"
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Favourites section
            item {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent1
                ) {
                    if (!isCollapsed) {
                        ODSText(
                            text = "Favourites",
                            style = DSTextStyles.oxMicrocopyRegular,
                            color = scheme.basicTextRecessive,
                            modifier = Modifier.padding(
                                horizontal = DSVariables.spacingComponent4,
                                vertical = DSVariables.spacingComponent2
                            )
                        )
                    }

                    favourites.forEach { item ->
                        SidebarNavItemRow(
                            item = item,
                            isSelected = selectedItem == item,
                            isCollapsed = isCollapsed,
                            scheme = scheme,
                            onClick = { onItemClick(item) }
                        )
                    }
                }
            }

            // Folders section
            if (folders.isNotEmpty()) {
                item {
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        gap = DSVariables.spacingComponent1
                    ) {
                        if (!isCollapsed) {
                            ODSText(
                                text = "Folders",
                                style = DSTextStyles.oxMicrocopyRegular,
                                color = scheme.basicTextRecessive,
                                modifier = Modifier.padding(
                                    horizontal = DSVariables.spacingComponent4,
                                    vertical = DSVariables.spacingComponent2
                                )
                            )
                        }

                        folders.forEach { folder ->
                            FolderItemRow(
                                folder = folder,
                                isCollapsed = isCollapsed,
                                scheme = scheme,
                                onClick = { onFolderClick?.invoke(folder) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarNavItemRow(
    item: SidebarNavItem,
    isSelected: Boolean,
    isCollapsed: Boolean,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        listOf(ODSColorModel(scheme.functionalSuccessStandard))
    } else {
        emptyList()
    }

    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.spacedBy(DSVariables.spacingComponent3),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        background = backgroundColor,
        padding = ODSPadding(
            horizontal = if (isCollapsed) DSVariables.spacingComponent2 else DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent2
        )
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = item.icon,
                tint = if (isSelected) scheme.functionalSuccessStandard else scheme.basicText,
                contentDescription = item.title
            ),
            modifier = Modifier.size(20.dp)
        )
        if (!isCollapsed) {
            ODSText(
                text = item.title,
                style = DSTextStyles.bodySRegular,
                color = if (isSelected) scheme.functionalSuccessStandard else scheme.basicText
            )
        }
    }
}

@Composable
private fun FolderItemRow(
    folder: FileItem,
    isCollapsed: Boolean,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.spacedBy(DSVariables.spacingComponent3),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        padding = ODSPadding(
            horizontal = if (isCollapsed) DSVariables.spacingComponent2 else DSVariables.spacingComponent4,
            vertical = DSVariables.spacingComponent2
        )
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = Icons.Outlined.Folder,
                tint = scheme.functionalSuccessStandard,
                contentDescription = folder.name
            ),
            modifier = Modifier.size(20.dp)
        )
        if (!isCollapsed) {
            ODSText(
                text = folder.name,
                style = DSTextStyles.bodySRegular,
                color = scheme.basicText,
                maxLines = 1
            )
        }
    }
}

