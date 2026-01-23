package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.filemanager.viewmodel.FileFilterCategory
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.molecules.tabs.ODSTabsVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Filter tabs view for file categories
 */
@Composable
fun FilterTabsView(
    activeFilter: FileFilterCategory,
    scheme: ODSTheme,
    onFilterSelected: (FileFilterCategory) -> Unit
) {
    val filterTabs = listOf(
        FileFilterCategory.ALL to stringResource(R.string.file_manager_all),
        FileFilterCategory.PHOTOS to stringResource(R.string.file_manager_photos),
        FileFilterCategory.VIDEOS to stringResource(R.string.file_manager_videos),
        FileFilterCategory.AUDIO to stringResource(R.string.file_manager_audio),
        FileFilterCategory.APPS to stringResource(R.string.file_manager_apps),
        FileFilterCategory.DOCUMENTS to stringResource(R.string.file_manager_documents),
        FileFilterCategory.GAMES to stringResource(R.string.file_manager_games),
        FileFilterCategory.DUPLICATES to stringResource(R.string.file_manager_duplicates)
    )

    val tabElements = filterTabs.map { (_, label) ->
        ODSTabItemModel(label = label)
    }

    val selectedIndex = filterTabs.indexOfFirst { it.first == activeFilter }.takeIf { it >= 0 } ?: 0

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = DSVariables.spacingComponent4,
                vertical = DSVariables.spacingComponent2
            )
    ) {
        ODSTabs(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSTabsProps(
                tabElements = tabElements,
                size = ODSTabsSize.SMALL,
                variant = ODSTabsVariant.FILL,
                showDividerFrame = true
            ),
            selectedTabIndex = selectedIndex,
            onSelectedTabChange = { index ->
                if (index < filterTabs.size) {
                    onFilterSelected(filterTabs[index].first)
                }
            }
        )
    }
}

