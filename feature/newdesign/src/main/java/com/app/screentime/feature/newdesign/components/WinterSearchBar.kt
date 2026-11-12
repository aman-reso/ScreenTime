package com.app.screentime.feature.newdesign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.feature.newdesign.R

object WinterSearchDefaults {
    val Height = 47.dp
    val CornerRadius = 16.dp
    val IconSize = 18.dp

    // Figma Design Tokens (Node 51:1183 "Luma / Search creators")
    val Background: Color @Composable @ReadOnlyComposable get() = WinterTokens.surfaceCard
    val IconTint: Color @Composable @ReadOnlyComposable get() = WinterTokens.iconMuted
    val TextColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textPrimary
    val PlaceholderColor: Color @Composable @ReadOnlyComposable get() = WinterTokens.textSecondary
    val FontSize = 13.sp
}

/**
 * WinterSearchBar - Pure Jetpack Compose implementation of Figma "Luma / Search creators" (Node 51:1183).
 *
 * @param query The current search query string.
 * @param onQueryChange Callback invoked when search input changes.
 * @param modifier Applied to search bar container.
 * @param placeholder The hint string shown when search text is empty.
 */
@Composable
fun WinterSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search names, interests, connections"
) {
    val shape = RoundedCornerShape(WinterSearchDefaults.CornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(WinterSearchDefaults.Height)
            .clip(shape)
            .background(WinterSearchDefaults.Background, shape)
            .border(1.dp, WinterTokens.borderSubtle, shape)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = WinterSearchDefaults.IconTint,
                modifier = Modifier.size(WinterSearchDefaults.IconSize)
            )

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    color = WinterSearchDefaults.TextColor,
                    fontSize = WinterSearchDefaults.FontSize,
                    fontWeight = FontWeight.Normal
                ),
                singleLine = true,
                cursorBrush = SolidColor(WinterTokens.actionPrimary),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = WinterSearchDefaults.PlaceholderColor,
                            fontSize = WinterSearchDefaults.FontSize,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Preview(name = "WinterSearchBar Preview", showBackground = true)
@Composable
private fun WinterSearchBarPreview() {
    var query by remember { mutableStateOf("") }
    Surface(color = WinterTokens.backgroundCanvas) {
        Box(modifier = Modifier.padding(16.dp)) {
            WinterSearchBar(
                query = query,
                onQueryChange = { query = it }
            )
        }
    }
}

