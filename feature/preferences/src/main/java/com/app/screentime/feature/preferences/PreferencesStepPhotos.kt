package com.app.screentime.feature.preferences

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.io.File
import java.io.FileOutputStream

/**
 * Step 2: Photos Gallery Setup.
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
            text = "Add your photos (2 photos)",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Upload 2 photos so potential matches can see your genuine vibe.",
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
    isLoading: Boolean = false,
    onAddClick: () -> Unit,
    onRemoveClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val slotHeight = if (isLarge) 210.dp else 105.dp
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
    } else if (isLoading) {
        ODSBox(
            modifier = modifier
                .height(slotHeight),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = slotRadius),
            border = ODSBorder(
                width = ODSVariables.strokes2,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary))
            ),
            contentAlignment = Alignment.Center
        ) {
            ODSLoadingSpinner(
                scheme = scheme,
                props = ODSLoadingSpinnerProps(
                    size = if (isLarge) ODSLoadingSpinnerSize.SMALL else ODSLoadingSpinnerSize.SMALL
                )
            )
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
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_plus,
                        contentDescription = "Add Photo"
                    ),
                    tint = scheme.basicText.getColor(),
                    modifier = Modifier.size(if (isLarge) 20.dp else 16.dp)
                )
            }
        }
    }
}

@Composable
fun PreferencesStepPhotos(
    uploadedPhotos: List<String>,
    isUploading: Boolean = false,
    error: String? = null,
    scheme: ODSTheme,
    onAddPhotos: (List<Uri>) -> Unit,
    onRemovePhoto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val remainingSlots = (2 - uploadedPhotos.size).coerceAtLeast(0)
            if (remainingSlots > 0) {
                onAddPhotos(uris.take(remainingSlots))
            } else {
                context.showODSToast("You can upload up to 2 photos")
            }
        }
    }

    var showCustomCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCustomCamera = true
        } else {
            context.showODSToast("Camera permission is required to capture photos")
        }
    }

    val onLaunchCamera = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            showCustomCamera = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (showCustomCamera) {
        FrontCameraCaptureDialog(
            onDismiss = { showCustomCamera = false },
            onPhotoCaptured = { uriStr ->
                showCustomCamera = false
                if (uploadedPhotos.size < 2) {
                    onAddPhotos(listOf(Uri.parse(uriStr)))
                } else {
                    context.showODSToast("You can upload up to 2 photos")
                }
            },
            scheme = scheme,
            title = "Capture Front Photo"
        )
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent5
    ) {
        PhotosHeader(scheme = scheme)

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
                isLoading = isUploading && uploadedPhotos.isEmpty(),
                onAddClick = {
                    if (uploadedPhotos.size < 2) {
                        photoPickerLauncher.launch("image/*")
                    } else {
                        context.showODSToast("You can upload up to 2 photos")
                    }
                },
                onRemoveClick = { onRemovePhoto(0) },
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )

            PhotoSlotItem(
                photoUrl = uploadedPhotos.getOrNull(1),
                isMain = false,
                isLarge = true,
                isLoading = isUploading && uploadedPhotos.size == 1,
                onAddClick = {
                    if (uploadedPhotos.size < 2) {
                        photoPickerLauncher.launch("image/*")
                    } else {
                        context.showODSToast("You can upload up to 2 photos")
                    }
                },
                onRemoveClick = { onRemovePhoto(1) },
                scheme = scheme,
                modifier = Modifier.weight(1f)
            )
        }

        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            gap = ODSVariables.spacingComponent3,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = if (isUploading) "Uploading..." else "Choose Gallery",
                    buttonIcon = ODSIconModel(
                        drawableRes = R.drawable.ic_image,
                        contentDescription = "Gallery"
                    ),
                    leftIcon = true,
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL,
                    disabled = isUploading
                ),
                onClick = {
                    if (uploadedPhotos.size < 2) {
                        photoPickerLauncher.launch("image/*")
                    } else {
                        context.showODSToast("You can upload up to 2 photos")
                    }
                }
            )

            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = ODSButtonProps(
                    label = "Take Photo",
                    buttonIcon = ODSIconModel(
                        drawableRes = R.drawable.ic_camera,
                        contentDescription = "Camera"
                    ),
                    leftIcon = true,
                    variant = ODSButtonVariant.SECONDARY,
                    size = ODSButtonSize.SMALL,
                    disabled = isUploading
                ),
                onClick = {
                    if (uploadedPhotos.size < 2) {
                        onLaunchCamera()
                    } else {
                        context.showODSToast("You can upload up to 2 photos")
                    }
                }
            )
        }

        if (!error.isNullOrBlank()) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2,
                padding = ODSPadding(all = ODSVariables.spacingComponent3),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveSubtle)),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_close,
                        contentDescription = "Error"
                    ),
                    tint = scheme.functionalDestructiveStandard.getColor(),
                    modifier = Modifier.size(16.dp)
                )
                ODSText(
                    text = error,
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.functionalDestructiveStandard,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
