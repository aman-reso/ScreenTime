package com.app.screentime.core.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    fun isLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return false
        return LocationManagerCompat.isLocationEnabled(locationManager)
    }

    fun promptEnableLocationService(
        context: Context,
        onResolutionRequired: (ResolvableApiException) -> Unit,
        onAlreadyEnabled: () -> Unit = {},
        onFailed: (Exception) -> Unit = {}
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val client = LocationServices.getSettingsClient(context)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                onAlreadyEnabled()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    onResolutionRequired(exception)
                } else {
                    onFailed(exception)
                }
            }
    }

    fun requestEnableLocationService(
        context: Context,
        settingsLauncher: ActivityResultLauncher<IntentSenderRequest>,
        onAlreadyEnabled: () -> Unit = {},
        onFailed: () -> Unit = {}
    ) {
        promptEnableLocationService(
            context = context,
            onResolutionRequired = { resolvableApiException ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(
                        resolvableApiException.resolution.intentSender
                    ).build()
                    settingsLauncher.launch(intentSenderRequest)
                } catch (e: Exception) {
                    openLocationSettings(context)
                }
            },
            onAlreadyEnabled = onAlreadyEnabled,
            onFailed = {
                onFailed()
            }
        )
    }

    fun openAppSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            context.showODSToast("Please allow Location permission in Settings")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openLocationSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            context.showODSToast("Please turn ON Location (GPS) service")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun performLocationAction(
        context: Context,
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        settingsLauncher: ActivityResultLauncher<IntentSenderRequest>,
        onResult: (latitude: Double?, longitude: Double?, cityName: String?) -> Unit
    ) {
        if (!hasLocationPermission(context)) {
            // Permission not allowed -> Ask for runtime location permission
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // Permission already allowed -> Check / prompt enable location service via native Google Play dialog!
            requestEnableLocationService(
                context = context,
                settingsLauncher = settingsLauncher,
                onAlreadyEnabled = {
                    fetchCurrentLocation(context, onResult)
                },
                onFailed = {
                    openLocationSettings(context)
                }
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation(
        context: Context,
        onResult: (latitude: Double?, longitude: Double?, cityName: String?) -> Unit
    ) {
        if (!hasLocationPermission(context) || !isLocationServiceEnabled(context)) {
            onResult(null, null, null)
            return
        }

        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    processLocation(context, location.latitude, location.longitude, onResult)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                        if (lastLoc != null) {
                            processLocation(context, lastLoc.latitude, lastLoc.longitude, onResult)
                        } else {
                            onResult(null, null, null)
                        }
                    }.addOnFailureListener {
                        onResult(null, null, null)
                    }
                }
            }.addOnFailureListener {
                onResult(null, null, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null, null, null)
        }
    }

    private fun processLocation(
        context: Context,
        lat: Double,
        lng: Double,
        onResult: (latitude: Double?, longitude: Double?, cityName: String?) -> Unit
    ) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(lat, lng, 1) { addresses ->
                    val address = addresses.firstOrNull()
                    val cityName = formatAddress(address) ?: "${String.format(Locale.US, "%.2f", lat)}, ${String.format(Locale.US, "%.2f", lng)}"
                    onResult(lat, lng, cityName)
                }
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                val address = addresses?.firstOrNull()
                val cityName = formatAddress(address) ?: "${String.format(Locale.US, "%.2f", lat)}, ${String.format(Locale.US, "%.2f", lng)}"
                onResult(lat, lng, cityName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(lat, lng, "${String.format(Locale.US, "%.2f", lat)}, ${String.format(Locale.US, "%.2f", lng)}")
        }
    }

    private fun formatAddress(address: Address?): String? {
        if (address == null) return null
        val city = address.locality ?: address.subAdminArea ?: address.adminArea
        val stateOrCountry = address.adminArea ?: address.countryName
        return when {
            city != null && stateOrCountry != null && city != stateOrCountry -> "$city, $stateOrCountry"
            city != null -> city
            stateOrCountry != null -> stateOrCountry
            else -> null
        }
    }
}
