package com.app.screentime.record.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.AppColors

@Composable
fun HourRangeHeader(hour: Int, colors: AppColors) {
    val hourRange = remember(hour) {
        val startHour = String.format("%02d:00", hour)
        val endHour = String.format("%02d:00", (hour + 1) % 24)
        "$startHour-$endHour"
    }

    AppText(
        text = hourRange,
        style = AppTextStyle.SubTitle,
        fontWeight = FontWeight.Bold,
        color = colors.textPrimary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

