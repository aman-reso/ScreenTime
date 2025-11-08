package com.app.screentime.utils

object AppFilterUtils {
    /**
     * Common launcher app package names
     */
    private val launcherPackageNames = setOf(
        "com.google.android.apps.nexuslauncher",      // Pixel Launcher
        "com.android.launcher",                         // AOSP Launcher
        "com.android.launcher2",                        // Legacy Launcher
        "com.android.launcher3",                        // AOSP Launcher 3
        "com.google.android.launcher",                  // Google Launcher
        "com.sec.android.app.launcher",                 // Samsung Launcher
        "com.huawei.android.launcher",                  // Huawei Launcher
        "com.miui.home",                                // MIUI Launcher
        "com.oneplus.launcher",                         // OnePlus Launcher
        "com.lge.launcher2",                           // LG Launcher
        "com.sony.launcher",                           // Sony Launcher
        "com.oppo.launcher",                           // Oppo Launcher
        "com.realme.launcher",                         // Realme Launcher
        "com.vivo.launcher",                           // Vivo Launcher
        "me.ele.launcher",                             // Others
        "com.novalauncher.TeslaUnleashed",             // Nova Launcher
        "com.teslacoilsw.launcher",                    // Nova Launcher
        "com.anddoes.launcher",                        // Apex Launcher
        "com.lenovo.launcher",                         // Lenovo Launcher
        "com.zte.launcher"                             // ZTE Launcher
    )

    /**
     * Check if a package name belongs to a launcher app
     */
    fun isLauncherApp(packageName: String): Boolean {
        return launcherPackageNames.contains(packageName) ||
                packageName.contains("launcher", ignoreCase = true)
    }

    /**
     * Filter out launcher apps from a list of app usages
     */
    fun filterLauncherApps(appUsages: List<com.app.screentime.data.entity.AppUsage>): List<com.app.screentime.data.entity.AppUsage> {
        return appUsages.filter { !isLauncherApp(it.packageName ?: "") }
    }
}

