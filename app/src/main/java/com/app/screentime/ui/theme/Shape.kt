package com.app.screentime.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Material 3 Expressive Shapes
// Characterized by more rounded, organic corners
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp), // Increased from 12.dp for a softer look
    large = RoundedCornerShape(24.dp),  // Increased from 16.dp for cards/containers
    extraLarge = RoundedCornerShape(32.dp) // For bottom sheets and large surfaces
)
