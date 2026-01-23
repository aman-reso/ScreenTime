package com.app.screentime.wallpaper.screen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast.makeText
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GetApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.app.screentime.ads.AdConfig
import com.app.screentime.ads.InterstitialAdManager
import com.app.screentime.config.R
import com.app.screentime.wallpaper.api.model.ImageItem
import com.app.screentime.wallpaper.model.Wallpaper
import com.app.screentime.wallpaper.model.WallpaperType
import com.app.screentime.wallpaper.viewmodel.WallpaperViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.atoms.progressbar.ODSProgressBar
import com.telekom.odsystem.atoms.progressbar.ODSProgressBarMode
import com.telekom.odsystem.atoms.progressbar.ODSProgressBarProps
import com.telekom.odsystem.atoms.progressbar.ODSProgressBarSize
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Full-screen wallpaper viewer screen
 */
@Composable
fun FullScreenWallpaperScreen(
    modifier: Modifier = Modifier,
    imageItem: ImageItem? = null,
    onBackClick: () -> Unit,
    viewModel: WallpaperViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    if (imageItem == null) {
        LaunchedEffect(Unit) {
            onBackClick()
        }
        return
    }
    val context = LocalContext.current
    val activity = LocalActivity.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasShownInterstitial by remember { mutableStateOf(false) }

    // Preload interstitial when full screen opens
    LaunchedEffect(Unit) {
        activity?.let { InterstitialAdManager.preloadAd(it) }
    }

    // Show success toast when wallpaper is set
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            makeText(
                context,
                message,
                android.widget.Toast.LENGTH_LONG
            ).show()
            viewModel.clearSuccessMessage()
        }
    }

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isLoadingShare by remember { mutableStateOf(false) }
    var isImageLoading by remember { mutableStateOf(true) }
    var imageLoadProgress by remember { mutableFloatStateOf(0f) }
    val downloadProgress = uiState.downloadProgress
    val downloadedBytes = uiState.downloadedBytes
    val totalBytes = uiState.totalBytes

    // Show full-screen interstitial while image is downloading/loading
    LaunchedEffect(isImageLoading) {
        if (isImageLoading && !hasShownInterstitial && AdConfig.areAdsEnabled()) {
            activity?.let { act ->
                InterstitialAdManager.showInterstitialAd(act) {}
                hasShownInterstitial = true
            }
        }
    }

    val displayImageUrl = imageItem.variations?.original?.url
        ?: imageItem.variations?.adapted?.url
        ?: imageItem.variations?.preview_small?.url

    ODSColumn(
        modifier = modifier.fillMaxSize(),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        // 1. Top section: status bar + close, share, download
        ODSRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = DSVariables.spacingComponent4,
                    end = DSVariables.spacingComponent4,
                    bottom = DSVariables.spacingComponent3
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(Icons.Outlined.Close),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST
                ),
                onClick = onBackClick
            )
            ODSRow(
                gap = DSVariables.spacingComponent3
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(Icons.Outlined.Share),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.GHOST,
                        disabled = isLoadingShare || (downloadProgress != null)
                    ),
                    onClick = {
                        coroutineScope.launch {
                            isLoadingShare = true
                            try {
                                val wallpaper = imageItem.toWallpaper()
                                shareWallpaper(context, wallpaper, imageItem, null)
                            } catch (e: Exception) {
                                makeText(
                                    context,
                                    "Failed to share: ${e.message}",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            } finally {
                                isLoadingShare = false
                            }
                        }
                    }
                )
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(Icons.Outlined.GetApp),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.GHOST,
                        disabled = isLoadingShare || (downloadProgress != null)
                    ),
                    onClick = {
                        val wallpaper = imageItem.toWallpaper()
                        viewModel.downloadWallpaper(wallpaper)
                    }
                )
            }
        }

        // Download progress bar with ODS component
        if (downloadProgress != null) {
            val downloadedMB = (downloadedBytes / 1024f / 1024f)
            val totalMB = (totalBytes / 1024f / 1024f)
            val percentage = (downloadProgress * 100).toInt()
            
            val counterText = if (totalBytes > 0) {
                "%.1f MB / %.1f MB (%d%%)".format(downloadedMB, totalMB, percentage)
            } else {
                "%d%%".format(percentage)
            }
            
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DSVariables.spacingComponent4, vertical = DSVariables.spacingComponent2)
            ) {
                ODSProgressBar(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSProgressBarProps(
                        label = stringResource(R.string.downloading),
                        counterText = counterText,
                        mainDataProgress = downloadProgress.coerceIn(0f, 1f),
                        size = ODSProgressBarSize.SMALL,
                        mode = ODSProgressBarMode.STANDARD
                    )
                )
            }
        }

        ODSBox(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        val newScale = (scale * zoom).coerceIn(1f, 5f)
                        scale = newScale

                        if (newScale > 1f) {
                            offsetX = (offsetX + pan.x).coerceIn(-2000f, 2000f)
                            offsetY = (offsetY + pan.y).coerceIn(-2000f, 2000f)
                        } else {
                            offsetX = 0f
                            offsetY = 0f
                        }
                    }
                }
        ) {
            if (isImageLoading) {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    background = listOf(ODSColorModel(scheme.basicBackground))
                ) {
                    ODSColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        gap = DSVariables.spacingComponent3
                    ) {
                        ODSLoadingSpinner(
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(
                                size = ODSLoadingSpinnerSize.SMALL,
                                variant = ODSLoadingSpinnerVariant.STANDARD,
                                labelAlignment = ODSLoadingSpinnerLabelAlignment.NONE
                            )
                        )
                        val loadPercentage = (imageLoadProgress * 100).toInt()
                        ODSText(
                            text = stringResource(R.string.loading_percentage, loadPercentage),
                            style = DSTextStyles.bodyMRegular,
                            color = scheme.basicText
                        )
                    }
                }
            }

            if (displayImageUrl != null) {
                val imageLoader = remember {
                    ImageLoader.Builder(context)
                        .crossfade(true)
                        .build()
                }
                
                val imageRequest = remember(displayImageUrl) {
                    ImageRequest.Builder(context)
                        .data(displayImageUrl)
                        .listener(
                            onStart = {
                                isImageLoading = true
                                imageLoadProgress = 0f
                            },
                            onSuccess = { _, _ ->
                                isImageLoading = false
                                imageLoadProgress = 1f
                            },
                            onError = { _, _ ->
                                isImageLoading = false
                            }
                        )
                        .target { drawable ->

                        }
                        .build()
                }
                
                // Track download progress
                LaunchedEffect(displayImageUrl) {
                    launch(Dispatchers.IO) {
                        try {
                            val connection = URL(displayImageUrl).openConnection() as HttpURLConnection
                            connection.connect()
                            val totalLength = connection.contentLengthLong
                            
                            if (totalLength > 0) {
                                val input = connection.inputStream
                                var downloaded = 0L
                                val buffer = ByteArray(8192)
                                var bytesRead: Int
                                
                                while (input.read(buffer).also { bytesRead = it } != -1) {
                                    downloaded += bytesRead
                                    withContext(Dispatchers.Main) {
                                        imageLoadProgress = (downloaded.toFloat() / totalLength).coerceIn(0f, 1f)
                                    }
                                }
                                input.close()
                            }
                            connection.disconnect()
                        } catch (e: Exception) {

                        }
                    }
                }

                ODSImage(
                    imageModel = ODSImageModel(
                        url = displayImageUrl,
                        onState = { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading -> {
                                    isImageLoading = true
                                }
                                is AsyncImagePainter.State.Success -> {
                                    isImageLoading = false
                                    imageLoadProgress = 1f
                                }
                                is AsyncImagePainter.State.Error -> {
                                    isImageLoading = false
                                }
                                else -> {}
                            }
                        }
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
            }
        }

        // 3. Bottom section: set wallpaper options
        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(scheme.basicBackgroundCard))
        ) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = DSVariables.spacingComponent4,
                        vertical = DSVariables.spacingComponent3
                    ),
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2)
            ) {
                ODSColumn(
                    modifier = Modifier
                        .weight(0.33f)
                        .clickable {
                            coroutineScope.launch {
                                val wallpaper = imageItem.toWallpaper()
                                viewModel.setWallpaper(wallpaper, WallpaperType.HOME)
                            }
                        }
                        .padding(DSVariables.spacingComponent2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(Icons.Outlined.Home, tint = scheme.basicText),
                        width = 24.dp,
                        height = 24.dp
                    )
                    ODSText(
                        text = stringResource(R.string.home_screen),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                }

                ODSColumn(
                    modifier = Modifier
                        .weight(0.33f)
                        .clickable {
                            coroutineScope.launch {
                                val wallpaper = imageItem.toWallpaper()
                                viewModel.setWallpaper(wallpaper, WallpaperType.LOCK)
                            }
                        }
                        .padding(DSVariables.spacingComponent2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(Icons.Outlined.Lock, tint = scheme.basicText),
                        width = 24.dp,
                        height = 24.dp
                    )
                    ODSText(
                        text = stringResource(R.string.lock_screen),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                }

                ODSColumn(
                    modifier = Modifier
                        .weight(0.33f)
                        .clickable {
                            coroutineScope.launch {
                                val wallpaper = imageItem.toWallpaper()
                                viewModel.setWallpaper(wallpaper, WallpaperType.BOTH)
                            }
                        }
                        .padding(DSVariables.spacingComponent2),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = DSVariables.spacingComponent2
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(Icons.Outlined.Apps, tint = scheme.basicText),
                        width = 24.dp,
                        height = 24.dp
                    )
                    ODSText(
                        text = stringResource(R.string.both),
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                }
            }
        }
    }
}

/**
 * Share wallpaper image
 */
private suspend fun shareWallpaper(
    context: Context,
    wallpaper: Wallpaper,
    imageItem: ImageItem,
    bitmap: Bitmap?
) = withContext(Dispatchers.IO) {
    try {
        // Prefer original URL from ImageItem, then adapted, then wallpaper imageUrl
        val imageUrl = imageItem.variations?.original?.url
            ?: imageItem.variations?.adapted?.url
            ?: wallpaper.imageUrl

        val shareBitmap = bitmap ?: when {
            wallpaper.localPath != null -> {
                BitmapFactory.decodeFile(wallpaper.localPath)
            }

            imageUrl != null -> {
                // Download image using Coil
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()
                val result = loader.execute(request)
                if (result is SuccessResult) {
                    (result.drawable as? BitmapDrawable)?.bitmap
                } else {
                    null
                }
            }

            else -> null
        }

        if (shareBitmap == null) {
            withContext(Dispatchers.Main) {
                makeText(
                    context,
                    "Failed to load image for sharing",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
            return@withContext
        }

        // Save to cache for sharing
        val cacheDir = File(context.cacheDir, "share_images")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val imageFile = File(cacheDir, "wallpaper_${System.currentTimeMillis()}.jpg")
        FileOutputStream(imageFile).use { out ->
            shareBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        withContext(Dispatchers.Main) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val chooser = Intent.createChooser(shareIntent, "Share Wallpaper")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        }
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            makeText(
                context,
                "Failed to share: ${e.message}",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}
