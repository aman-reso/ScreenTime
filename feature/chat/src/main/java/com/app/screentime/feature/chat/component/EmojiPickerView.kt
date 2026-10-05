package com.app.screentime.feature.chat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.core.ui.theme.zonaODSTheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

data class EmojiCategory(
    val title: String,
    val icon: String,
    val emojis: List<String>
)

val EMOJI_CATEGORIES = listOf(
    EmojiCategory(
        title = "Smileys",
        icon = "😀",
        emojis = listOf(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣",
            "🥲", "☺️", "😊", "😇", "🙂", "🙃", "😉", "😌",
            "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛",
            "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🥸",
            "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕",
            "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢",
            "😭", "😤", "😠", "😡", "🤬", "🤯", "😳", "🥵",
            "🥶", "😱", "😨", "😰", "😥", "😓", "🤗", "🤔",
            "🤭", "🤫", "🤥", "😶", "😐", "😑", "😬", "🙄",
            "😯", "😦", "😧", "😮", "😲", "🥱", "😴", "🤤"
        )
    ),
    EmojiCategory(
        title = "Gestures",
        icon = "👍",
        emojis = listOf(
            "👍", "👎", "👊", "✊", "🤛", "🤜", "🤞", "✌️",
            "🤟", "🤘", "👌", "🤌", "🤏", "👈", "👉", "👆",
            "👇", "☝️", "✋", "🤚", "🖐️", "🖖", "👋", "🤙",
            "💪", "🙏", "👏", "🙌", "👐", "🤲", "🤝", "✍️",
            "💅", "🤳", "💃", "🕺", "🚶", "🏃", "🙆", "🙅"
        )
    ),
    EmojiCategory(
        title = "Hearts",
        icon = "❤️",
        emojis = listOf(
            "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍",
            "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
            "💘", "💝", "💟", "💌", "💋", "🫂", "😻", "😽",
            "💐", "🌹", "🥀", "🌺", "🌸", "🌷", "🌻", "🌼"
        )
    ),
    EmojiCategory(
        title = "Popular",
        icon = "🔥",
        emojis = listOf(
            "🔥", "✨", "🌟", "⭐", "💫", "🎉", "🎊", "🎈",
            "🎁", "🏆", "🥇", "🥈", "🥉", "🎯", "🚀", "💯",
            "⚡", "💥", "🌈", "☀️", "🌙", "🍕", "🍔", "🍟",
            "🍦", "🍩", "🍫", "🍿", "🍻", "🥂", "☕", "🍾"
        )
    )
)

/**
 * Native Compose Emoji Picker View with category tabs and a backspace button.
 */
@Composable
fun EmojiPickerView(
    scheme: ODSTheme = zonaODSTheme,
    onEmojiSelected: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    val currentCategory = EMOJI_CATEGORIES.getOrElse(selectedCategoryIndex) { EMOJI_CATEGORIES.first() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(scheme.basicBackgroundCard.getColor())
    ) {
        // Top category tab bar with Backspace button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EMOJI_CATEGORIES.forEachIndexed { index, category ->
                    val isSelected = index == selectedCategoryIndex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) scheme.basicAccent.getColor().copy(alpha = 0.15f)
                                else Color.Transparent
                            )
                            .clickable { selectedCategoryIndex = index }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.icon,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            // Backspace delete button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(scheme.basicStroke.getColor().copy(alpha = 0.3f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackspace
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⌫",
                    fontSize = 18.sp,
                    color = scheme.basicText.getColor()
                )
            }
        }

        // Emoji Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(currentCategory.emojis) { emoji ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onEmojiSelected(emoji) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emoji,
                        fontSize = 26.sp
                    )
                }
            }
        }
    }
}
