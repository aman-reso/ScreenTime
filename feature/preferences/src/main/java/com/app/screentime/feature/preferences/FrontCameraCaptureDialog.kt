package com.app.screentime.feature.preferences

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.app.screentime.core.ui.util.showODSToast
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors

private const val TAG = "FrontCameraCapture"

/**
 * Dedicated 100% Front-Only Camera Dialog powered by CameraX.
 * Physically locks to the front camera (no toggle to back camera),
 * displays an oval face alignment guide, and captures a live selfie directly into storage.
 */
@Composable
fun FrontCameraCaptureDialog(
    onDismiss: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    scheme: ODSTheme,
    title: String = "Front Camera Photo",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Dialog(
        onDismissRequest = {
            if (!isCapturing) onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isCapturing,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // 1. CameraX Preview View locked to FRONT camera
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()

                            val preview = Preview.Builder().build().also {
                                it.surfaceProvider = previewView.surfaceProvider
                            }

                            val capture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                .setTargetRotation(previewView.display?.rotation ?: android.view.Surface.ROTATION_0)
                                .build()

                            // HARD-LOCKED TO FRONT FACING ONLY
                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                                .build()

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                capture
                            )

                            imageCapture = capture
                        } catch (exc: Exception) {
                            Log.e(TAG, "Failed to bind front camera", exc)
                            ctx.showODSToast("Could not access front camera")
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            // 2. Translucent Oval Face Guide Overlay
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val ovalWidth = canvasWidth * 0.72f
                val ovalHeight = canvasHeight * 0.46f
                val ovalLeft = (canvasWidth - ovalWidth) / 2f
                val ovalTop = (canvasHeight - ovalHeight) / 2f - 40.dp.toPx()

                val ovalPath = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(ovalLeft, ovalTop),
                                size = Size(ovalWidth, ovalHeight)
                            ),
                            cornerRadius = CornerRadius(ovalWidth / 2f, ovalHeight / 2f)
                        )
                    )
                }

                // Darken everything outside the oval guide
                clipPath(ovalPath, clipOp = ClipOp.Difference) {
                    drawRect(color = Color(0x99000000))
                }
            }

            // 3. Top Bar (Close Button + Step Title)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x55000000))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(
                            drawableRes = R.drawable.ic_close,
                            contentDescription = "Close"
                        ),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                ODSText(
                    text = title,
                    style = ODSTextStyles.bodyMBold,
                    color = scheme.basicBackgroundCard
                )

                Spacer(modifier = Modifier.size(40.dp))
            }

            // 4. Instructions & Bottom Shutter Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ODSText(
                    text = "Center your face in the frame",
                    style = ODSTextStyles.bodySBold,
                    color = scheme.basicBackgroundCard
                )

                Spacer(modifier = Modifier.height(6.dp))

                ODSText(
                    text = "Front camera only • Good lighting required",
                    style = ODSTextStyles.microcopyRegular,
                    color = scheme.basicBackgroundCard
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Shutter Button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(if (isCapturing) Color.Gray else Color.White)
                        .clickable(
                            enabled = !isCapturing && imageCapture != null,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val capture = imageCapture ?: return@clickable
                            isCapturing = true

                            val photoFile = File(
                                context.cacheDir,
                                "selfie_front_${System.currentTimeMillis()}.jpg"
                            )

                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            capture.takePicture(
                                outputOptions,
                                cameraExecutor,
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        try {
                                            // Ensure correct rotation and mirror for front camera
                                            normalizeCapturedImage(photoFile)
                                            val savedUri = Uri.fromFile(photoFile).toString()

                                            ContextCompat.getMainExecutor(context).execute {
                                                isCapturing = false
                                                onPhotoCaptured(savedUri)
                                                onDismiss()
                                            }
                                        } catch (t: Throwable) {
                                            Log.e(TAG, "Image normalization error", t)
                                            ContextCompat.getMainExecutor(context).execute {
                                                isCapturing = false
                                                onPhotoCaptured(Uri.fromFile(photoFile).toString())
                                                onDismiss()
                                            }
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        Log.e(TAG, "Selfie capture failed: ${exception.message}", exception)
                                        ContextCompat.getMainExecutor(context).execute {
                                            isCapturing = false
                                            context.showODSToast("Capture failed: ${exception.message}")
                                        }
                                    }
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isCapturing) {
                        ODSLoadingSpinner(
                            scheme = scheme,
                            props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.X_SMALL)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Reads EXIF orientation from the captured photo file, applies rotation so the bitmap
 * is correctly upright, and rewrites the JPEG.
 */
private fun normalizeCapturedImage(file: File) {
    val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return
    val exif = ExifInterface(file.absolutePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
        else -> return // Already upright
    }

    val rotated = Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )

    FileOutputStream(file).use { out ->
        rotated.compress(Bitmap.CompressFormat.JPEG, 92, out)
        out.flush()
    }
    bitmap.recycle()
    rotated.recycle()
}
