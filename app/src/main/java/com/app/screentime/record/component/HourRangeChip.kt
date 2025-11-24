package com.app.screentime.record.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.AppColors

@Composable
fun HourRangeChip(
    hour: Int,
    colors: AppColors,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val hourRange = remember(hour) {
        val startHour = "%02d:00".format(hour)
        val endHour = "%02d:00".format((hour + 1) % 24)
        "$startHour - $endHour"
    }

    FilterChip(
        leadingIcon =
            if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                    )
                }
            } else {
                null
            },
        selected = selected,
        onClick = onClick,
        label = {
            AppText(
                text = hourRange,
                style = AppTextStyle.Label,
                color = if (selected) colors.textOnPrimary else colors.textPrimary
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colors.accent,
            containerColor = colors.card
        )
    )
}

