package com.app.screentime.wallpaper.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import com.app.screentime.wallpaper.model.Wallpaper
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Full-screen wallpaper viewer with zoom and pan gestures
 */
@Composable
fun FullScreenWallpaperViewer(
    wallpaper: Wallpaper,
    onDismiss: () -> Unit,
    onSetWallpaper: () -> Unit,
    onDownload: () -> Unit,
    scheme: ODSTheme
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // Load image bitmap
    LaunchedEffect(wallpaper.id) {
        imageBitmap = withContext(Dispatchers.IO) {
            try {
                if (wallpaper.isLocal && wallpaper.localPath != null) {
                    BitmapFactory.decodeFile(wallpaper.localPath)
                } else if (wallpaper.imageUrl != null) {
                    // For remote images, we'll use ODSImage which handles URLs
                    // For now, return null and we'll use ODSImage in the UI
                    null
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    ODSBox(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Image with zoom and pan gestures
        if (imageBitmap != null) {
            // Local image - use bitmap
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                            scale = newScale

                            // Only allow panning when zoomed in
                            if (newScale > 1f) {
                                offsetX = (offsetX + pan.x).coerceIn(-2000f, 2000f)
                                offsetY = (offsetY + pan.y).coerceIn(-2000f, 2000f)
                            } else {
                                // Reset offset when zoomed out
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
            ) {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = wallpaper.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        },
                    contentScale = ContentScale.Fit
                )
            }
        } else if (wallpaper.imageUrl != null) {
            // Remote image - use ODSImage with zoom/pan and loading progress
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                            scale = newScale

                            // Only allow panning when zoomed in
                            if (newScale > 1f) {
                                offsetX = (offsetX + pan.x).coerceIn(-2000f, 2000f)
                                offsetY = (offsetY + pan.y).coerceIn(-2000f, 2000f)
                            } else {
                                // Reset offset when zoomed out
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
            ) {
                com.telekom.odsystem.atoms.ODSImage(
                    imageModel = com.telekom.odsystem.atoms.ODSImageModel(
                        url = wallpaper.imageUrl,
                        contentDescription = wallpaper.name,
                        onState = { state -> imageState = state },
                        diskCachePolicy = CachePolicy.DISABLED,
                        memoryCachePolicy = CachePolicy.ENABLED
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        },
                    contentScale = ContentScale.Fit
                )
                
                // Show loading/error overlay based on state
                when (imageState) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSLoadingSpinner(
                                scheme = scheme,
                                props = ODSLoadingSpinnerProps(
                                    labelText = "Loading wallpaper...",
                                    size = ODSLoadingSpinnerSize.SMALL,
                                    variant = ODSLoadingSpinnerVariant.STANDARD,
                                    labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL
                                )
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "Failed to load wallpaper",
                                style = DSTextStyles.bodyMRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }
                    else -> { /* Success or Empty - image is shown */ }
                }
            }
        }

        // Close button
        ODSButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(DSVariables.spacingComponent4),
            scheme = scheme,
            props = ODSButtonProps(
                buttonIcon = ODSIconModel(Icons.Default.Close),
                buttonType = ODSButtonButtonType.ICON_ONLY,
                size = ODSButtonSize.SMALL,
                variant = ODSButtonVariant.SECONDARY
            ),
            onClick = onDismiss
        )

        // Bottom action bar with Set Wallpaper and Download buttons
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(DSVariables.spacingComponent4),
            horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Set as Wallpaper button
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(Icons.Default.Wallpaper),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.LARGE,
                    variant = ODSButtonVariant.PRIMARY
                ),
                onClick = onSetWallpaper
            )

            // Download button
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(Icons.Default.GetApp),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.LARGE,
                    variant = ODSButtonVariant.PRIMARY
                ),
                onClick = onDownload
            )
        }
    }
}
