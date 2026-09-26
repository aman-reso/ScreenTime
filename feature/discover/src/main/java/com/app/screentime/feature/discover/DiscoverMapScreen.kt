package com.app.screentime.feature.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.components.DiscoverMapShimmer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.launch

/**
 * Discover Map Screen powered by Google Maps and backed by DiscoverMapViewModel.
 *
 * Rules:
 * 1. 100% ODS components for overlays and controls.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp with Funnel Sans font.
 * 5. All padding, margins, gaps, and corner radii use `ODSVariables`.
 * 6. Pure presentation: all mapping, data resolution, and state reside in DiscoverMapViewModel.
 */
@Composable
fun DiscoverMapScreen(
    onNavigateToChat: (modelId: String, modelName: String) -> Unit,
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialEmptyRadius: Boolean = false,
    viewModel: DiscoverMapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(initialEmptyRadius) {
        if (initialEmptyRadius) {
            viewModel.setEmptyRadius(true)
        }
    }

    if (uiState.isLoading && uiState.candidates.isEmpty()) {
        DiscoverMapShimmer(
            modifier = modifier,
            scheme = scheme
        )
        return
    }

    val coroutineScope = rememberCoroutineScope()
    val minZoom = 10f

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uiState.userLocation, 13f)
    }

    LaunchedEffect(uiState.userLocation) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(uiState.userLocation, 13f),
            durationMs = 500
        )
    }

    val currentZoom by remember { derivedStateOf { cameraPositionState.position.zoom } }
    val canZoomOut by remember { derivedStateOf { currentZoom > minZoom + 0.1f } }

    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.position.zoom }
            .collect { zoom ->
                if (zoom < minZoom) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.zoomTo(minZoom),
                        durationMs = 250
                    )
                }
            }
    }

    val mapProperties = remember {
        MapProperties(
            mapType = MapType.NORMAL,
            minZoomPreference = minZoom
        )
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false
        )
    }

    val selectedCandidate = uiState.selectedCandidate
    val selectedProfile = selectedCandidate?.profile

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground)),
        padding = ODSPadding(bottom = DSVariables.spacingComponent9)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            // User's own location marker
            MarkerComposable(
                state = rememberUpdatedMarkerState(position = uiState.userLocation),
                onClick = { true }
            ) {
                ODSBox(
                    modifier = Modifier.size(36.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicBackground))
                    ),
                    contentAlignment = Alignment.Center,
                    effect = ODSEffect(
                        elevations = listOf(
                            ODSElevation(
                                x = 0, y = 2, blur = 8, spread = 2,
                                color = HexColor(0x66D81B60),
                                type = ODSElevationType.DROP_SHADOW
                            )
                        )
                    )
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_map_pin,
                            contentDescription = "My Location"
                        ),
                        tint = scheme.basicTextOnAccent.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Real 5 km Search Radius Circle (when in Empty Radius view)
            if (uiState.isEmptyRadius) {
                Circle(
                    center = uiState.userLocation,
                    radius = 5000.0,
                    fillColor = scheme.basicAccent.getColor().copy(alpha = 0.08f),
                    strokeColor = scheme.basicAccent.getColor(),
                    strokeWidth = 4f,
                    strokePattern = listOf(Dash(20f), Gap(15f))
                )
            } else {
                // Genuine Map Profile Markers directly from ViewModel & Mapper
                uiState.candidates.forEach { candidate ->
                    val isSelected = candidate.profile.id == selectedProfile?.id
                    MarkerComposable(
                        state = rememberUpdatedMarkerState(position = candidate.latLng),
                        onClick = {
                            viewModel.selectCandidate(candidate)
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLng(candidate.latLng),
                                    durationMs = 500
                                )
                            }
                            true
                        }
                    ) {
                        ODSColumn(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ODSBox(
                                modifier = Modifier.size(if (isSelected) 56.dp else 48.dp),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                                border = ODSBorder(
                                    width = if (isSelected) ODSVariables.strokes3 else ODSVariables.strokes2,
                                    colorList = listOf(
                                        ODSColorModel(
                                            hexColor = if (isSelected) scheme.basicAccent else scheme.basicStroke
                                        )
                                    )
                                ),
                                clipContent = true
                            ) {
                                ODSImage(
                                    imageModel = ODSImageModel(
                                        url = candidate.profile.avatarUrl,
                                        contentDescription = candidate.profile.name
                                    ),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            if (candidate.profile.tags.isNotEmpty()) {
                                ODSBox(
                                    modifier = Modifier
                                        .offset(y = (-4).dp)
                                        .size(10.dp),
                                    background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                                    cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                    rotate = 45f
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── 2. Top Floating Controls: Profile Card / Search Bar ──────────
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    horizontal = ODSVariables.spacingLayout1,
                    vertical = ODSVariables.spacingComponent3
                ),
            gap = ODSVariables.spacingComponent4
        ) {
            if (uiState.isEmptyRadius || selectedProfile == null) {
                // Top Search Bar (in empty radius mode or when no candidate selected)
                ODSBox(
                    modifier = Modifier.fillMaxWidth(),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingComponent4
                    )
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSRow(
                            verticalAlignment = Alignment.CenterVertically,
                            gap = ODSVariables.spacingComponent4
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_search,
                                    contentDescription = "Search"
                                ),
                                tint = scheme.basicTextRecessive.getColor(),
                                modifier = Modifier.size(20.dp)
                            )
                            ODSText(
                                text = uiState.cityName,
                                style = ODSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                        }

                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_sliders,
                                contentDescription = "Filters"
                            ),
                            tint = scheme.basicTextRecessive.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                // Floating Profile Card for Currently Selected Profile
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                onNavigateToChat(
                                    selectedProfile.id,
                                    selectedProfile.name
                                )
                            }
                        ),
                    gap = ODSVariables.spacingLayout1,
                    padding = ODSPadding(all = ODSVariables.spacingComponent4),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    border = ODSBorder(
                        width = 2.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    effect = ODSEffect(
                        elevations = listOf(
                            ODSElevation(
                                x = 0,
                                y = 8,
                                blur = 16,
                                spread = 0,
                                color = HexColor(0x24000000),
                                type = ODSElevationType.DROP_SHADOW
                            )
                        )
                    )
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = selectedProfile.avatarUrl,
                            contentDescription = selectedProfile.name
                        ),
                        width = 80.dp,
                        height = 80.dp,
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        contentScale = ContentScale.Crop
                    )

                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = 6.dp,
                        verticalAlignment = Alignment.Top,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (selectedProfile.isOnline) {
                            ODSRow(
                                padding = ODSPadding(
                                    top = ODSVariables.spacingComponent2,
                                    bottom = ODSVariables.spacingComponent2,
                                    left = ODSVariables.spacingComponent3,
                                    right = ODSVariables.spacingComponent3
                                ),
                                cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                                horizontalAlignment = Alignment.Start,
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                            ) {
                                ODSText(
                                    text = "RECENTLY ACTIVE",
                                    style = ODSTextStyles.microcopyBold,
                                    color = scheme.basicTextDominant
                                )
                            }
                        }

                        val titleText = if (selectedProfile.age > 0) {
                            "${selectedProfile.name}, ${selectedProfile.age}"
                        } else {
                            selectedProfile.name
                        }

                        ODSText(
                            text = titleText,
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        val subtitleText = listOfNotNull(
                            selectedProfile.matchedPreferences.takeIf { it.isNotBlank() },
                            selectedProfile.distance.takeIf { it.isNotBlank() }
                        ).joinToString(" • ").ifBlank {
                            selectedProfile.location.ifBlank { "Nearby" }
                        }

                        ODSText(
                            text = subtitleText,
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    // Message / Direct Match Action Button
                    ODSRow(
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        effect = ODSEffect(
                            elevations = listOf(
                                ODSElevation(
                                    x = 0,
                                    y = 6,
                                    blur = 14,
                                    spread = 0,
                                    color = HexColor(0x38D81B60),
                                    type = ODSElevationType.DROP_SHADOW
                                )
                            )
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    onNavigateToChat(
                                        selectedProfile.id,
                                        selectedProfile.name
                                    )
                                }
                            )
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(drawableRes = R.drawable.ic_message_circle),
                            tint = scheme.basicTextOnAccent.getColor(),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // ── 3. Floating Map Controls (Re-center, Zoom In & Zoom Out) ─────
        ODSColumn(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = ODSVariables.spacingLayout1,
                    bottom = ODSVariables.spacingComponent9
                ),
            gap = ODSVariables.spacingComponent3,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Re-center on user
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable {
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(uiState.userLocation, 13f),
                                durationMs = 500
                            )
                        }
                    },
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center,
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0, y = 2, blur = 8, spread = 0,
                            color = HexColor(0x1A000000),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.ic_map_pin),
                    tint = scheme.basicAccent.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Zoom In (+)
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable {
                        coroutineScope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.zoomIn(),
                                durationMs = 300
                            )
                        }
                    },
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center,
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0, y = 2, blur = 8, spread = 0,
                            color = HexColor(0x1A000000),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(drawableRes = R.drawable.ic_plus),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Zoom Out (−)
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = canZoomOut,
                        onClick = {
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.zoomOut(),
                                    durationMs = 300
                                )
                            }
                        }
                    ),
                background = listOf(
                    ODSColorModel(
                        hexColor = if (canZoomOut) scheme.basicBackgroundCard
                        else scheme.basicBackgroundCardSubtle
                    )
                ),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(
                        ODSColorModel(
                            hexColor = if (canZoomOut) scheme.basicStroke
                            else scheme.basicStrokeSubtle
                        )
                    )
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center,
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0, y = 2, blur = 8, spread = 0,
                            color = HexColor(0x1A000000),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                ODSText(
                    text = "−",
                    style = ODSTextStyles.bodyMBold,
                    color = if (canZoomOut) scheme.basicText else scheme.basicTextRecessive
                )
            }
        }

        // ── 4. Bottom Empty State Card ───────────────────────────────────
        if (uiState.isEmptyRadius || uiState.candidates.isEmpty()) {
            ODSColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingLayout2
                    ),
                gap = ODSVariables.spacingComponent6,
                padding = ODSPadding(all = ODSVariables.spacingLayout1),
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                effect = ODSEffect(
                    elevations = listOf(
                        ODSElevation(
                            x = 0, y = 8, blur = 16, spread = 0,
                            color = HexColor(0x24000000),
                            type = ODSElevationType.DROP_SHADOW
                        )
                    )
                )
            ) {
                // Info Section
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ODSRow(
                            padding = ODSPadding(
                                top = ODSVariables.spacingComponent2,
                                bottom = ODSVariables.spacingComponent2,
                                left = ODSVariables.spacingComponent3,
                                right = ODSVariables.spacingComponent3
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                            horizontalAlignment = Alignment.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
                        ) {
                            ODSText(
                                text = "EMPTY SEARCH RADIUS",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextDominant
                            )
                        }

                        ODSText(
                            text = "Radius: 5 km",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextDominant
                        )
                    }

                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No nearby people found",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "It's pretty quiet within 5 km right now. Try expanding your search distance to discover people in neighboring districts.",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    ODSRow(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    viewModel.setEmptyRadius(false)
                                    viewModel.loadCandidates()
                                    coroutineScope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(uiState.userLocation, 13f),
                                            durationMs = 500
                                        )
                                    }
                                }
                            ),
                        gap = ODSVariables.spacingComponent3,
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent5,
                            bottom = ODSVariables.spacingComponent5,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        effect = ODSEffect(
                            elevations = listOf(
                                ODSElevation(
                                    x = 0, y = 6, blur = 14, spread = 0,
                                    color = HexColor(0x38D81B60),
                                    type = ODSElevationType.DROP_SHADOW
                                )
                            )
                        )
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_arrow_up_right,
                                contentDescription = "Expand Distance"
                            ),
                            tint = scheme.basicTextOnAccent.getColor(),
                            modifier = Modifier.size(ODSVariables.spacingComponent5)
                        )
                        ODSText(
                            text = "Expand Distance",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextOnAccent
                        )
                    }

                    // Secondary: Change Location
                    ODSRow(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* Open location picker */ }
                        ),
                        padding = ODSPadding(
                            top = ODSVariables.spacingComponent5,
                            bottom = ODSVariables.spacingComponent5,
                            left = ODSVariables.spacingLayout1,
                            right = ODSVariables.spacingLayout1
                        ),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ODSText(
                            text = "Change Location",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextRecessive
                        )
                    }
                }
            }
        }
    }
}
