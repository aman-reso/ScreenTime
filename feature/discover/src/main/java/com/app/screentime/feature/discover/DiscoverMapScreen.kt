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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.app.screentime.feature.discover.components.DiscoverMapShimmer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
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
 * Genuine Dark Map Style JSON matching Zona theme.
 */
private const val DARK_MAP_STYLE = """
[
  {"elementType": "geometry", "stylers": [{"color": "#181326"}]},
  {"elementType": "labels.text.fill", "stylers": [{"color": "#8F82A4"}]},
  {"elementType": "labels.text.stroke", "stylers": [{"color": "#181326"}]},
  {"featureType": "administrative.locality", "elementType": "labels.text.fill", "stylers": [{"color": "#E1D4EC"}]},
  {"featureType": "poi", "elementType": "labels.text.fill", "stylers": [{"color": "#76658E"}]},
  {"featureType": "poi.park", "elementType": "geometry", "stylers": [{"color": "#201833"}]},
  {"featureType": "road", "elementType": "geometry", "stylers": [{"color": "#2A1E40"}]},
  {"featureType": "road", "elementType": "geometry.stroke", "stylers": [{"color": "#1D152C"}]},
  {"featureType": "road.highway", "elementType": "geometry", "stylers": [{"color": "#3B2658"}]},
  {"featureType": "transit", "elementType": "geometry", "stylers": [{"color": "#26193D"}]},
  {"featureType": "water", "elementType": "geometry", "stylers": [{"color": "#0F0B18"}]}
]
"""

data class MapProfile(
    val id: String,
    val name: String,
    val age: Int,
    val avatarUrl: String,
    val activityText: String,
    val distanceText: String,
    val isRecentlyActive: Boolean,
    val hasCoralBadge: Boolean,
    val latLng: LatLng
)

/**
 * Discover Map Screen powered by genuine Google Maps.
 *
 * Rules:
 * 1. 100% ODS components for overlays and controls.
 * 2. Colors picked from `scheme: ODSTheme`.
 * 3. Compact button sizing.
 * 4. Maximum text size 16sp with Funnel Sans font.
 * 5. All padding, margins, gaps, and corner radii use `ODSVariables`.
 */
@Composable
fun DiscoverMapScreen(
    onNavigateToChat: (modelId: String, modelName: String) -> Unit,
    onNavigateToList: () -> Unit = {},
    onNavigateToProfile: (userId: String, userName: String) -> Unit = { _, _ -> },
    onOpenTerms: () -> Unit = {},
    modifier: Modifier = Modifier,
    scheme: ODSTheme = zonaODSTheme,
    initialEmptyRadius: Boolean = false,
    isLoading: Boolean = false
) {
    if (isLoading) {
        DiscoverMapShimmer(
            modifier = modifier,
            scheme = scheme
        )
        return
    }

    var isEmptyRadius by remember { mutableStateOf(initialEmptyRadius) }
    val coroutineScope = rememberCoroutineScope()

    // ── Radius restriction ───────────────────────────────────────────────
    // Zoom 10f ≈ 20 km radius on a standard phone screen.
    // The SDK minZoomPreference blocks pinch-zoom; LaunchedEffect guards
    // against edge-cases (e.g. rapid two-finger swipe past the limit).
    val minZoom = 10f

    // Center user location in Manhattan, NYC
    val userLocation = remember { LatLng(40.7306, -73.9910) }

    val profiles = remember {
        listOf(
            MapProfile(
                id = "chloe_22",
                name = "Chloe",
                age = 22,
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop",
                activityText = "Active 5m ago",
                distanceText = "1.2 km away",
                isRecentlyActive = true,
                hasCoralBadge = true,
                latLng = LatLng(40.7240, -73.9980)
            ),
            MapProfile(
                id = "marcus_25",
                name = "Marcus",
                age = 25,
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=600&auto=format&fit=crop",
                activityText = "Active 15m ago",
                distanceText = "2.8 km away",
                isRecentlyActive = false,
                hasCoralBadge = false,
                latLng = LatLng(40.7484, -73.9857)
            ),
            MapProfile(
                id = "sophia_23",
                name = "Sophia",
                age = 23,
                avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?q=80&w=600&auto=format&fit=crop",
                activityText = "Active 1h ago",
                distanceText = "3.4 km away",
                isRecentlyActive = false,
                hasCoralBadge = false,
                latLng = LatLng(40.7128, -74.0060)
            ),
            MapProfile(
                id = "elena_24",
                name = "Elena",
                age = 24,
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=600&auto=format&fit=crop",
                activityText = "Active now",
                distanceText = "4.1 km away",
                isRecentlyActive = true,
                hasCoralBadge = true,
                latLng = LatLng(40.7580, -73.9855)
            )
        )
    }

    var selectedProfile by remember { mutableStateOf(profiles[0]) }
    var showMatchRulesDialog by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 13f)
    }

    // Live zoom level — drives Zoom Out button enabled/disabled state
    val currentZoom by remember { derivedStateOf { cameraPositionState.position.zoom } }
    val canZoomOut by remember { derivedStateOf { currentZoom > minZoom + 0.1f } }

    // Safety guard: if pinch-gesture sneaks past minZoomPreference, snap back
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
            minZoomPreference = minZoom  // SDK-level 20 km radius cap
            // Note: Custom dark style removed — MapStyleOptions can silently blank all tiles
            // if the JSON fails to parse at runtime. The map now uses default tiles reliably.
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
                state = rememberUpdatedMarkerState(position = userLocation),
                onClick = { true }
            ) {
                ODSBox(
                    modifier = Modifier.size(48.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    border = ODSBorder(
                        width = ODSVariables.strokes2,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicAccent))
                    ),
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop",
                            contentDescription = "My Location"
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Real 5 km Search Radius Circle (when in Empty Radius view)
            if (isEmptyRadius) {
                Circle(
                    center = userLocation,
                    radius = 5000.0,
                    fillColor = scheme.basicAccent.getColor().copy(alpha = 0.08f),
                    strokeColor = scheme.basicAccent.getColor(),
                    strokeWidth = 4f,
                    strokePattern = listOf(Dash(20f), Gap(15f))
                )
            } else {
                // Genuine Map Profile Markers
                profiles.forEach { profile ->
                    val isSelected = profile.id == selectedProfile.id
                    MarkerComposable(
                        state = rememberUpdatedMarkerState(position = profile.latLng),
                        onClick = {
                            selectedProfile = profile
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLng(profile.latLng),
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
                                        url = profile.avatarUrl,
                                        contentDescription = profile.name
                                    ),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            if (profile.hasCoralBadge) {
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

        // ── 2. Top Floating Controls: Profile Card / Search + Filters ────
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
            if (isEmptyRadius) {
                // Top Search Bar (in empty radius mode)
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
                                text = "Manhattan, NY",
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
                // Floating Profile Card for Currently Selected Profile (Matching Figma node-id 10-1101)
                ODSRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onNavigateToProfile(selectedProfile.id, selectedProfile.name) }
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
                        if (selectedProfile.isRecentlyActive) {
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

                        ODSText(
                            text = "${selectedProfile.name}, ${selectedProfile.age}",
                            style = ODSTextStyles.bodyMBold,
                            color = scheme.basicTextDominant
                        )

                        ODSText(
                            text = "${selectedProfile.activityText} • ${selectedProfile.distanceText}",
                            style = ODSTextStyles.microcopyRegular,
                            color = scheme.basicTextRecessive
                        )
                    }

                    // Message / Match Action Button
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
                                onClick = { showMatchRulesDialog = true }
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

            // Filter Chips Row: [ Map | List ] -- [< 5 km] -- [Online]
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                gap = ODSVariables.spacingComponent3
            ) {
                // Map / List Toggle
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(all = ODSVariables.spacingComponent1)
                ) {
                    ODSRow(verticalAlignment = Alignment.CenterVertically) {
                        // Map Active
                        ODSBox(
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent2
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "Map",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicBackground
                            )
                        }

                        // List Inactive
                        ODSBox(
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onNavigateToList
                            ),
                            padding = ODSPadding(
                                horizontal = ODSVariables.spacingLayout1,
                                vertical = ODSVariables.spacingComponent2
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "List",
                                style = ODSTextStyles.bodySBold,
                                color = scheme.basicTextRecessive
                            )
                        }
                    }
                }

                // "< 5 km" Filter Chip (toggles empty radius demonstration)
                ODSBox(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isEmptyRadius = !isEmptyRadius
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        userLocation,
                                        if (isEmptyRadius) 12f else 13f
                                    ),
                                    durationMs = 500
                                )
                            }
                        }
                    ),
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = if (isEmptyRadius) scheme.basicAccent else scheme.basicStroke))
                    ),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingComponent4,
                        vertical = ODSVariables.spacingComponent3
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "< 5 km",
                        style = ODSTextStyles.bodySBold,
                        color = if (isEmptyRadius) scheme.basicAccent else scheme.basicText
                    )
                }

                // "Online" Filter Chip
                ODSBox(
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                    border = ODSBorder(
                        width = ODSVariables.strokes1,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                    ),
                    padding = ODSPadding(
                        horizontal = ODSVariables.spacingComponent4,
                        vertical = ODSVariables.spacingComponent3
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSText(
                        text = "Online",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicText
                    )
                }
            }
        }

        // ── 3. Floating Map Controls (Re-center, Zoom In & Zoom Out) ─────
        ODSColumn(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = ODSVariables.spacingLayout1,
                    bottom = ODSVariables.spacingComponent9  // 40dp — keeps clear of bottom card
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
                                CameraUpdateFactory.newLatLngZoom(userLocation, 13f),
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
                            cameraPositionState.animate(CameraUpdateFactory.zoomIn(), durationMs = 300)
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
            // Zoom Out (−) — disabled at 20 km radius limit
            ODSBox(
                modifier = Modifier
                    .size(44.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = canZoomOut,
                        onClick = {
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomOut(), durationMs = 300)
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

        // ── 4. Bottom Empty State Card (Figma node 29-161) ───────────────
        if (isEmptyRadius) {
            ODSColumn(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        horizontal = ODSVariables.spacingLayout1,
                        vertical = ODSVariables.spacingLayout2
                    ),
                gap = ODSVariables.spacingComponent6,          // 20dp
                padding = ODSPadding(all = ODSVariables.spacingLayout1), // 16dp
                cornerRadius = ODSCorners(all = ODSVariables.radiusLarge), // 24dp
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
                // ── Info Section ─────────────────────────────────────────
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent3,      // 8dp
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Header row: badge + radius label
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // "EMPTY SEARCH RADIUS" badge
                        ODSRow(
                            padding = ODSPadding(
                                top = ODSVariables.spacingComponent2,     // 4dp
                                bottom = ODSVariables.spacingComponent2,  // 4dp
                                left = ODSVariables.spacingComponent3,    // 8dp
                                right = ODSVariables.spacingComponent3    // 8dp
                            ),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusSmall), // 8dp
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

                        // "Radius: 5 km" label
                        ODSText(
                            text = "Radius: 5 km",
                            style = ODSTextStyles.bodySBold,
                            color = scheme.basicTextDominant
                        )
                    }

                    // Title
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No nearby people found",
                        style = ODSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    // Description
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = "It's pretty quiet within 5 km right now. Try expanding your search distance to discover people in neighboring districts.",
                        style = ODSTextStyles.bodySRegular,
                        color = scheme.basicTextRecessive
                    )
                }

                // ── Action Buttons Row ───────────────────────────────────
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    gap = ODSVariables.spacingComponent4,      // 12dp
                    horizontalAlignment = Alignment.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Primary: Expand Distance
                    ODSRow(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    isEmptyRadius = false
                                    coroutineScope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(userLocation, 13f),
                                            durationMs = 500
                                        )
                                    }
                                }
                            ),
                        gap = ODSVariables.spacingComponent3,  // 8dp
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
                            modifier = Modifier.size(ODSVariables.spacingComponent5) // 16dp
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
                            onClick = { /* TODO: Open location picker */ }
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

        // ── 5. "Before You Match..." Dialog ──────────────────────────────
        if (showMatchRulesDialog) {
            BeforeYouMatchDialog(
                onAccept = {
                    showMatchRulesDialog = false
                    onNavigateToChat(selectedProfile.id, selectedProfile.name)
                },
                onDecline = {
                    showMatchRulesDialog = false
                },
                onReadTerms = {
                    showMatchRulesDialog = false
                    onOpenTerms()
                }
            )
        }
    }
}
