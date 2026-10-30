package com.app.screentime.feature.discover.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.foundations.ODSPadding
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
    modifier: Modifier = Modifier,
    isStuck: Boolean = false
) {
    val forYouLabel = stringResource(R.string.tab_for_you)
    val followingLabel = stringResource(R.string.tab_following)
    val matchesLabel = stringResource(R.string.tab_matches)

    ODSColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(scheme.basicBackground.getColor())
    ) {
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            padding = ODSPadding(
                horizontal = ODSVariables.spacingComponent5,
                bottom = ODSVariables.spacingComponent4
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
                        showDividerFrame = false,
                        size = ODSTabsSize.SMALL,
                        tabElements = listOf(
                            ODSTabItemModel(label = forYouLabel),
                            ODSTabItemModel(label = followingLabel),
                            ODSTabItemModel(label = matchesLabel)
                        )
                    ),
                    onSelectedTabChange = {
                        onTabSelect(it)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = isStuck,
            enter = fadeIn(animationSpec = tween(150)),
            exit = fadeOut(animationSpec = tween(150))
        ) {
            ODSDivider(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = ODSDividerProps(
                    variant = ODSDividerVariant.HORIZONTAL,
                    inset = false,
                    spacing = false
                )
            )
        }
    }
}
