package com.app.screentime.wallpaper.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import com.app.screentime.wallpaper.model.Wallpaper
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.extensions.onClick
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    scheme: ODSTheme,
    onClick: () -> Unit,
    isCurrentHome: Boolean = false,
    isCurrentLock: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    ODSCardBasic(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        scheme = scheme,
        props = ODSCardBasicProps(),
        contentPadding = ODSPadding(0.dp),
        contentSlot = {
            Box(modifier = Modifier.fillMaxSize()) {
                if (wallpaper.isLocal) {
                    // For local wallpapers, use ODSImage with bitmap
                    val bitmap = wallpaper.localPath?.let { BitmapFactory.decodeFile(it) }
                    if (bitmap != null) {
                        ODSImage(
                            imageModel = ODSImageModel(bitmap),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            cornerRadius = ODSCorners(all = DSVariables.spacingComponent2)
                        )
                    }
                } else {
                    // For remote wallpapers, use ODSImage with loading state tracking
                    wallpaper.imageUrl?.let { imageUrl ->
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = imageUrl,
                                contentDescription = wallpaper.name,
                                onState = { state -> imageState = state },
                                diskCachePolicy = CachePolicy.DISABLED,
                                memoryCachePolicy = CachePolicy.ENABLED
                            ),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            cornerRadius = ODSCorners(all = DSVariables.spacingComponent2)
                        )
                        
                        // Show loading/error overlay based on state
                        when (imageState) {
                            is AsyncImagePainter.State.Loading -> {
                                ODSBox(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSLoadingSpinner(
                                        scheme = scheme,
                                        props = ODSLoadingSpinnerProps(
                                            labelText = "Loading...",
                                            size = ODSLoadingSpinnerSize.SMALL,
                                            variant = ODSLoadingSpinnerVariant.STANDARD,
                                            labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL
                                        )
                                    )
                                }
                            }
                            is AsyncImagePainter.State.Error -> {
                                ODSBox(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ODSText(
                                        text = "Failed to load",
                                        style = DSTextStyles.bodySRegular,
                                        color = scheme.functionalDestructiveStandard
                                    )
                                }
                            }
                            else -> { /* Success or Empty - image is shown */ }
                        }
                    }
                }
            }
        }
    )
}

