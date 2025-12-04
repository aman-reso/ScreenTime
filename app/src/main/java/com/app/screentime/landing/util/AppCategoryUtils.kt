package com.app.screentime.landing.util

import android.content.Context
import android.content.pm.ApplicationInfo
import com.app.screentime.R
import com.app.screentime.data.entity.AppUsage

/**
 * Utility class for categorizing apps
 */
object AppCategoryUtils {

    /**
     * App categories
     */
    enum class AppCategory(val displayName: String, val drawableRes: Int) {
        SOCIAL_MEDIA("Social Media", R.drawable.ic_social_media),
        ENTERTAINMENT("Entertainment", R.drawable.entertainment),
        PRODUCTIVITY("Productivity", R.drawable.productivity),
        GAMES("Games", R.drawable.ic_games),
        COMMUNICATION("Communication", R.drawable.ic_communication),
        SHOPPING("Shopping", R.drawable.ic_shopping),
        NEWS_READING("News & Reading", R.drawable.ic_news),
        UTILITIES("Utilities", R.drawable.ic_utilities),
        OTHERS("Others", R.drawable.ic_others),
        AUDIO("Audio", R.drawable.ic_audio),
        VIDEO("Video", R.drawable.ic_video)
    }


    private fun getCategoryTitle(category: Int?): AppCategory {
        return when (category) {
            ApplicationInfo.CATEGORY_GAME -> AppCategory.GAMES
            ApplicationInfo.CATEGORY_AUDIO -> AppCategory.AUDIO
            ApplicationInfo.CATEGORY_VIDEO -> AppCategory.VIDEO
            ApplicationInfo.CATEGORY_IMAGE -> AppCategory.ENTERTAINMENT
            ApplicationInfo.CATEGORY_SOCIAL -> AppCategory.SOCIAL_MEDIA
            ApplicationInfo.CATEGORY_NEWS -> AppCategory.NEWS_READING
            ApplicationInfo.CATEGORY_MAPS -> AppCategory.UTILITIES
            ApplicationInfo.CATEGORY_PRODUCTIVITY -> AppCategory.PRODUCTIVITY
            ApplicationInfo.CATEGORY_ACCESSIBILITY -> AppCategory.UTILITIES
            else -> AppCategory.OTHERS
        }
    }


    /**
     * Get category for an app based on package name and app name
     */
    fun getCategory(appUsage: AppUsage): AppCategory {
        val packageName = appUsage.packageName?.lowercase() ?: ""
        val appName = appUsage.appName?.lowercase() ?: ""
        appUsage.applicationInfo?.category?.let {
            getCategoryTitle(it).let { category ->
                if (category != AppCategory.OTHERS) {
                    return category
                }
            }
        }
        return when {
            // Social Media
            packageName.contains("instagram") || appName.contains("instagram") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("facebook") || appName.contains("facebook") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("twitter") || appName.contains("twitter") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("x.com") || appName.contains("x.com") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("snapchat") || appName.contains("snapchat") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("tiktok") || appName.contains("tiktok") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("linkedin") || appName.contains("linkedin") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("reddit") || appName.contains("reddit") -> AppCategory.SOCIAL_MEDIA
            packageName.contains("pinterest") || appName.contains("pinterest") -> AppCategory.SOCIAL_MEDIA

            // Entertainment
            packageName.contains("youtube") || appName.contains("youtube") -> AppCategory.ENTERTAINMENT
            packageName.contains("netflix") || appName.contains("netflix") -> AppCategory.ENTERTAINMENT
            packageName.contains("spotify") || appName.contains("spotify") -> AppCategory.ENTERTAINMENT
            packageName.contains("prime") || appName.contains("prime") -> AppCategory.ENTERTAINMENT
            packageName.contains("disney") || appName.contains("disney") -> AppCategory.ENTERTAINMENT
            packageName.contains("hotstar") || appName.contains("hotstar") -> AppCategory.ENTERTAINMENT
            packageName.contains("zee5") || appName.contains("zee5") -> AppCategory.ENTERTAINMENT
            packageName.contains("sonyliv") || appName.contains("sonyliv") -> AppCategory.ENTERTAINMENT
            packageName.contains("music") || appName.contains("music") -> AppCategory.ENTERTAINMENT
            packageName.contains("player") || appName.contains("player") -> AppCategory.ENTERTAINMENT

            // Communication
            packageName.contains("whatsapp") || appName.contains("whatsapp") -> AppCategory.COMMUNICATION
            packageName.contains("telegram") || appName.contains("telegram") -> AppCategory.COMMUNICATION
            packageName.contains("messenger") || appName.contains("messenger") -> AppCategory.COMMUNICATION
            packageName.contains("signal") || appName.contains("signal") -> AppCategory.COMMUNICATION
            packageName.contains("viber") || appName.contains("viber") -> AppCategory.COMMUNICATION
            packageName.contains("skype") || appName.contains("skype") -> AppCategory.COMMUNICATION
            packageName.contains("discord") || appName.contains("discord") -> AppCategory.COMMUNICATION
            packageName.contains("slack") || appName.contains("slack") -> AppCategory.COMMUNICATION

            // Productivity
            packageName.contains("gmail") || appName.contains("gmail") -> AppCategory.PRODUCTIVITY
            packageName.contains("drive") || appName.contains("drive") -> AppCategory.PRODUCTIVITY
            packageName.contains("docs") || appName.contains("docs") -> AppCategory.PRODUCTIVITY
            packageName.contains("sheets") || appName.contains("sheets") -> AppCategory.PRODUCTIVITY
            packageName.contains("slides") || appName.contains("slides") -> AppCategory.PRODUCTIVITY
            packageName.contains("office") || appName.contains("office") -> AppCategory.PRODUCTIVITY
            packageName.contains("word") || appName.contains("word") -> AppCategory.PRODUCTIVITY
            packageName.contains("excel") || appName.contains("excel") -> AppCategory.PRODUCTIVITY
            packageName.contains("powerpoint") || appName.contains("powerpoint") -> AppCategory.PRODUCTIVITY
            packageName.contains("notion") || appName.contains("notion") -> AppCategory.PRODUCTIVITY
            packageName.contains("evernote") || appName.contains("evernote") -> AppCategory.PRODUCTIVITY
            packageName.contains("onenote") || appName.contains("onenote") -> AppCategory.PRODUCTIVITY
            packageName.contains("calendar") || appName.contains("calendar") -> AppCategory.PRODUCTIVITY
            packageName.contains("notes") || appName.contains("notes") -> AppCategory.PRODUCTIVITY

            // Shopping
            packageName.contains("amazon") || appName.contains("amazon") -> AppCategory.SHOPPING
            packageName.contains("flipkart") || appName.contains("flipkart") -> AppCategory.SHOPPING
            packageName.contains("myntra") || appName.contains("myntra") -> AppCategory.SHOPPING
            packageName.contains("paytm") || appName.contains("paytm") -> AppCategory.SHOPPING
            packageName.contains("phonepe") || appName.contains("phonepe") -> AppCategory.SHOPPING
            packageName.contains("gpay") || appName.contains("gpay") -> AppCategory.SHOPPING
            packageName.contains("gpay") || appName.contains("google pay") -> AppCategory.SHOPPING
            packageName.contains("swiggy") || appName.contains("swiggy") -> AppCategory.SHOPPING
            packageName.contains("zomato") || appName.contains("zomato") -> AppCategory.SHOPPING

            // Games
            packageName.contains("game") || appName.contains("game") -> AppCategory.GAMES
            packageName.contains("play") && (packageName.contains("games") || appName.contains("games")) -> AppCategory.GAMES

            // News & Reading
            packageName.contains("news") || appName.contains("news") -> AppCategory.NEWS_READING
            packageName.contains("reader") || appName.contains("reader") -> AppCategory.NEWS_READING
            packageName.contains("kindle") || appName.contains("kindle") -> AppCategory.NEWS_READING
            packageName.contains("books") || appName.contains("books") -> AppCategory.NEWS_READING

            // Utilities
            packageName.contains("camera") || appName.contains("camera") -> AppCategory.UTILITIES
            packageName.contains("gallery") || appName.contains("gallery") -> AppCategory.UTILITIES
            packageName.contains("photos") || appName.contains("photos") -> AppCategory.UTILITIES
            packageName.contains("file") || appName.contains("file") -> AppCategory.UTILITIES
            packageName.contains("settings") || appName.contains("settings") -> AppCategory.UTILITIES
            packageName.contains("calculator") || appName.contains("calculator") -> AppCategory.UTILITIES
            packageName.contains("clock") || appName.contains("clock") -> AppCategory.UTILITIES
            packageName.contains("browser") || appName.contains("browser") -> AppCategory.UTILITIES
            packageName.contains("chrome") || appName.contains("chrome") -> AppCategory.UTILITIES
            packageName.contains("firefox") || appName.contains("firefox") -> AppCategory.UTILITIES

            else -> AppCategory.OTHERS
        }
    }

    /**
     * Group apps by category and calculate total usage per category
     */
    fun groupByCategory(apps: List<AppUsage>): Map<AppCategory, Long> {
        return apps.groupBy { getCategory(it) }
            .mapValues { (_, appList) -> appList.sumOf { it.appScreenTime } }
            .filter { it.value > 0 } // Only include categories with usage
    }
}
