package com.app.screentime.record.repository

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

/**
 * NetworkUsageHelper is a utility class responsible for gathering network usage statistics for
 * Android applications. It provides methods to fetch network usage data for mobile and Wi-Fi
 * connections over specified time intervals.
 */
class NetworkUsageHelper constructor(val context: Context) {
    val networkStatsManager =
        context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

    /**
     * Fetches Wi-Fi usage statistics for a specified time interval.
     *
     * @param networkStatsManager The NetworkStatsManager used to query network usage.
     * @param start               The start time of the interval in milliseconds.
     * @param end                 The end time of the interval in milliseconds.
     * @return A map where keys are app UIDs and values are the corresponding Wi-Fi usage in KBs.
     */
    fun fetchWifiUsageForInterval(
        start: Long,
        end: Long,
    ): Map<Int, Long> =
        fetchNetworkUsageForInterval(ConnectivityManager.TYPE_WIFI, start, end)

    /**
     * Fetches mobile data usage statistics for a specified time interval.
     *
     * @param networkStatsManager The NetworkStatsManager used to query network usage.
     * @param start               The start time of the interval in milliseconds.
     * @param end                 The end time of the interval in milliseconds.
     * @return A map where keys are app UIDs and values are the corresponding mobile data usage in KBs.
     */
    fun fetchMobileUsageForInterval(
        start: Long,
        end: Long,
    ): Map<Int, Long> =
        fetchNetworkUsageForInterval(
            ConnectivityManager.TYPE_MOBILE,
            start,
            end
        )

    /**
     * Fetches network usage statistics for a specified network type for today.
     *
     * @param networkStatsManager The NetworkStatsManager used to query network usage.
     * @param networkType The type of network (e.g., ConnectivityManager.TYPE_WIFI or TYPE_MOBILE).
     * @return A map where keys are app UIDs and values are the corresponding data usage in KBs.
     */
    fun fetchNetworkUsageForTodayTillNow(
        networkStatsManager: NetworkStatsManager,
        networkType: Int,
    ): Map<Int, Long> {
        val midNightCal = Calendar.getInstance()
        midNightCal[Calendar.HOUR_OF_DAY] = 0
        midNightCal[Calendar.MINUTE] = 0
        midNightCal[Calendar.SECOND] = 0
        midNightCal[Calendar.MILLISECOND] = 0

        val start = midNightCal.timeInMillis
        val end = System.currentTimeMillis()

        return fetchNetworkUsageForInterval(networkType, start, end)
    }

    /**
     * Fetches network usage statistics for a specified network type over a given time interval.
     *
     * @param networkStatsManager The NetworkStatsManager used to query network usage.
     * @param networkType The type of network (e.g., ConnectivityManager.TYPE_WIFI or TYPE_MOBILE).
     * @param start The start time of the interval in milliseconds.
     * @param end The end time of the interval in milliseconds.
     * @return A map where keys are app UIDs and values are the corresponding data usage in KBs.
     */
    private fun fetchNetworkUsageForInterval(
        networkType: Int,
        start: Long,
        end: Long,
    ): Map<Int, Long> {
        val usageMap = mutableMapOf<Int, Long>()
        try {
            val networkStats = networkStatsManager.querySummary(networkType, null, start, end)

            networkStats.use {
                val bucket = NetworkStats.Bucket()

                while (networkStats.hasNextBucket()) {
                    networkStats.getNextBucket(bucket)
                    val uid = bucket.uid
                    usageMap[uid] =
                        usageMap.getOrDefault(uid, 0L) + (bucket.rxBytes + bucket.txBytes)
                }

            }
        } catch (e: Exception) {
            Log.e(
                "tag",
                "fetchNetworkUsageForInterval: Error fetching network usage for type $networkType",
                e
            )
        }

        return usageMap
            .mapValues { it.value }  // Convert bytes to KBs
            .filterValues { it > 0L }       // Only keep entries with usage
    }


}
