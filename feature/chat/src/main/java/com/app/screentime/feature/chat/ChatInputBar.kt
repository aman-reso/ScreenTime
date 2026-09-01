package com.app.screentime.feature.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.ODSTextStyles
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun ChatInputBar(
    inputText: String,
    modelName: String,
    scheme: ODSTheme,
    onInputTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ODSRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        gap = 10.dp
    ) {
        // Capsule Input Container
        ODSBox(
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            background = listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(all = 24.dp),
            border = ODSBorder(
                width = 1.dp,
                colorList = listOf(ODSColorModel(hexColor = scheme.basicStrokeSubtle))
            ),
            padding = ODSPadding(left = 16.dp, right = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Text Input Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (inputText.isEmpty()) {
                        ODSText(
                            text = "Message $modelName",
                            style = ODSTextStyles.bodySRegular,
                            color = scheme.basicTextRecessive
                        )
                    }
                    BasicTextField(
                        value = inputText,
                        onValueChange = onInputTextChanged,
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = scheme.basicText.getColor()
                        ),
                        cursorBrush = SolidColor(scheme.functionalDestructiveStandard.getColor()),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Mic button without heavy background
                ODSBox(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onVoiceClick),
                    contentAlignment = Alignment.Center
                ) {
                    ODSIcon(
                        iconModel = ODSIconModel(imageVector = Icons.Outlined.Mic),
                        tint = scheme.basicTextRecessive.getColor()
                    )
                }
            }
        }

        // Dedicated Coral Pink Send Button with paper airplane send icon (compact 44.dp)
        ODSBox(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .clickable(onClick = onSendMessage),
            background = listOf(ODSColorModel(hexColor = scheme.functionalDestructiveStandard)),
            contentAlignment = Alignment.Center
        ) {
            ODSIcon(
                iconModel = ODSIconModel(drawableRes = R.drawable.send),
                tint = Color.White
            )
        }
    }
}


