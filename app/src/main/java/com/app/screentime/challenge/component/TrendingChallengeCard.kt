package com.app.screentime.challenge.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.screentime.network.model.Challenge
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Trending challenge card component for displaying challenges in a horizontal list.
 * Uses ODS components for consistent styling.
 *
 * @param challenge The challenge to display
 * @param modifier Modifier to be applied to the component
 * @param onJoin Callback when join button is clicked
 * @param scheme ODS theme scheme
 */
@Composable
fun TrendingChallengeCard(
    challenge: Challenge,
    modifier: Modifier = Modifier,
    onJoin: () -> Unit,
    scheme: ODSTheme
) {
    ODSBox(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard))
    ) {
        ODSColumn {
            // Image
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (challenge.thumbnail != null) {
                    AsyncImage(
                        model = challenge.thumbnail,
                        contentDescription = challenge.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Content
            ODSColumn(
                padding = ODSPadding(all = 12.dp),
                background = listOf(ODSColorModel(scheme.basicBackgroundSubtle)),
                modifier = Modifier
                    .fillMaxWidth(),
                gap = 4.dp
            ) {
                ODSText(
                    text = challenge.title,
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                ODSText(
                    text = challenge.description,
                    style = DSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    gap = 8.dp
                ) {
                    // Badge
                    ODSBox(
                        modifier = Modifier.weight(1f, fill = false),
                        background = listOf(ODSColorModel(scheme.basicAccent)),
                        cornerRadius = ODSCorners(all = 12.dp),
                        padding = ODSPadding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        ODSRow(
                            gap = 4.dp,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            com.telekom.odsystem.atoms.icon.ODSIcon(
                                iconModel = com.telekom.odsystem.atoms.icon.ODSIconModel(
                                    imageVector = Icons.Default.Star,
                                    tint = scheme.basicAccent,
                                    contentDescription = null
                                ),
                                width = 14.dp,
                                height = 14.dp
                            )
                            ODSText(
                                text = challenge.reward,
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.basicAccent,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Plus button
                    ODSBox(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onJoin),
                        background = listOf(ODSColorModel(scheme.basicText)),
                        contentAlignment = Alignment.Center
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Add,
//                            contentDescription = "Join",
//                            tint = scheme.basicTextOnAccent,
//                            modifier = Modifier.size(20.dp)
//                        )
                    }
                }
            }
        }
    }
}

