package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.molecules.tabs.ODSTabItemModel
import com.telekom.odsystem.molecules.tabs.ODSTabs
import com.telekom.odsystem.molecules.tabs.ODSTabsProps
import com.telekom.odsystem.molecules.tabs.ODSTabsSize
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun FeedTabsHeader(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = ODSVariables.spacingComponent6,
                vertical = ODSVariables.spacingComponent2
            ),
        gap = ODSVariables.spacingComponent6,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onTabSelect(0) }
                ),
            gap = ODSVariables.spacingComponent2
        ) {
            ODSTabs(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                selectedTabIndex = selectedTab,
                props = ODSTabsProps(
                    size = ODSTabsSize.SMALL,
                    tabElements = listOf(
                        ODSTabItemModel(label = "For you"),
                        ODSTabItemModel(label = "Following")
                    )
                ),
                onSelectedTabChange = {
                    onTabSelect(it)
                }
            )
        }
    }
}