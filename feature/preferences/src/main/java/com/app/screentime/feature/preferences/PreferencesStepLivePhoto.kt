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
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.ODSElevationType
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Step 10: Live Photo Verification.
 * Recreated with 100% ODS components strictly following GEMINI.md rules.
 */

@Composable
fun LivePhotoHeader(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent2
    ) {
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Take a live selfie",
            style = ODSTextStyles.bodyMBold,
            color = scheme.basicTextDominant
        )
        ODSText(
            modifier = Modifier.fillMaxWidth(),
            text = "Quick selfie to prove you are really you. This unlocks the Coral Verified badge on your profile!",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun LivePhotoPreviewCard(
    livePhotoUri: String?,
    onCaptureClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCaptureClick
            ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusExtraLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(
                ODSColorModel(
                    hexColor = if (livePhotoUri != null) scheme.basicAccent else scheme.basicStroke
                )
            )
        ),
        clipContent = true,
        contentAlignment = Alignment.Center
    ) {
        if (livePhotoUri != null) {
            ODSImage(
                imageModel = ODSImageModel(
                    url = livePhotoUri,
                    contentDescription = "Live Selfie"
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Verified Badge Overlay
            ODSRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(ODSVariables.spacingLayout1),
                gap = ODSVariables.spacingComponent2,
                padding = ODSPadding(
                    horizontal = ODSVariables.spacingComponent4,
                    vertical = ODSVariables.spacingComponent2
                ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_shield_check,
                        contentDescription = "Verified"
                    ),
                    tint = scheme.basicBackgroundCard.getColor(),
                    modifier = Modifier.size(16.dp)
                )
                ODSText(
                    text = "Selfie Verified",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicBackgroundCard
                )
            }
        } else {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent3,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ODSBox(
                    modifier = Modifier.size(64.dp),
                    background = listOf(ODSColorModel(hexColor = scheme.basicAccentSecondary)),
                    cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_camera,
                            contentDescription = "Camera"
                        ),
                        tint = scheme.basicAccent.getColor(),
                        modifier = Modifier.size(28.dp)
                    )
                }

                ODSText(
                    text = "Tap to open camera",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicAccent
                )

                ODSText(
                    text = "Hold phone at eye level in good lighting",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicTextRecessive
                )
            }
        }
    }
}

@Composable
fun LivePhotoTrustNote(
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent3,
        padding = ODSPadding(all = ODSVariables.spacingLayout1),
        cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                drawableRes = R.drawable.ic_shield,
                contentDescription = "Privacy"
            ),
            tint = scheme.basicAccent.getColor(),
            modifier = Modifier.size(20.dp)
        )
        ODSText(
            modifier = Modifier.weight(1f),
            text = "Your verification selfie is kept strictly private and will never be published to your profile feed.",
            style = ODSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun PreferencesStepLivePhoto(
    livePhotoUri: String?,
    onCaptureLivePhoto: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            try {
                val file = java.io.File(context.cacheDir, "live_photo_${System.currentTimeMillis()}.jpg")
                java.io.FileOutputStream(file).use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                }
                onCaptureLivePhoto(android.net.Uri.fromFile(file).toString())
            } catch (e: Exception) {
                // File writing failed
            }
        }
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent6
    ) {
        LivePhotoHeader(scheme = scheme)
        LivePhotoPreviewCard(
            livePhotoUri = livePhotoUri,
            onCaptureClick = { cameraLauncher.launch(null) },
            scheme = scheme
        )
        LivePhotoTrustNote(scheme = scheme)
    }
}
