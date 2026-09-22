package com.app.screentime.feature.preferences

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 5: Photos & Location Setup.
 */
@Composable
fun PreferencesStepFive(
    uploadedPhotos: List<String>,
    scheme: ODSTheme,
    onAddPhoto: (String) -> Unit,
    onRemovePhoto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri -> onAddPhoto(uri.toString()) }
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent5
    ) {
        // ── 1. Heading ─────────────────────────────────────────────────────
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent2
        ) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Add your beautiful photos",
                style = ODSTextStyles.bodyMBold,
                color = scheme.basicTextDominant
            )
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = "Upload at least 2 photos to continue your profile setup.",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }

        // ── 2. Top Row: 2 Large Photo Slots ────────────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Slot 1 (Primary / MAIN Photo)
            if (uploadedPhotos.isNotEmpty()) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = uploadedPhotos[0],
                            contentDescription = "Main Photo"
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // "MAIN" Badge on Top Left
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusSmall),
                        padding = ODSPadding(
                            horizontal = ODSVariables.spacingComponent3,
                            vertical = ODSVariables.spacingComponent1
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSText(
                            text = "MAIN",
                            style = ODSTextStyles.microcopyBold,
                            color = scheme.basicBackgroundCard
                        )
                    }

                    // Remove circular button on Bottom Right
                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(28.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onRemovePhoto(0) }
                            ),
                        background = listOf(ODSColorModel(hexColor = scheme.basicTextDominant)),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_close,
                                contentDescription = "Remove"
                            ),
                            tint = scheme.basicBackgroundCard.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                EmptyPhotoSlot(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp),
                    scheme = scheme,
                    isLarge = true,
                    onClick = { photoPickerLauncher.launch("image/*") }
                )
            }

            // Slot 2
            if (uploadedPhotos.size > 1) {
                ODSBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                    clipContent = true
                ) {
                    ODSImage(
                        imageModel = ODSImageModel(
                            url = uploadedPhotos[1],
                            contentDescription = "Photo 2"
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    ODSBox(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(28.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onRemovePhoto(1) }
                            ),
                        background = listOf(ODSColorModel(hexColor = scheme.basicTextDominant)),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        contentAlignment = Alignment.Center
                    ) {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                drawableRes = R.drawable.ic_close,
                                contentDescription = "Remove"
                            ),
                            tint = scheme.basicBackgroundCard.getColor(),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                EmptyPhotoSlot(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp),
                    scheme = scheme,
                    isLarge = true,
                    onClick = { photoPickerLauncher.launch("image/*") }
                )
            }
        }

        // ── 3. Bottom Row: 3 Smaller Photo Slots ───────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 2..4) {
                if (uploadedPhotos.size > i) {
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(105.dp),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                        clipContent = true
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = uploadedPhotos[i],
                                contentDescription = "Photo ${i + 1}"
                            ),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        ODSBox(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(6.dp)
                                .size(24.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { onRemovePhoto(i) }
                                ),
                            background = listOf(ODSColorModel(hexColor = scheme.basicTextDominant)),
                            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    drawableRes = R.drawable.ic_close,
                                    contentDescription = "Remove"
                                ),
                                tint = scheme.basicBackgroundCard.getColor(),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                } else {
                    EmptyPhotoSlot(
                        modifier = Modifier
                            .weight(1f)
                            .height(105.dp),
                        scheme = scheme,
                        isLarge = false,
                        onClick = { photoPickerLauncher.launch("image/*") }
                    )
                }
            }
        }

        // ── 4. Action Buttons: Gallery & Camera ────────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Choose Gallery Button
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { photoPickerLauncher.launch("image/*") }
                    ),
                height = 48.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = ODSVariables.spacingComponent2
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_image,
                            contentDescription = "Gallery"
                        ),
                        tint = scheme.basicTextDominant.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                    ODSText(
                        text = "Choose Gallery",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextDominant
                    )
                }
            }

            // Take Photo Button
            ODSBox(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { photoPickerLauncher.launch("image/*") }
                    ),
                height = 48.dp,
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                border = ODSBorder(
                    width = ODSVariables.strokes2,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
                ),
                contentAlignment = Alignment.Center
            ) {
                ODSRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    gap = ODSVariables.spacingComponent2
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_camera,
                            contentDescription = "Camera"
                        ),
                        tint = scheme.basicTextDominant.getColor(),
                        modifier = Modifier.size(18.dp)
                    )
                    ODSText(
                        text = "Take Photo",
                        style = ODSTextStyles.bodySBold,
                        color = scheme.basicTextDominant
                    )
                }
            }
        }

        // ── 5. Location & Safety Info Box ──────────────────────────────────
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            padding = ODSPadding(
                horizontal = ODSVariables.spacingLayout1,
                vertical = ODSVariables.spacingComponent4
            ),
            cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
            ),
            horizontalAlignment = Alignment.Start,
            verticalAlignment = Alignment.CenterVertically,
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_map_pin,
                    contentDescription = "Location"
                ),
                tint = scheme.basicAccent.getColor(),
                modifier = Modifier.size(20.dp)
            )
            ODSText(
                modifier = Modifier.weight(1f),
                text = "We use your location to connect you with nearby matches. Exact coordinates are never shared.",
                style = ODSTextStyles.microcopyRegular,
                color = scheme.basicTextRecessive
            )
        }
    }
}

@Composable
private fun EmptyPhotoSlot(
    scheme: ODSTheme,
    isLarge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = if (isLarge) ODSVariables.radiusLarge else ODSVariables.radiusMedium),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStroke))
        ),
        contentAlignment = Alignment.Center
    ) {
        ODSBox(
            modifier = Modifier.size(if (isLarge) 40.dp else 32.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
            cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(
                    drawableRes = R.drawable.ic_plus,
                    contentDescription = "Add Photo"
                ),
                tint = scheme.basicAccent.getColor(),
                modifier = Modifier.size(if (isLarge) 20.dp else 16.dp)
            )
        }
    }
}
