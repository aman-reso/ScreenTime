//package com.app.screentime.sync
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.Network
//import android.net.NetworkCapabilities
//import android.net.NetworkRequest
//
///**
// * Monitors network connectivity and triggers sync when internet becomes available
// */
//class NetworkMonitor(private val context: Context) {
//
//    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            super.onAvailable(network)
//            android.util.Log.d("NetworkMonitor", "Network available - triggering sync")
//            // Trigger sync when network becomes available
//            MyWorker.triggerOnNetworkAvailable(context)
//        }
//
//        override fun onLost(network: Network) {
//            super.onLost(network)
//            android.util.Log.d("NetworkMonitor", "Network lost")
//        }
//
//        override fun onCapabilitiesChanged(
//            network: Network,
//            networkCapabilities: NetworkCapabilities
//        ) {
//            super.onCapabilitiesChanged(network, networkCapabilities)
//            val hasInternet = networkCapabilities.hasCapability(
//                NetworkCapabilities.NET_CAPABILITY_INTERNET
//            )
//            val hasTransport = networkCapabilities.hasTransport(
//                NetworkCapabilities.TRANSPORT_WIFI
//            ) || networkCapabilities.hasTransport(
//                NetworkCapabilities.TRANSPORT_CELLULAR
//            )
//
//            if (hasInternet && hasTransport) {
//                android.util.Log.d("NetworkMonitor", "Network capabilities changed - internet available")
//                MyWorker.triggerOnNetworkAvailable(context)
//            }
//        }
//    }
//
//    fun startMonitoring() {
//        val networkRequest = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .build()
//
//        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//        android.util.Log.d("NetworkMonitor", "Started monitoring network")
//    }
//
//    fun stopMonitoring() {
//        connectivityManager.unregisterNetworkCallback(networkCallback)
//        android.util.Log.d("NetworkMonitor", "Stopped monitoring network")
//    }
//
//    companion object {
//        private var instance: NetworkMonitor? = null
//
//        fun getInstance(context: Context): NetworkMonitor {
//            if (instance == null) {
//                instance = NetworkMonitor(context.applicationContext)
//            }
//            return instance!!
//        }
//    }
//}
