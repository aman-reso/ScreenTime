package com.app.screentime.consent.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors

@Preview(showBackground = true)
@Composable
fun ConsentingSection(
    title: String = "Title",
    description: String = "Description",
    checked: Boolean = false,
    isMandatory: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    val animatedChecked by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.weight(1f)) {
            AppText(text = title, style = AppTextStyle.Body)
            AppText(text = description, style = AppTextStyle.Label, color = colors.textLight)
        }

        Switch(
            thumbContent = {
                // Fade + scale animation for icon
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
            checked = if (isMandatory) true else checked,
            onCheckedChange = if (isMandatory) {
                onCheckedChange
            } else onCheckedChange,
            enabled = !isMandatory,
            modifier = Modifier
        )
    }
}
