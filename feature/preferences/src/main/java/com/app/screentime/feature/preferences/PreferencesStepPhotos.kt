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
 * Step 9: Photos Gallery Setup.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

@Composable
fun PhotosHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Add your best photos",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Upload at least 2 photos so potential matches can see your genuine vibe.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun PhotoSlotItem(
    photoUrl: String?,
    isMain: Boolean,
    isLarge: Boolean,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val slotHeight = if (isLarge) 175.dp else 105.dp
    val slotRadius = if (isLarge) ODSVariables.radiusLarge else ODSVariables.radiusMedium

    if (photoUrl != null) {
        ODSBox(
            modifier = modifier.height(slotHeight),
            cornerRadius = ODSCorners(all = slotRadius),
            clipContent = true
        ) {
            ODSImage(
                imageModel = ODSImageModel(
                    url = photoUrl,
                    contentDescription = if (isMain) "Main Photo" else "Photo"
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (isMain) {
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
            }

            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onRemoveClick
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
        ODSBox(
            modifier = modifier
                .height(slotHeight)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onAddClick
                ),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = slotRadius),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
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
}

@Composable
fun PreferencesStepPhotos(
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
        PhotosHeader(scheme = scheme)

        // Top Row: 2 Primary Photo Slots
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PhotoSlotItem(
                photoUrl = uploadedPhotos.getOrNull(0),
                isMain = true,
                isLarge = true,
                onAddClick = { photoPickerLauncher.launch("image/*") },
                onRemoveClick = { onRemovePhoto(0) },
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )

            PhotoSlotItem(
                photoUrl = uploadedPhotos.getOrNull(1),
                isMain = false,
                isLarge = true,
                onAddClick = { photoPickerLauncher.launch("image/*") },
                onRemoveClick = { onRemovePhoto(1) },
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
        }

        // Bottom Row: 3 Smaller Photo Slots
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 2..4) {
                PhotoSlotItem(
                    photoUrl = uploadedPhotos.getOrNull(i),
                    isMain = false,
                    isLarge = false,
                    onAddClick = { photoPickerLauncher.launch("image/*") },
                    onRemoveClick = { onRemovePhoto(i) },
                    scheme = scheme,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Action Buttons Row: Gallery & Camera
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent4,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
    }
}
