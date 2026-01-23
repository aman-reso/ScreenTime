package com.app.screentime.filemanager.component

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.Text as MaterialText
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.FileProvider
import com.app.screentime.filemanager.model.FileItem
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.molecules.dialog.ODSDialog
import com.telekom.odsystem.molecules.dialog.ODSDialogProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Full-screen image viewer with share, crop, and compress options
 */
@Composable
fun FullScreenImageViewer(
    fileItem: FileItem,
    onDismiss: () -> Unit,
    onShare: (Uri) -> Unit,
    onShareError: (String) -> Unit,
    onCrop: (File) -> Unit,
    onCropError: (String) -> Unit,
    onCompress: (File) -> Unit,
    onCompressError: (String) -> Unit,
    onResize: (File, Int, Int) -> Unit,
    onResizeError: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var showResizeDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    // Get original image dimensions
    val originalWidth = remember(imageBitmap) { imageBitmap?.width ?: 0 }
    val originalHeight = remember(imageBitmap) { imageBitmap?.height ?: 0 }

    // Load image bitmap
    LaunchedEffect(fileItem.file.absolutePath) {
        imageBitmap = withContext(Dispatchers.IO) {
            try {
                BitmapFactory.decodeFile(fileItem.file.absolutePath)
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Image with zoom and pan gestures
        imageBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = (scale * zoom).coerceIn(1f, 5f)
                            scale = newScale
                            
                            // Only allow panning when zoomed in
                            if (newScale > 1f) {
                                offsetX = (offsetX + pan.x).coerceIn(-1000f, 1000f)
                                offsetY = (offsetY + pan.y).coerceIn(-1000f, 1000f)
                            } else {
                                // Reset offset when zoomed out
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = fileItem.name,
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

        // Header with image name and close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image name
            Text(
                text = fileItem.name,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                maxLines = 1
            )
            
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Bottom action bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Share button
            ActionButton(
                icon = Icons.Outlined.Share,
                label = "Share",
                onClick = {
                    coroutineScope.launch {
                        try {
                            val uri = getFileUri(context, fileItem.file)
                            if (uri != null) {
                                onShare(uri)
                            } else {
                                onShareError("Failed to get file URI")
                            }
                        } catch (e: Exception) {
                            onShareError("Failed to share: ${e.message}")
                        }
                    }
                }
            )

            // Crop button
            ActionButton(
                icon = Icons.Outlined.ContentCut,
                label = "Crop",
                onClick = {
                    try {
                        onCrop(fileItem.file)
                    } catch (e: Exception) {
                        onCropError("Failed to crop: ${e.message}")
                    }
                }
            )

            // Resize button
            ActionButton(
                icon = Icons.Outlined.AspectRatio,
                label = "Resize",
                onClick = {
                    showResizeDialog = true
                }
            )

            // Compress button
            ActionButton(
                icon = Icons.Outlined.Archive,
                label = "Compress",
                onClick = {
                    coroutineScope.launch {
                        try {
                            val compressedFile = compressImage(context, fileItem.file)
                            if (compressedFile != null) {
                                onCompress(compressedFile)
                            } else {
                                onCompressError("Failed to compress image")
                            }
                        } catch (e: Exception) {
                            onCompressError("Failed to compress: ${e.message}")
                        }
                    }
                }
            )
        }
        
        // Resize dialog
        if (showResizeDialog) {
            ResizeImageDialog(
                originalWidth = originalWidth,
                originalHeight = originalHeight,
                onDismiss = { showResizeDialog = false },
                onResize = { width, height ->
                    showResizeDialog = false
                    coroutineScope.launch {
                        try {
                            val resizedFile = resizeImage(context, fileItem.file, width, height)
                            if (resizedFile != null) {
                                onResize(resizedFile, width, height)
                            } else {
                                onResizeError("Failed to resize image")
                            }
                        } catch (e: Exception) {
                            onResizeError("Failed to resize: ${e.message}")
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

/**
 * Get file URI using FileProvider
 * If FileProvider fails, copy file to cache and return cache URI
 */
private suspend fun getFileUri(context: Context, file: File): Uri? = withContext(Dispatchers.IO) {
    try {
        if (!file.exists()) {
            Log.e("FullScreenImageViewer", "File does not exist: ${file.absolutePath}")
            return@withContext null
        }
        
        // Try to get URI directly first
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            Log.d("FullScreenImageViewer", "Got URI directly: $uri")
            return@withContext uri
        } catch (e: Exception) {
            Log.w("FullScreenImageViewer", "Direct FileProvider failed, copying to cache: ${e.message}")
            
            // If direct access fails, copy to cache directory
            val cacheDir = context.cacheDir
            val shareDir = File(cacheDir, "share_images")
            if (!shareDir.exists()) {
                shareDir.mkdirs()
            }
            
            val cachedFile = File(shareDir, file.name)
            file.copyTo(cachedFile, overwrite = true)
            
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                cachedFile
            )
            Log.d("FullScreenImageViewer", "Got URI from cache: $uri")
            return@withContext uri
        }
    } catch (e: Exception) {
        Log.e("FullScreenImageViewer", "Error getting file URI: ${e.message}", e)
        null
    }
}

/**
 * Compress image file - saves in same directory with numbered suffix
 */
private suspend fun compressImage(context: Context, originalFile: File): File? = withContext(Dispatchers.IO) {
    try {
        if (!originalFile.exists()) {
            Log.e("FullScreenImageViewer", "Original file does not exist: ${originalFile.absolutePath}")
            return@withContext null
        }
        
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath) ?: run {
            Log.e("FullScreenImageViewer", "Failed to decode bitmap from: ${originalFile.absolutePath}")
            return@withContext null
        }
        
        // Determine output format based on original file extension
        val extension = originalFile.extension.lowercase()
        val compressFormat = when (extension) {
            "png" -> Bitmap.CompressFormat.PNG
            "webp" -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.JPEG
        }
        
        // Keep same extension as original
        val outputExtension = extension
        
        // Find available filename with numbered suffix (1), (2), etc.
        val parentDir = originalFile.parentFile ?: return@withContext null
        val baseName = originalFile.nameWithoutExtension
        var counter = 1
        var compressedFile: File
        
        do {
            val suffix = if (counter == 1) "" else " ($counter)"
            val compressedFileName = "$baseName$suffix.$outputExtension"
            compressedFile = File(parentDir, compressedFileName)
            counter++
        } while (compressedFile.exists() && counter < 1000) // Safety limit
        
        // Compress to 70% quality (PNG doesn't support quality, so use 100)
        val quality = if (compressFormat == Bitmap.CompressFormat.PNG) 100 else 70
        val outputStream = FileOutputStream(compressedFile)
        val success = bitmap.compress(compressFormat, quality, outputStream)
        outputStream.close()
        
        if (!success) {
            Log.e("FullScreenImageViewer", "Failed to compress bitmap")
            compressedFile.delete()
            return@withContext null
        }
        
        if (!compressedFile.exists()) {
            Log.e("FullScreenImageViewer", "Compressed file was not created")
            return@withContext null
        }
        
        Log.d("FullScreenImageViewer", "Image compressed successfully: ${compressedFile.absolutePath}")
        compressedFile
    } catch (e: Exception) {
        Log.e("FullScreenImageViewer", "Error compressing image: ${e.message}", e)
        null
    }
}

/**
 * Resize image dialog for specifying pixel dimensions
 */
@Composable
private fun ResizeImageDialog(
    originalWidth: Int,
    originalHeight: Int,
    onDismiss: () -> Unit,
    onResize: (Int, Int) -> Unit,
    scheme: ODSTheme = neutralScheme
) {
    var widthText by remember { mutableStateOf("") }
    var heightText by remember { mutableStateOf("") }
    var widthError by remember { mutableStateOf<String?>(null) }
    var heightError by remember { mutableStateOf<String?>(null) }

    ODSDialog(
        modifier = Modifier.fillMaxWidth(0.9f),
        scheme = scheme,
        onDismissRequest = onDismiss,
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        props = ODSDialogProps(
            showCloseButton = true,
            title = "Resize Image",
            bodyText = "Original size: ${originalWidth} x ${originalHeight} pixels"
        ),
        contentSlot = {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4,
                padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
            ) {
                // Preset sizes for common government exam requirements
                ODSText(
                    text = "Common Sizes:",
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                
                ODSColumn(
                    gap = DSVariables.spacingComponent2
                ) {
                    PresetSizeButton("Passport Photo (413 x 531)", 413, 531, scheme) {
                        widthText = "413"
                        heightText = "531"
                    }
                    PresetSizeButton("Visa Photo (600 x 600)", 600, 600, scheme) {
                        widthText = "600"
                        heightText = "600"
                    }
                    PresetSizeButton("Aadhaar Photo (200 x 200)", 200, 200, scheme) {
                        widthText = "200"
                        heightText = "200"
                    }
                    PresetSizeButton("PAN Card (200 x 250)", 200, 250, scheme) {
                        widthText = "200"
                        heightText = "250"
                    }
                }
                
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent3))
                
                // Custom size inputs
                ODSText(
                    text = "Custom Size:",
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
                
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3)
                ) {
                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Width (px)",
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        TextField(
                            value = widthText,
                            onValueChange = {
                                widthText = it.filter { char -> char.isDigit() }
                                widthError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { androidx.compose.material3.Text("Width") },
                            isError = widthError != null
                        )
                        widthError?.let {
                            ODSText(
                                text = it,
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }
                    
                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = DSVariables.spacingComponent2
                    ) {
                        ODSText(
                            text = "Height (px)",
                            style = DSTextStyles.bodySRegular,
                            color = scheme.basicText
                        )
                        TextField(
                            value = heightText,
                            onValueChange = {
                                heightText = it.filter { char -> char.isDigit() }
                                heightError = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { androidx.compose.material3.Text("Height") },
                            isError = heightError != null
                        )
                        heightError?.let {
                            ODSText(
                                text = it,
                                style = DSTextStyles.microcopyRegular,
                                color = scheme.functionalDestructiveStandard
                            )
                        }
                    }
                }
            }
        },
        actionSlot = {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent3),
                padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
            ) {
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Cancel",
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                )
                
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = "Resize",
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = {
                        val width = widthText.toIntOrNull()
                        val height = heightText.toIntOrNull()
                        
                        when {
                            width == null || width <= 0 -> {
                                widthError = "Enter valid width"
                            }
                            height == null || height <= 0 -> {
                                heightError = "Enter valid height"
                            }
                            else -> {
                                onResize(width, height)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    )
}

@Composable
private fun PresetSizeButton(
    label: String,
    width: Int,
    height: Int,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSBox(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSText(
            text = label,
            style = DSTextStyles.bodySRegular,
            color = scheme.basicText
        )
    }
}

/**
 * Resize image to specified dimensions
 */
private suspend fun resizeImage(
    context: Context,
    originalFile: File,
    targetWidth: Int,
    targetHeight: Int
): File? = withContext(Dispatchers.IO) {
    try {
        if (!originalFile.exists()) {
            Log.e("FullScreenImageViewer", "Original file does not exist: ${originalFile.absolutePath}")
            return@withContext null
        }
        
        val originalBitmap = BitmapFactory.decodeFile(originalFile.absolutePath) ?: run {
            Log.e("FullScreenImageViewer", "Failed to decode bitmap from: ${originalFile.absolutePath}")
            return@withContext null
        }
        
        // Resize bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            targetWidth,
            targetHeight,
            true
        )
        
        // Determine output format based on original file extension
        val extension = originalFile.extension.lowercase()
        val compressFormat = when (extension) {
            "png" -> Bitmap.CompressFormat.PNG
            "webp" -> Bitmap.CompressFormat.WEBP
            else -> Bitmap.CompressFormat.JPEG
        }
        
        // Find available filename with numbered suffix
        val parentDir = originalFile.parentFile ?: return@withContext null
        val baseName = originalFile.nameWithoutExtension
        var counter = 1
        var resizedFile: File
        
        do {
            val suffix = if (counter == 1) "" else " ($counter)"
            val resizedFileName = "${baseName}_${targetWidth}x${targetHeight}$suffix.$extension"
            resizedFile = File(parentDir, resizedFileName)
            counter++
        } while (resizedFile.exists() && counter < 1000)
        
        // Save resized image
        val quality = if (compressFormat == Bitmap.CompressFormat.PNG) 100 else 90
        val outputStream = FileOutputStream(resizedFile)
        val success = resizedBitmap.compress(compressFormat, quality, outputStream)
        outputStream.close()
        
        // Recycle bitmaps
        originalBitmap.recycle()
        resizedBitmap.recycle()
        
        if (!success) {
            Log.e("FullScreenImageViewer", "Failed to save resized bitmap")
            resizedFile.delete()
            return@withContext null
        }
        
        if (!resizedFile.exists()) {
            Log.e("FullScreenImageViewer", "Resized file was not created")
            return@withContext null
        }
        
        Log.d("FullScreenImageViewer", "Image resized successfully: ${resizedFile.absolutePath}")
        resizedFile
    } catch (e: Exception) {
        Log.e("FullScreenImageViewer", "Error resizing image: ${e.message}", e)
        null
    }
}

