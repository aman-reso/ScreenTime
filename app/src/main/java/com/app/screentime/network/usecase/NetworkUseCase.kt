package com.app.screentime.network.usecase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import com.app.screentime.network.model.ConnectionType
import com.app.screentime.network.model.NetworkInfo
import com.app.screentime.network.model.NetworkSpeed
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for retrieving network information from the device
 */
@Singleton
class NetworkUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        context.getSystemService(Context.WIFI_SERVICE) as? WifiManager
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    /**
     * Get current network information
     */
    fun getNetworkInfo(): NetworkInfo {
        val network = connectivityManager.activeNetwork ?: return getDefaultNetworkInfo()
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return getDefaultNetworkInfo()

        val connectionType = when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.MOBILE
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
            else -> ConnectionType.NONE
        }

        val isConnected = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        // Get signal strength for WiFi
        var signalStrength: Int? = null
        var wifiSSID: String? = null
        if (connectionType == ConnectionType.WIFI && wifiManager != null) {
            val wifiInfo = wifiManager.connectionInfo
            signalStrength = wifiInfo.rssi // Signal strength in dBm
            wifiSSID = wifiInfo.ssid?.replace("\"", "") // Remove quotes from SSID
        }

        // Get signal strength for Mobile
        var networkOperator: String? = null
        var isRoaming = false
        if (connectionType == ConnectionType.MOBILE && telephonyManager != null) {
            try {
                // Get signal strength level (0-4) and convert to approximate dBm
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val signalStrengthInfo = telephonyManager.signalStrength
                    signalStrength = signalStrengthInfo?.let { ss ->
                        val cellSs = ss.cellSignalStrengths.firstOrNull()

                        cellSs?.let {
                            val dbm = it.dbm
                            if (dbm != Int.MAX_VALUE) {
                                dbm
                            } else {
                                // fallback to level
                                when (it.level) {
                                    0 -> -113
                                    1 -> -103
                                    2 -> -93
                                    3 -> -83
                                    4 -> -73
                                    else -> null
                                }
                            }
                        }
                    }
                }
                networkOperator = telephonyManager.networkOperatorName
                isRoaming = telephonyManager.isNetworkRoaming
            } catch (e: Exception) {
                // Handle permission or other errors silently
            }
        }

        // Get IP address
        val ipAddress = getLocalIpAddress()

        return NetworkInfo(
            connectionType = connectionType,
            isConnected = isConnected,
            signalStrength = signalStrength,
            networkSpeed = null, // Network speed requires active measurement
            ipAddress = ipAddress,
            networkOperator = networkOperator,
            wifiSSID = wifiSSID,
            isRoaming = isRoaming
        )
    }

    /**
     * Get local IP address
     */
    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is java.net.Inet4Address) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore
        }
        return null
    }

    /**
     * Get default network info when no network is available
     */
    private fun getDefaultNetworkInfo(): NetworkInfo {
        return NetworkInfo(
            connectionType = ConnectionType.NONE,
            isConnected = false,
            signalStrength = null,
            networkSpeed = null,
            ipAddress = null,
            networkOperator = null,
            wifiSSID = null,
            isRoaming = false
        )
    }
}

