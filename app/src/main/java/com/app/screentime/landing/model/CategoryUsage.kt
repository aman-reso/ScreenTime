package com.app.screentime.landing.model

import com.app.screentime.landing.util.AppCategoryUtils

/**
 * Data model for category-wise usage
 */
data class CategoryUsage(
    val category: AppCategoryUtils.AppCategory,
    val totalScreenTime: Long,
    val formattedTime: String,
    val percentage: Float // Percentage of total screen time
)

