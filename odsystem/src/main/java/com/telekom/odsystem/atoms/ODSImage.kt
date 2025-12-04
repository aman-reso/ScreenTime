package com.telekom.odsystem.atoms

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.R
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSAspectRatio
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSEffect
import com.telekom.odsystem.foundations.ODSElevation
import com.telekom.odsystem.foundations.applyODSEffect
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import kotlinx.coroutines.Dispatchers
import java.net.URI
import java.net.URL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * ODSImage composable.
 *
 * Displays an image with support for multiple customization options, including size, shape, effects,
 * tinting, and content scaling. Supports images from various sources defined in [ODSImageModel].
 *
 * @param imageModel The image model defining the source and metadata of the image.
 * @param modifier Modifier applied to this component.
 * @param width The desired width of the image.
 * @param height The desired height of the image.
 * @param minWidth Parameter for customization.
 * @param minHeight Parameter for customization.
 * @param maxWidth Parameter for customization.
 * @param maxHeight Parameter for customization.
 * @param border Optional border to apply around the image.
 * @param cornerRadius Optional corner radius for rounding the image corners.
 * @param effect Optional visual effect to apply (e.g., shadow, elevation).
 * @param tint The tint color to apply to the icon.
 *             If [HexColor.None] is provided, no tint will be applied,
 *             and the icon will preserve its original colors. * @param scale Parameter for customization.
 * @param scale Parameter for customization.
 * @param opacity Parameter for customization.
 * @param rotate Parameter for customization.
 * @param aspectRatio Parameter for customization.
 * @param contentScale Defines how the image should be scaled within its bounds.
 * @param filterQuality The sampling algorithm used when scaling the image.
 */
@Suppress("All")
@Composable
fun ODSImage(
    modifier: Modifier? = Modifier,
    imageModel: ODSImageModel?,
    width: Dp? = null,
    height: Dp? = null,
    minWidth: Dp? = null,
    minHeight: Dp? = null,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
    border: ODSBorder? = null,
    cornerRadius: ODSCorners? = null,
    effect: ODSEffect? = null,
    tint: Color? = null,
    scale: Float? = null,
    opacity: Float? = null,
    rotate: Float? = null,
    aspectRatio: ODSAspectRatio? = null,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    imageModel ?: return
    val imageTint = imageModel.tint?.getColor() ?: tint
    val colorFilter: ColorFilter? =
        if (imageTint != null && imageTint != HexColor.None.getColor()) ColorFilter.tint(imageTint) else null
    val contentDescription = imageModel.contentDescription
    val context = LocalContext.current
    var internalModifier = modifier ?: Modifier
    val internalShape = cornerRadius?.getRoundedCornerShape() ?: RoundedCornerShape(0.dp)

    scale?.let {
        internalModifier = internalModifier.then(Modifier.scale(it))
    }

    rotate?.let {
        internalModifier = internalModifier.then(Modifier.rotate(it))
    }

    internalModifier = internalModifier.then(
        Modifier.sizeWithinBounds(
            minWidth = minWidth ?: Dp.Unspecified,
            minHeight = minHeight ?: Dp.Unspecified,
            maxWidth = maxWidth ?: Dp.Unspecified,
            maxHeight = maxHeight ?: Dp.Unspecified
        )
    )

    width?.let {
        internalModifier = internalModifier.then(Modifier.width(it))
    }

    height?.let {
        internalModifier = internalModifier.then(Modifier.height(it))
    }

    aspectRatio?.let {
        internalModifier = internalModifier.then(Modifier.aspectRatio(it.value))
    }

    effect?.let {
        internalModifier = internalModifier.applyODSEffect(
            effect = it.copy(
                elevations = it.elevations.map { elevation ->
                    elevation.copy(
                        color = HexColor(
                            elevation.color.getHexColor(),
                            elevation.color.alpha * (opacity ?: 1f)
                        )
                    )
                }
            ),
            corners = cornerRadius,
            borderWidth = border?.width
        )
    }

    opacity?.let {
        internalModifier = internalModifier.then(Modifier.alpha(it))
    }

    internalModifier = internalModifier.then(
        Modifier.background(imageModel.background, internalShape)
    )

    internalModifier = internalModifier.then(
        Modifier.border(
            width = border?.width ?: 0.dp,
            colorList = border?.colorList,
            shape = internalShape
        )
    )

    internalModifier = internalModifier.then(
        Modifier.clip(internalShape)
    )

    if (imageModel.url != null && !isGifUrl(imageModel.url)) {
        val imageRequestBuilder = ImageRequest.Builder(context)
            .data(imageModel.url)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(imageModel.url)
            .diskCacheKey(imageModel.url)
            .diskCachePolicy(imageModel.diskCachePolicy)
            .memoryCachePolicy(imageModel.memoryCachePolicy)

        imageModel.placeholder?.let {
            imageRequestBuilder.placeholder(it)
                .error(it)
                .fallback(it)
        }

        val imageRequest = imageRequestBuilder.build()

        AsyncImage(
            modifier = internalModifier,
            model = imageRequest,
            contentDescription = contentDescription,
            contentScale = contentScale,
            filterQuality = filterQuality,
            onState = imageModel.onState,
            colorFilter = colorFilter
        )
    } else if (imageModel.imageVector != null) {
        Icon(
            modifier = internalModifier,
            imageVector = imageModel.imageVector!!,
            contentDescription = contentDescription,
            tint = imageTint ?: Color.Unspecified
        )
    } else if (imageModel.drawableRes != null) {
        Image(
            modifier = internalModifier,
            painter = painterResource(id = imageModel.drawableRes!!),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    } else if (imageModel.bitmap != null) {
        Image(
            modifier = internalModifier,
            bitmap = imageModel.bitmap!!.asImageBitmap(),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    } else if (!imageModel.base64.isNullOrEmpty()) {
        val bitmap = imageModel.base64?.let { base64toBitmap(it) } ?: run {
            imageModel.onDecodeState?.invoke(DecodeState.Error(Exception("Failed to decode base64 image")))
            null
        }
        if (bitmap != null) {
            imageModel.onDecodeState?.invoke(DecodeState.Success)
            Image(
                modifier = internalModifier,
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                colorFilter = colorFilter,
            )
        }
    } else if (imageModel.url != null && isGifUrl(imageModel.url)) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .networkObserverEnabled(enable = imageModel.networkObserverEnabled)
            .components {
                if (SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()

        val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
            .data(imageModel.url)
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(imageModel.url)
            .diskCacheKey(imageModel.url)
            .diskCachePolicy(imageModel.diskCachePolicy)
            .memoryCachePolicy(imageModel.memoryCachePolicy)

        imageModel.placeholder?.let {
            imageRequestBuilder.placeholder(it)
                .error(it)
                .fallback(it)
        }

        val imageRequest = imageRequestBuilder.build()

        Image(
            modifier = internalModifier,
            painter = rememberAsyncImagePainter(
                model = imageRequest,
                imageLoader = imageLoader,
                onState = imageModel.onState
            ),
            contentDescription = contentDescription,
            contentScale = contentScale,
            colorFilter = colorFilter,
        )
    }
}

/**
 * Represents a flexible model for defining and loading images in the ODS design system.
 *
 * Supports loading images from various sources such as URLs, Base64 strings, drawable resources,
 * Bitmap instances, and vector graphics. It also includes support for image loading callbacks, placeholders,
 * background customization, tinting, and accessibility.
 *
 * @property url The URL from which to load the image.
 * @property placeholder A drawable resource ID to be used as a placeholder, error, or fallback image when loading from a URL.
 * @property base64 A Base64 encoded string representing the image.
 * @property bitmap A [Bitmap] instance to be displayed directly.
 * @property onDecodeState A callback invoked with [DecodeState] during Base64 image decoding, either [DecodeState.Success] or [DecodeState.Error].
 * @property onState A callback invoked with [AsyncImagePainter.State] when loading an image via Coil (e.g., from a URL),
 *                   useful for tracking image load lifecycle events such as loading, success, or error.
 * @property drawableRes A drawable resource ID for displaying a local image. Annotated with [@DrawableRes].
 * @property imageVector An [ImageVector] to be displayed, typically for vector graphics or icons.
 * @property background A list of [ODSColorModel] to define the background of the image.
 *                      Can be used to create solid or gradient backgrounds.
 * @property tint The tint color to apply to the image.
 *                If [HexColor.None] is provided, the image will retain its original colors.
 * @property contentDescription A textual description of the image, used for accessibility purposes.
 * @property networkObserverEnabled Controls whether Coil's network observer is enabled for URL image loading.Defaults to `true`.
 * @property memoryCachePolicy The memory cache policy for Coil image loading. Defaults to [CachePolicy.ENABLED].
 * @property diskCachePolicy The disk cache policy for Coil image loading. Defaults to [CachePolicy.ENABLED].
 */
class ODSImageModel {
    var url: String? = null
    var placeholder: Int? = null
    var base64: String? = null
    var bitmap: Bitmap? = null

    var onDecodeState: ((DecodeState) -> Unit)? = null
    var onState: ((AsyncImagePainter.State) -> Unit)? = null

    @DrawableRes
    var drawableRes: Int? = null

    var imageVector: ImageVector? = null

    var background: List<ODSColorModel>? = null

    var tint: HexColor? = null

    var contentDescription: String? = null

    var networkObserverEnabled: Boolean = true

    var memoryCachePolicy: CachePolicy = CachePolicy.ENABLED

    var diskCachePolicy: CachePolicy = CachePolicy.ENABLED

    constructor(
        url: String,
        placeholder: Int? = null,
        onState: ((AsyncImagePainter.State) -> Unit)? = null,
        background: List<ODSColorModel>? = null,
        tint: HexColor? = null,
        contentDescription: String? = null,
        networkObserverEnabled: Boolean = true,
        memoryCachePolicy: CachePolicy = CachePolicy.ENABLED,
        diskCachePolicy: CachePolicy = CachePolicy.ENABLED,
    ) {
        this.url = url
        this.placeholder = placeholder
        this.onState = onState
        this.background = background
        this.tint = tint
        this.contentDescription = contentDescription
        this.networkObserverEnabled = networkObserverEnabled
        this.memoryCachePolicy = memoryCachePolicy
        this.diskCachePolicy = diskCachePolicy
    }

    constructor(
        base64: String,
        onDecodeState: ((DecodeState) -> Unit)? = null,
        background: List<ODSColorModel>? = null,
        tint: HexColor? = null,
        contentDescription: String? = null,
    ) {
        this.base64 = base64
        this.onDecodeState = onDecodeState
        this.background = background
        this.tint = tint
        this.contentDescription = contentDescription
    }

    constructor(
        drawableRes: Int,
        background: List<ODSColorModel>? = null,
        tint: HexColor? = null,
        contentDescription: String? = null,
    ) {
        this.drawableRes = drawableRes
        this.background = background
        this.tint = tint
        this.contentDescription = contentDescription
    }

    constructor(
        imageVector: ImageVector,
        background: List<ODSColorModel>? = null,
        tint: HexColor? = null,
        contentDescription: String? = null,
    ) {
        this.imageVector = imageVector
        this.background = background
        this.tint = tint
        this.contentDescription = contentDescription
    }

    constructor(
        bitmap: Bitmap,
        background: List<ODSColorModel>? = null,
        tint: HexColor? = null,
        contentDescription: String? = null,
    ) {
        this.bitmap = bitmap
        this.background = background
        this.tint = tint
        this.contentDescription = contentDescription
    }

    constructor(background: List<ODSColorModel>, contentDescription: String? = null) {
        this.background = background
        this.contentDescription = contentDescription
    }
}

private fun isGifUrl(url: String?): Boolean {
    try {
        url ?: return false
        val uri = URI(URL(url).toString())
        val path = uri.path
        return path.substringAfterLast("/").endsWith(".gif") || path.substringAfterLast("/")
            .endsWith(".webp")
    } catch (
        e: Exception,
    ) {
        println(e)
        return false
    }
}

@OptIn(ExperimentalEncodingApi::class)
private fun base64toBitmap(base64: String): Bitmap? {
    try {
        val base64Image = if (base64.startsWith(PNG_BASE64_HEADER)) {
            base64.split(",")[1]
        } else {
            base64
        }
        val decodedString = Base64.decode(base64Image)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        println(e)
        return null
    }
}

sealed class DecodeState {
    data object Success : DecodeState()
    data class Error(val exception: Exception) : DecodeState()
}

private const val PNG_BASE64_HEADER = "data:image/png;base64,"

@Preview
@Composable
fun ODSImagePreview() {
    ODSColumn(
        modifier = Modifier.fillMaxSize(),
        background = listOf(ODSColorModel(neutralScheme.basicBackground)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ODSImage(
            effect = ODSEffect(
                elevations = listOf(
                    ODSElevation(
                        x = 3,
                        y = 3,
                        spread = 3,

                        )
                )
            ),
            width = 200.dp,
            height = 100.dp,
            cornerRadius = ODSCorners(
                all = DSVariables.radiusLarge
            ),
            aspectRatio = ODSAspectRatio.VALUE_1_2,
            opacity = 0.5f,
            scale = 1f,
            rotate = 0f,
            border = ODSBorder(
                width = 2.dp,
                colorList = listOf(ODSColorModel(neutralScheme.functionalWarningStandard))
            ),
            imageModel = ODSImageModel(drawableRes = R.drawable.available_keychain_type_bold),
        )
    }
}
