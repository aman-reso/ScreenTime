package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.model.ModelProfile
import com.app.screentime.core.ui.components.PompiereTitle
import com.telekom.odsystem.atoms.*
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.*
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

@Composable
fun ModelProfileScreen(
    modelId: String,
    modelName: String = "Model",
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    viewModel: ModelProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onStartChat: (String, String) -> Unit = { _, _ -> },
    onStartVoiceCall: (String, String) -> Unit = { _, _ -> }
) {
    LaunchedEffect(modelId) {
        viewModel.loadModel(modelId, modelName)
    }

    val uiState by viewModel.uiState.collectAsState()
    val model = uiState.model ?: ModelProfile(id = modelId, name = modelName)
    val isFavorite = uiState.isFavorite

    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        // 1. Top Bar
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = com.telekom.odsystem.R.drawable.navigation_left_type_standard_size_standard),
                    tint = scheme.basicText.getColor()
                )
            }
            PompiereTitle(
                text = "Profile Detail",
                scheme = scheme,
                style = ODSTextStyles.pompiereHeader
            )
            IconButton(onClick = {}) {
                ODSIcon(
                    iconModel = ODSIconModel(imageVector = Icons.Filled.MoreHoriz),
                    tint = scheme.basicText.getColor()
                )
            }
        }

        // 2. Main Hero Photo Card
        ODSBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 16.dp),
            border = ODSBorder(width = 1.dp, colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))),
            padding = ODSPadding(all = 8.dp)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = 12.dp
            ) {
                // Large Hero Image Container
                ODSBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    background = listOf(ODSColorModel(hexColor = cheddarSecondaryScheme.basicBackgroundSubtle)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = model.name.firstOrNull()?.toString() ?: "M",
                        style = ODSTextStyles.pompiereDisplayL,
                        color = scheme.basicText
                    )

                    // Top Right Floating Distance Badge
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        background = listOf(ODSColorModel(hexColor = HexColor(0x99000000))),
                        cornerRadius = ODSCorners(all = 12.dp),
                        padding = ODSPadding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = 4.dp
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(imageVector = Icons.Outlined.Navigation),
                                tint = Color.White
                            )
                            ODSText(
                                text = model.distance,
                                style = ODSTextStyles.bodySRegular,
                                color = HexColor(0xffffffff)
                            )
                        }
                    }

                    // Favorite Button Top Left
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.toggleFavorite() },
                        background = listOf(ODSColorModel(hexColor = HexColor(0x66000000))),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
                            ),
                            tint = if (isFavorite) scheme.functionalDestructiveStandard.getColor() else Color.White
                        )
                    }
                }

                // Name & Age with Verified Badge + Matched Preferences
                ODSColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    gap = 4.dp
                ) {
                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 6.dp
                    ) {
                        ODSText(
                            text = "${model.name}, ${model.age}",
                            style = ODSTextStyles.pompiereTitleM,
                            color = scheme.basicText
                        )
                        ODSIcon(
                            iconModel = ODSIconModel(imageVector = Icons.Outlined.Verified),
                            tint = scheme.functionalDestructiveStandard.getColor()
                        )
                    }
                    if (model.matchedPreferences.isNotBlank()) {
                        ODSText(
                            text = "✦ ${model.matchedPreferences}",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 3. About Section
        if (model.bio.isNotBlank()) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 6.dp
            ) {
                ODSText(
                    text = "About",
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                ODSText(
                    text = model.bio,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.basicTextRecessive
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        // 4. Preferences & Interest Tags
        if (model.tags.isNotEmpty()) {
            ODSColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                gap = 8.dp
            ) {
                val tagChunks = model.tags.chunked(3)
                tagChunks.forEach { rowTags ->
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowTags.forEach { tag ->
                            ODSBox(
                                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                                cornerRadius = ODSCorners(all = 16.dp),
                                border = ODSBorder(width = 1.dp, colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))),
                                padding = ODSPadding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                ODSText(
                                    text = tag,
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicText
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        // 5. Photo Gallery Moments Row
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = 8.dp
        ) {
            ODSText(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = "Moments",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(4) { idx ->
                    ODSBox(
                        modifier = Modifier
                            .size(width = 80.dp, height = 100.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        background = listOf(ODSColorModel(hexColor = orchidSecondaryScheme.basicBackgroundSubtle)),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "📸 ${idx + 1}",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicText
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 6. Bottom Sticky Call & Chat Action Buttons
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            gap = 12.dp
        ) {
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Chat (${model.chatRate}c)",
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL
                ),
                onClick = { onStartChat(model.id, model.name) }
            )
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Voice Call · ${model.ratePerMinute}c",
                    variant = ODSButtonVariant.PRIMARY,
                    size = ODSButtonSize.SMALL
                ),
                onClick = { onStartVoiceCall(model.id, model.name) }
            )
        }

        Spacer(Modifier.height(28.dp))
    }
}
