package com.telekom.odsystem.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.onClick(transparentClick: Boolean = true, clickAction: () -> Unit): Modifier =
    if (transparentClick) {
        clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = { clickAction() }
        )
    } else {
        clickable { clickAction() }
    }

@Composable
fun VerticalSpace(dp: Int) = Spacer(Modifier.height(dp.dp))

@Composable
fun HorizontalSpace(dp: Int) = Spacer(Modifier.width(dp.dp))
