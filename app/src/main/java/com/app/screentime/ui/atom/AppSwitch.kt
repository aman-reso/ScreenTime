package com.app.screentime.ui.atom

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.theme.LocalAppColors

@Composable
fun AppSwitch(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    val animatedChecked by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    Switch(
        thumbContent = {
            val scale by animateFloatAsState(
                targetValue = if (checked) 1f else 0.9f,
                animationSpec = tween(200)
            )
            Icon(
                imageVector = if (checked) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = "Checked",
                modifier = Modifier
                    .padding(4.dp)
                    .scale(scale),
                tint = if (checked) colors.textPrimary else colors.textLight.copy(alpha = 1 - animatedChecked)
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = colors.textMuted,
            checkedTrackColor = colors.accent,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = colors.textPrimary,
            uncheckedTrackColor = colors.textMuted,
            uncheckedBorderColor = Color.Transparent
        ),
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier
    )
}