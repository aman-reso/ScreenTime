package com.app.screentime.feature.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.core.model.ChatMessage
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: ChatMessage,
    isMe: Boolean,
    scheme: ODSTheme,
    avatarUrl: String? = null
) {
    val alignment = if (isMe) Alignment.End else Alignment.Start
    val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timestamp))

    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        if (!isMe) {
            // Model Message Bubble (0.7f width)
            ODSBox(
                modifier = Modifier.fillMaxWidth(0.7f),
                background = listOf(ODSColorModel(hexColor = HexColor(0xDD221644))),
                cornerRadius = ODSCorners(
                    topLeft = 6.dp,
                    topRight = 16.dp,
                    bottomLeft = 16.dp,
                    bottomRight = 16.dp
                ),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
                ),
                padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                ODSColumn(gap = 4.dp) {
                    ODSText(
                        text = message.text,
                        style = ODSTextStyles.bodySRegular,
                        color = HexColor(0xFFFFFFFF)
                    )
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSText(
                            text = timeStr,
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor(0x99FFFFFF)
                        )
                    }
                }
            }
        } else {
            // User Message Bubble (0.7f width)
            ODSBox(
                modifier = Modifier.fillMaxWidth(0.7f),
                background = listOf(ODSColorModel(hexColor = HexColor(0xFF5B29A6))),
                cornerRadius = ODSCorners(
                    topLeft = 16.dp,
                    topRight = 6.dp,
                    bottomLeft = 16.dp,
                    bottomRight = 16.dp
                ),
                border = ODSBorder(
                    width = 1.dp,
                    colorList = listOf(ODSColorModel(hexColor = HexColor(0x55BC96FF)))
                ),
                padding = ODSPadding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                ODSColumn(gap = 4.dp) {
                    ODSText(
                        text = message.text,
                        style = ODSTextStyles.bodySRegular,
                        color = HexColor(0xFFFFFFFF)
                    )
                    ODSColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSText(
                            text = timeStr,
                            style = ODSTextStyles.microcopyRegular,
                            color = HexColor(0xCCFFFFFF)
                        )
                    }
                }
            }
        }
    }
}
