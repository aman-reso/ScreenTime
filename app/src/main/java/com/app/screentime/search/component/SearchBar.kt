package com.app.screentime.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun GlassSearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    placeholder: String = "Search by username...",
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    autoFocus: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Track focused state for UI / debug if needed
    var isFocused by remember { mutableStateOf(false) }
    
    // Auto focus when autoFocus is true
    androidx.compose.runtime.LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(MaterialTheme.shapes.extraLarge) // More rounded, pill-like shape
            .background(colors.card)
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(horizontal = 0.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            enabled = enabled,
            readOnly = false,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.success),
            modifier = Modifier
                .matchParentSize()
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    isFocused = state.isFocused
                }
                .padding(horizontal = 20.dp),
            textStyle = TextStyle(
                color = colors.textPrimary,
                fontSize = 16.sp
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // @ symbol on the left
                    AppText(
                        text = "@",
                        color = colors.textSecondary,
                        style = AppTextStyle.Body,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    
                    // Text field in the middle
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty() && !isFocused) {
                            AppText(
                                text = placeholder,
                                color = colors.textHint,
                                style = AppTextStyle.Label
                            )
                        }
                        innerTextField()
                    }

                    // Microphone icon on the right
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice search",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )
    }
}


@Composable
fun GlassSearchBarPlaceholder(
    modifier: Modifier = Modifier,
    placeholder: String = "Search by username...",
    onClick: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(colors.card)
            .border(
                width = 1.dp,
                color = colors.border.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.extraLarge
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
            ) {
                onClick()
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // @ symbol on the left
            AppText(
                text = "@",
                color = colors.textSecondary,
                style = AppTextStyle.Body,
                modifier = Modifier.padding(start = 4.dp)
            )

            // Placeholder text
            AppText(
                text = placeholder,
                color = colors.textHint,
                style = AppTextStyle.Label,
                modifier = Modifier.weight(1f)
            )

            // Microphone icon on the right
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice search",
                tint = colors.textSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

