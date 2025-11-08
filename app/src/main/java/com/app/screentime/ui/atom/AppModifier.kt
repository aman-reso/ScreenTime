package com.app.screentime.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


fun Modifier.glassBottomSheetBackground(): Modifier =
    this
        .background(
            Brush.verticalGradient(
                listOf(
                    Color(0xFF1C1C1C),
                    Color(0xFF0D0D0D)
                )
            )
        )
        .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))


//.border(
//            width = 1.dp,
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    Color.White.copy(alpha = 0.50f),
//                    Color.White.copy(alpha = 0.00f),
//                    Color.White.copy(alpha = 0.00f),
//                    Color.White.copy(alpha = 0.42f)
//                )
//            ),
//            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
//        )
