package com.app.screentime.network.model

/**
 * Network information data model
 */
data class NetworkInfo(
    val connectionType: ConnectionType,
    val isConnected: Boolean,
    val signalStrength: Int? = null, // Signal strength in dBm (for mobile/WiFi)
    val networkSpeed: NetworkSpeed? = null, // Network speed information
    val ipAddress: String? = null, // IP address
    val networkOperator: String? = null, // Mobile network operator name
    val wifiSSID: String? = null, // WiFi SSID
    val isRoaming: Boolean = false // Whether device is roaming
)

/**
 * Network connection type
 */
enum class ConnectionType {
    WIFI,
    MOBILE,
    ETHERNET,
    VPN,
    NONE
}

/**
 * Network speed information
 */
data class NetworkSpeed(
    val downloadSpeed: Long = 0, // in bytes per second
    val uploadSpeed: Long = 0, // in bytes per second
    val latency: Int = 0 // in milliseconds
)

/**
 * Network health status
 */
enum class NetworkHealth {
    EXCELLENT,
    GOOD,
    FAIR,
    POOR,
    NO_CONNECTION
}

