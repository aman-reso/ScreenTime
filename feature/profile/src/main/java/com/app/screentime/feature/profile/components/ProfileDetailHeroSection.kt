package com.app.screentime.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Hero image carousel with:
 * - 540dp height
 * - Top photo segmented progress indicators
 * - Top-right 3-dots action button to Block/Report
 * - Bottom-left floating circular Chat button
 * - Tap zones for photo navigation
 */
@Composable
fun ProfileDetailHeroSection(
    photos: List<String>,
    currentPhotoIndex: Int,
    displayName: String,
    isMyProfile: Boolean,
    scheme: ODSTheme,
    onPhotoIndexChange: (Int) -> Unit,
    onChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val distinctPhotos = remember(photos) {
        photos.filter { it.isNotBlank() }.distinct()
    }
    var isImageLoading by remember(currentPhotoIndex, distinctPhotos) { mutableStateOf(true) }
    val currentUrl = distinctPhotos.getOrNull(currentPhotoIndex)

    ODSBox(
        modifier = modifier
            .fillMaxWidth()
            .height(540.dp),
        cornerRadius = ODSCorners(
            topLeft = 0.dp,
            topRight = 0.dp,
            bottomLeft = 24.dp,
            bottomRight = 24.dp
        ),
        clipContent = true,
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
    ) {
        if (!currentUrl.isNullOrBlank()) {
            if (isImageLoading) {
                ODSSkeleton(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    props = ODSSkeletonProps(variant = ODSSkeletonVariant.FULL)
                )
            }

            ODSImage(
                imageModel = ODSImageModel(
                    url = currentUrl,
                    contentDescription = displayName,
                    onState = { state ->
                        isImageLoading = state is AsyncImagePainter.State.Loading
                    }
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (isImageLoading) {
                ODSBox(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ODSLoadingSpinner(
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(size = ODSLoadingSpinnerSize.SMALL)
                    )
                }
            }
        } else {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_user,
                        contentDescription = displayName
                    ),
                    tint = scheme.basicTextRecessive.getColor(),
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        // Top photo segmented indicator bars
        if (distinctPhotos.size > 1) {
            ODSRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = ODSVariables.spacingComponent4)
                    .padding(horizontal = ODSVariables.spacingComponent4),
                gap = ODSVariables.spacingComponent2,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                distinctPhotos.forEachIndexed { idx, _ ->
                    val isSelected = idx == currentPhotoIndex
                    ODSBox(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp),
                        background = listOf(
                            ODSColorModel(
                                hexColor = if (isSelected) scheme.basicAccent else HexColor("#FFFFFF", 0.5f)
                            )
                        ),
                        cornerRadius = ODSCorners(all = 2.dp)
                    )
                }
            }
        }

        // Tap zones for switching photos
        ODSBox(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterStart)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        if (currentPhotoIndex > 0) {
                            onPhotoIndexChange(currentPhotoIndex - 1)
                        }
                    }
                )
        ) {}

        ODSBox(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterEnd)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        if (currentPhotoIndex < distinctPhotos.size - 1) {
                            onPhotoIndexChange(currentPhotoIndex + 1)
                        }
                    }
                )
        ) {}

        // Bottom-right circular chat button on image
        if (!isMyProfile) {
            ODSBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = ODSVariables.spacingLayout1,
                        bottom = ODSVariables.spacingLayout1
                    )
                    .size(56.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onChatClick
                    ),
                cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                background = listOf(ODSColorModel(hexColor = scheme.basicText)),
                contentAlignment = Alignment.Center
            ) {
                ODSIcon(
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.ic_message_circle,
                        contentDescription = "Chat"
                    ),
                    tint = scheme.basicBackground.getColor(),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
