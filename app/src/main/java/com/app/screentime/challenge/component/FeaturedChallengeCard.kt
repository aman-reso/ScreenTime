package com.app.screentime.challenge.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.app.screentime.challenge.component.util.formatDate
import com.telekom.odsystem.atoms.icon.ODSIcon

/**
 * Featured challenge card component for displaying a prominent challenge.
 * Uses ODS components for consistent styling.
 *
 * @param challenge The challenge to display
 * @param onJoin Callback when join button is clicked
 * @param onViewDetails Callback when view details is clicked
 * @param scheme ODS theme scheme
 */
@Composable
fun FeaturedChallengeCard(
    challenge: Challenge,
    onJoin: () -> Unit,
    onViewDetails: () -> Unit,
    scheme: ODSTheme
) {
    val startDate = formatDate(challenge.startTime)
    val endDate = formatDate(challenge.endTime)

    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onViewDetails.invoke()
            }
            .clip(RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        ODSColumn {
            // Image
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (challenge.thumbnail != null) {
                    AsyncImage(
                        model = challenge.thumbnail,
                        contentDescription = challenge.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Trophy badge overlay
                ODSBox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    background = listOf(ODSColorModel(scheme.basicTextOnAccent)),
                    cornerRadius = com.telekom.odsystem.foundations.ODSCorners(all = 12.dp),
                    padding = ODSPadding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
                ) {
                    ODSRow(
                        gap = 6.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = challenge.reward,
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )
                    }
                }
            }

            // Content
            ODSColumn(
                background = listOf(ODSColorModel(scheme.basicBackgroundSubtle)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                gap = 12.dp
            ) {
                // Date and Participants row
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ODSRow(
                        gap = 6.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.CalendarToday,
//                            contentDescription = null,
//                            tint = scheme.basicTextRecessive,
//                            modifier = Modifier.size(16.dp)
//                        )
                        ODSText(
                            text = "$startDate - $endDate",
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                    ODSRow(
                        gap = 6.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Group,
//                            contentDescription = null,
//                            tint = scheme.basicTextRecessive,
//                            modifier = Modifier.size(16.dp)
//                        )
                        ODSText(
                            text = "1.2k joined",
                            style = DSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }

                // Title
                ODSText(
                    text = challenge.title,
                    style = DSTextStyles.subtitle,
                    color = scheme.basicText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
                ODSText(
                    text = challenge.description,
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

//                // Join Button
//                AppPrimaryButton(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = "Join Challenge",
//                    onClick = onJoin
//                )
            }
        }
    }
}

