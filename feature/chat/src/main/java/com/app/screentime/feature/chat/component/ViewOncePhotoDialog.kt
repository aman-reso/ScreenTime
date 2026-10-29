package com.app.screentime.feature.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.ODSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Ephemeral View-Once Photo Viewer Modal.
 * Displays the photo in full screen. When dismissed (by clicking Close, back, or outside),
 * the photo is permanently consumed and removed from the chat.
 */
@Composable
fun ViewOncePhotoDialog(
    photoUrl: String,
    onDismiss: () -> Unit,
    scheme: ODSTheme,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.96f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header: Close Button + Title & View Once Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFFFFF))
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
                                contentDescription = "Close and remove photo"
                            ),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    ODSRow(
                        verticalAlignment = Alignment.CenterVertically,
                        gap = 6.dp
                    ) {
                        ODSBox(
                            modifier = Modifier.size(20.dp),
                            cornerRadius = ODSCorners(all = 10.dp),
                            background = listOf(ODSColorModel(hexColor = scheme.basicAccent)),
                            contentAlignment = Alignment.Center
                        ) {
                            ODSText(
                                text = "1",
                                style = ODSTextStyles.microcopyBold,
                                color = scheme.basicTextOnAccent
                            )
                        }

                        ODSText(
                            text = "View Once Photo",
                            style = ODSTextStyles.bodyMBold,
                            color = HexColor("#FFFFFF", 1.0f)
                        )
                    }

                    Spacer(modifier = Modifier.size(40.dp))
                }

                // Centered High-Resolution Photo
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ODSBox(
                        modifier = Modifier
                            .fillMaxSize(),
                        cornerRadius = ODSCorners(all = ODSVariables.radiusLarge),
                        clipContent = true,
                        contentAlignment = Alignment.Center
                    ) {
                        ODSImage(
                            imageModel = ODSImageModel(
                                url = photoUrl,
                                contentDescription = "View Once Photo"
                            ),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Bottom Ephemeral Warning
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ODSBox(
                        cornerRadius = ODSCorners(all = ODSVariables.radiusFull),
                        background = listOf(ODSColorModel(hexColor = HexColor("#000000", 0.6f))),
                        border = ODSBorder(
                            width = ODSVariables.strokes1,
                            colorList = listOf(ODSColorModel(hexColor = HexColor("#FFFFFF", 0.2f)))
                        ),
                        padding = com.telekom.odsystem.foundations.ODSPadding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    ) {
                        ODSText(
                            text = "🔒 This photo will disappear from chat once closed",
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor("#FFFFFF", 0.9f)
                        )
                    }
                }
            }
        }
    }
}
