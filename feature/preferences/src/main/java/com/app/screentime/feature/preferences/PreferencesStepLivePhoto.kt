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
import java.io.File
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

import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize

/**
 * Step 3 (Live Photo Verification): ML Kit + ArcFace Face Verification.
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
            text = "Quick selfie to verify your face. Hold the phone at eye level in good lighting.",
            style = ODSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
fun LivePhotoPreviewCard(
    livePhotoUri: String?,
    isVerifying: Boolean,
    isVerified: Boolean,
    verificationError: String?,
    onCaptureClick: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val borderColor = when {
        isVerifying -> scheme.basicAccent
        isVerified -> scheme.basicAccent
        verificationError != null -> scheme.functionalDestructiveStandard
        livePhotoUri != null -> scheme.basicAccent
        else -> scheme.basicStroke
    }

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCaptureClick
            ),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = ODSVariables.radiusExtraLarge),
        border = ODSBorder(
            width = ODSVariables.strokes2,
            colorList = listOf(ODSColorModel(hexColor = borderColor))
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

            // Status Badge Overlay
            if (isVerifying) {
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
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                    )
                    ODSText(
                        text = "Verifying selfie...",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextDominant
                    )
                }
            } else if (verificationError != null) {
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
                    background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_alert_triangle,
                            contentDescription = "Mismatch"
                        ),
                        tint = scheme.basicBackgroundCard.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                    ODSText(
                        text = "Tap to retake selfie",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicBackgroundCard
                    )
                }
            } else {
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
                    background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_refresh_cw,
                            contentDescription = "Retake"
                        ),
                        tint = scheme.basicTextDominant.getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                    ODSText(
                        text = "Tap to retake",
                        style = ODSTextStyles.microcopyBold,
                        color = scheme.basicTextDominant
                    )
                }
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
                    text = "Tap to open front camera",
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
    isVerifyingFace: Boolean = false,
    isFaceVerified: Boolean = false,
    verificationError: String? = null,
    onCaptureLivePhoto: (String) -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showCustomCamera by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCustomCamera = true
        } else {
            context.showODSToast("Camera permission is required to take a selfie")
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
                onCaptureLivePhoto(uriStr)
            },
            scheme = scheme
        )
    }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = ODSVariables.spacingComponent5
    ) {
        LivePhotoHeader(scheme = scheme)

        LivePhotoPreviewCard(
            livePhotoUri = livePhotoUri,
            isVerifying = isVerifyingFace,
            isVerified = isFaceVerified,
            verificationError = verificationError,
            onCaptureClick = onLaunchCamera,
            scheme = scheme
        )

        if (verificationError != null) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                gap = ODSVariables.spacingComponent2,
                padding = ODSPadding(all = ODSVariables.spacingComponent3),
                cornerRadius = ODSCorners(all = ODSVariables.radiusMedium),
                background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle)),
                border = ODSBorder(
                    width = ODSVariables.strokes1,
                    colorList = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard))
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_alert_triangle,
                        contentDescription = "Error"
                    ),
                    tint = scheme.functionalDestructiveStandard.getColor(),
                    modifier = Modifier.size(18.dp)
                )
                ODSText(
                    modifier = Modifier.weight(1f),
                    text = verificationError,
                    style = ODSTextStyles.bodySRegular,
                    color = scheme.functionalDestructiveStandard
                )
            }
        }

        LivePhotoTrustNote(scheme = scheme)
    }
}
