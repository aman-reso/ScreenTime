package com.app.screentime.location.usecase

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import com.app.screentime.location.repository.LocationRepository
import com.app.screentime.network.model.LocationData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import android.Manifest
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityLte
import android.telephony.CellInfo
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.location.LocationManagerCompat
import com.app.screentime.utils.DateUtils
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlin.coroutines.resume

/**
 * Use case for location operations
 * Handles all business logic for fetching and managing location
 */

data class LocationData(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val lastUpdated: String? = null,
    val shareLocation: Boolean = false
)

class LocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val GEOLOCATION_API_KEY =
            "YOUR_GOOGLE_API_KEY_HERE" // Obtain from Google Cloud Console, enable Geolocation API
        private const val TAG = "LocationUseCase"
    }

    /* ---------------------------------------------------
     * PUBLIC API
     * --------------------------------------------------- */

    suspend fun fetchCurrentLocation(
        useHighAccuracy: Boolean = true
    ): Result<LocationData> = try {
        val location = if (isLocationEnabled() && isGpsEnabled()) {
            getFreshLocation(useHighAccuracy)
        } else {
            getLastKnownLocation()
        } ?: getLocationFromCellTowers()

        if (location == null) {
            Result.failure(Exception("Location unavailable"))
        } else {
            Result.success(
                LocationData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = getAddressFromLocation(location),
                    lastUpdated = ZonedDateTime.now().toString(),
                    shareLocation = false
                )
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


    suspend fun getUserLastLocation(username: String): Result<com.app.screentime.network.model.UserLastLocationData> {
        return locationRepository.getUserLastLocation(username)
    }

    suspend fun syncLocationToServer(locationData: LocationData): Result<com.app.screentime.network.model.LocationSyncResponse> {
        // Expected format: '2024-01-15T10:00:00Z'
        val iso8601Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val utcZone = java.time.ZoneId.of("UTC")

        val lastSyncTime = try {
            locationData.lastUpdated?.let { existingDate ->
                try {
                    // Try parsing with ZonedDateTime first
                    val zonedDateTime = ZonedDateTime.parse(existingDate)
                    zonedDateTime.withZoneSameInstant(utcZone)
                        .format(iso8601Formatter)
                } catch (e: Exception) {
                    // If that fails, try using DateUtils (Joda Time) to parse
                    try {
                        val dateTime = DateUtils.parseISO8601(existingDate)
                        val instant = Instant.ofEpochMilli(dateTime.millis)
                        instant.atZone(utcZone).format(iso8601Formatter)
                    } catch (e2: Exception) {
                        // Fallback to current time in UTC
                        Instant.now().atZone(utcZone).format(iso8601Formatter)
                    }
                }
            } ?: Instant.now().atZone(utcZone).format(iso8601Formatter)
        } catch (e: Exception) {
            // Final fallback to current time in UTC
            Instant.now().atZone(utcZone).format(iso8601Formatter)
        }

        val request = com.app.screentime.network.model.LocationSyncRequest(
            latitude = locationData.latitude ?: 0.0,
            longitude = locationData.longitude ?: 0.0,
            address = locationData.address,
            lastSyncTime = lastSyncTime
        )
        return locationRepository.syncLocation(request)
    }


    /* ---------------------------------------------------
     * LOCATION CORE LOGIC
     * --------------------------------------------------- */

    private fun isLocationEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun isGpsEnabled(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * GPS → Network fallback (only when location toggle is ON)
     */
    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(
        useHighAccuracy: Boolean
    ): Location? = suspendCancellableCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        var resumed = false

        fun resumeOnce(location: Location?) {
            if (!resumed) {
                resumed = true
                cont.resume(location)
            }
        }

        // 1️⃣ Try cached first (FAST)
        client.lastLocation.addOnSuccessListener { last ->
            if (last != null && last.accuracy <= 50 && System.currentTimeMillis() - last.time <= 30_000) {
                resumeOnce(last)
            }
        }

        // 2️⃣ Request fresh update
        val priority = if (useHighAccuracy && isGpsEnabled()) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val request = LocationRequest.Builder(priority, 5000)
            .setMinUpdateDistanceMeters(10f)
            .setMaxUpdates(1)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                client.removeLocationUpdates(this)
                resumeOnce(result.lastLocation)
            }
        }
        client.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        // 3️⃣ Hard timeout (15s)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            client.removeLocationUpdates(callback)
            resumeOnce(null)
        }, 15_000)

        cont.invokeOnCancellation {
            client.removeLocationUpdates(callback)
            handler.removeCallbacksAndMessages(null)
        }
    }

    /**
     * Used ONLY when system location toggle is OFF
     */
    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(): Location? {
        val client = LocationServices.getFusedLocationProviderClient(context)
        return try {
            val location = client.lastLocation.await()
            if (location != null && location.accuracy <= 100 && System.currentTimeMillis() - location.time <= 60 * 60 * 1000) { // 1 hour
                location
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Fallback to get approximate location using cell towers via Google Geolocation API.
     * Requires INTERNET permission and a valid API key.
     * Handles cases where location services are off.
     */
    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    private suspend fun getLocationFromCellTowers(): Location? = withContext(Dispatchers.IO) {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isLocationEnabled =
            LocationManagerCompat.isLocationEnabled(locationManager)

        Log.d(TAG, "isLocationEnabled: $isLocationEnabled")


        val executor = Executors.newSingleThreadExecutor()

        telephonyManager.requestCellInfoUpdate(
            executor,
            object : TelephonyManager.CellInfoCallback() {
                override fun onCellInfo(cellInfoList: List<CellInfo>) {
                    if (cellInfoList.isEmpty()) {
                        Log.d(TAG, "CellInfoCallback: empty list (this is normal)")
                        return
                    }

                    for (cellInfo in cellInfoList) {
                        val registered = cellInfo.isRegistered
                        val ageMs = System.currentTimeMillis() - cellInfo.timeStamp

                        when (cellInfo) {
                            is CellInfoLte -> {
                                val id = cellInfo.cellIdentity
                                val signal = cellInfo.cellSignalStrength

                                Log.d(
                                    TAG,
                                    "LTE | registered=$registered " +
                                            "mcc=${id.mccString} mnc=${id.mncString} " +
                                            "ci=${id.ci} tac=${id.tac} pci=${id.pci} " +
                                            "rsrp=${signal.rsrp} rsrq=${signal.rsrq} ageMs=$ageMs"
                                )
                            }

                            is CellInfoNr -> { // 5G
                                val id = cellInfo.cellIdentity
                                val signal = cellInfo.cellSignalStrength

                                Log.d(
                                    TAG,
                                    "NR(5G) | registered=$registered " +
                                            "mcc=${id} mnc=${id} " +
                                            "nci=${id} tac=${id} " +
                                            "ssRsrp=${signal} ageMs=$ageMs"
                                )
                            }

                            is CellInfoWcdma -> {
                                val id = cellInfo.cellIdentity
                                val signal = cellInfo.cellSignalStrength

                                Log.d(
                                    TAG,
                                    "WCDMA | registered=$registered " +
                                            "cid=${id.cid} lac=${id.lac} dbm=${signal.dbm}"
                                )
                            }

                            is CellInfoGsm -> {
                                val id = cellInfo.cellIdentity
                                val signal = cellInfo.cellSignalStrength

                                Log.d(
                                    TAG,
                                    "GSM | registered=$registered " +
                                            "cid=${id.cid} lac=${id.lac} dbm=${signal.dbm}"
                                )
                            }

                            is CellInfoCdma -> {
                                val id = cellInfo.cellIdentity
                                val signal = cellInfo.cellSignalStrength

                                Log.d(
                                    TAG,
                                    "CDMA | registered=$registered " +
                                            "networkId=${id.networkId} baseStationId=${id.basestationId} " +
                                            "dbm=${signal.dbm}"
                                )
                            }
                        }
                    }
                }

                override fun onError(errorCode: Int, detail: Throwable?) {
                    Log.e(TAG, "Cell info update error: $errorCode", detail)
                }
            }
        )


        // Get cell info; if empty, request update
        var cellInfoList: List<CellInfo> = telephonyManager.allCellInfo
        if (cellInfoList.isEmpty()) {
            try {
                cellInfoList =
                    suspendCancellableCoroutine { cont: CancellableContinuation<List<CellInfo>> ->
                        val executor = Executors.newSingleThreadExecutor()
                        telephonyManager.requestCellInfoUpdate(
                            executor,
                            object : TelephonyManager.CellInfoCallback() {
                                override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
                                    cont.resume(cellInfo)
                                }

                                override fun onError(errorCode: Int, detail: Throwable?) {
                                    Log.e(TAG, "Cell info update error: $errorCode", detail)
                                    cont.resume(emptyList())
                                }
                            })
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to request cell info update", e)
                return@withContext null
            }
        }

        if (cellInfoList.isEmpty()) {
            Log.e(TAG, "No cell info available")
            return@withContext null
        }

        // Prepare cellTowers array
        val cellTowersArray = JSONArray()
        var radioType: String? = null

        for (info in cellInfoList) {
            when (info) {
                is CellInfoLte -> {
                    val identity = info.cellIdentity
                    if (identity.ci != CellInfo.UNAVAILABLE) {
                        val mcc = identity.mccString?.toIntOrNull() ?: continue
                        val mnc = identity.mncString?.toIntOrNull() ?: continue
                        val cellObj = JSONObject().apply {
                            put("cellId", identity.ci)
                            put(
                                "locationAreaCode",
                                identity.tac.takeIf { it != CellInfo.UNAVAILABLE } ?: continue)
                            put("mobileCountryCode", mcc)
                            put("mobileNetworkCode", mnc)
                            put("signalStrength", info.cellSignalStrength.dbm)
                            put("age", 0)
                            // timingAdvance not directly available in CellInfoLte
                        }
                        cellTowersArray.put(cellObj)
                        if (radioType == null) radioType = "lte"
                    }
                }

                is CellInfoGsm -> {
                    val identity = info.cellIdentity
                    if (identity.cid != CellInfo.UNAVAILABLE) {
                        val mcc = identity.mccString?.toIntOrNull() ?: continue
                        val mnc = identity.mncString?.toIntOrNull() ?: continue
                        val cellObj = JSONObject().apply {
                            put("cellId", identity.cid)
                            put(
                                "locationAreaCode",
                                identity.lac.takeIf { it != CellInfo.UNAVAILABLE } ?: continue)
                            put("mobileCountryCode", mcc)
                            put("mobileNetworkCode", mnc)
                            put("signalStrength", info.cellSignalStrength.dbm)
                            put("age", 0)
                        }
                        cellTowersArray.put(cellObj)
                        if (radioType == null) radioType = "gsm"
                    }
                }
                // Add support for other types like WCDMA, CDMA, NR if needed
            }
        }

        if (cellTowersArray.length() == 0) {
            Log.e(TAG, "No valid cell towers found")
            return@withContext null
        }

        // Get home MCC/MNC from operator
        val operator = telephonyManager.networkOperator
        val homeMcc = if (operator.length >= 3) operator.take(3).toIntOrNull() ?: 0 else 0
        val homeMnc = if (operator.length >= 5) operator.substring(3).toIntOrNull() ?: 0 else 0

        val carrier = telephonyManager.networkOperatorName

        // Build request JSON
        val requestJson = JSONObject().apply {
            put("homeMobileCountryCode", homeMcc)
            put("homeMobileNetworkCode", homeMnc)
            if (radioType != null) put("radioType", radioType)
            put("carrier", carrier)
            put("considerIp", true)
            put("cellTowers", cellTowersArray)
        }

        // Make POST request to Google Geolocation API
        try {
            val url =
                URL("https://www.googleapis.com/geolocation/v1/geolocate?key=$GEOLOCATION_API_KEY")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            OutputStreamWriter(conn.outputStream).use { writer ->
                writer.write(requestJson.toString())
                writer.flush()
            }

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val response =
                    BufferedReader(InputStreamReader(conn.inputStream)).use { it.readText() }
                val jsonResponse = JSONObject(response)
                val locJson = jsonResponse.getJSONObject("location")
                val lat = locJson.getDouble("lat")
                val lng = locJson.getDouble("lng")
                val accuracy = jsonResponse.getDouble("accuracy")

                val location = Location("cell_tower")
                location.latitude = lat
                location.longitude = lng
                location.accuracy = accuracy.toFloat()
                location.time = System.currentTimeMillis()
                return@withContext location
            } else {
                Log.e(TAG, "Geolocation API error: ${conn.responseCode} - ${conn.responseMessage}")
                return@withContext null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching location from cell towers", e)
            return@withContext null
        }
    }

    /* ---------------------------------------------------
     * GEOCODER
     * --------------------------------------------------- */

    private suspend fun getAddressFromLocation(location: Location): String? =
        withContext(Dispatchers.IO) {
            try {
                Geocoder(context, Locale.getDefault())
                    .getFromLocation(location.latitude, location.longitude, 1)
                    ?.firstOrNull()
                    ?.getAddressLine(0)
            } catch (e: Exception) {
                null
            }
        }

}
