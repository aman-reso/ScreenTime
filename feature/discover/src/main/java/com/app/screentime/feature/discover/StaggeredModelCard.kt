package com.app.screentime.feature.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ModelProfile
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
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

private val dummyModelImages = listOf(
    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=800&q=80",
    "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=800&q=80"
)

@Composable
fun StaggeredModelCard(
    model: ModelProfile,
    modifier: Modifier = Modifier,
    height: Dp? = null,
    scheme: ODSTheme,
    onVoiceCallClick: (() -> Unit)? = null,
    onChatClick: (() -> Unit)? = null,
    onClick: () -> Unit = {}
) {
    val dummyUrl = remember(model.id, model.avatarUrl) {
        if (model.avatarUrl.isNotBlank() && model.avatarUrl.startsWith("http")) model.avatarUrl
        else {
            val hash = (model.id.hashCode() and 0x7FFFFFFF)
            dummyModelImages[hash % dummyModelImages.size]
        }
    }

    ODSBox(
        modifier = modifier
            .then(if (height != null) Modifier.height(height) else Modifier.fillMaxHeight())
            .clickable(onClick = onClick),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = 12.dp),
        border = ODSBorder(
            width = 1.dp,
            colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
        )
    ) {
        // Model Photo
        ODSImage(
            imageModel = ODSImageModel(
                url = dummyUrl,
                contentDescription = model.name
            ),
            cornerRadius = ODSCorners(all = 12.dp),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Scrim overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x22000000),
                            Color.Transparent,
                            Color(0xE6000000)
                        )
                    )
                )
        )

        // Top Online Live Dot / Rate Tag
        ODSRow(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            gap = 6.dp,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (model.isOnline) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                )
            }
            ODSBox(
                background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                cornerRadius = ODSCorners(all = 6.dp),
                padding = com.telekom.odsystem.foundations.ODSPadding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                ODSText(
                    text = "${model.ratePerMinute}c/m",
                    style = ODSTextStyles.microcopyBold,
                    color = scheme.basicAccent
                )
            }
        }

        // Bottom Details Row with Model info + Voice Call & Message Icon Buttons
        ODSRow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            ODSColumn(
                modifier = Modifier.weight(1f, fill = false),
                gap = 2.dp
            ) {
                ODSText(
                    text = model.name,
                    style = ODSTextStyles.bodySBold,
                    color = HexColor(0xFFFFFFFF)
                )
                val tagStr = if (model.tags.isNotEmpty()) model.tags.take(2)
                    .joinToString(", ") else "Verified"
                ODSText(
                    text = tagStr,
                    style = ODSTextStyles.microcopyRegular,
                    color = HexColor(0xCCFFFFFF)
                )
            }

            // Call and Voice Icons from ODS (same as ModelProfileBottomBar reference)
            ODSRow(
                gap = 6.dp,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Message Icon Button
                ODSBox(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { (onChatClick ?: onClick)() },
                    background = listOf(ODSColorModel(hexColor = HexColor(0x991E1145))),
                    border = ODSBorder(
                        width = 0.5.dp,
                        colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.message),
                        tint = HexColor(0xFFFFFFFF).getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Voice Call Icon Button (Coral Pink)
                ODSBox(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { (onVoiceCallClick ?: onClick)() },
                    background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(drawableRes = R.drawable.call),
                        tint = HexColor(0xFFFFFFFF).getColor(),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
