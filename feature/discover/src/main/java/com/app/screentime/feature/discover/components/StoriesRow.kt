package com.app.screentime.feature.discover.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.HomeStoryItem
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun StoriesRow(
    stories: List<HomeStoryItem>,
    onStoryClick: (HomeStoryItem) -> Unit,
    scheme: ODSTheme = zonaODSTheme
) {
    ODSRow(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        gap = ODSVariables.spacingComponent4,
        padding = ODSPadding(
            left = ODSVariables.spacingLayout1,
            right = ODSVariables.spacingLayout1,
            top = ODSVariables.spacingComponent2,
            bottom = ODSVariables.spacingComponent3
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        stories.forEach { story ->
            ODSColumn(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onStoryClick(story) }
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                gap = ODSVariables.spacingComponent2
            ) {
                val ringColor =
                    if (story.isHighlighted) scheme.basicAccent else scheme.basicStrokeSubtle
                ODSBox(
                    modifier = Modifier.size(64.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = ringColor))
                    ),
                    padding = ODSPadding(all = 3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = story.avatarUrl,
                            contentDescription = story.name
                        ),
                        modifier = Modifier.size(54.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        contentScale = ContentScale.Crop
                    )
                }

                ODSText(
                    text = story.name,
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicText
                )
            }
        }
    }
}