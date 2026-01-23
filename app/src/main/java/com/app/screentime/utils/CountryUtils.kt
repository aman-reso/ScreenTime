package com.app.screentime.utils

import android.content.Context
import android.telephony.TelephonyManager
import java.util.Locale

object CountryUtils {
    fun isUserInIndia(context: Context): Boolean {
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

            // 1. Check Network Country ISO
            val networkCountryIso = telephonyManager?.networkCountryIso
            if (!networkCountryIso.isNullOrEmpty() && networkCountryIso.equals(
                    "in",
                    ignoreCase = true
                )
            ) {
                return true
            }

            val simCountryIso = telephonyManager?.simCountryIso
            if (!simCountryIso.isNullOrEmpty() && simCountryIso.equals("in", ignoreCase = true)) {
                return true
            }

            val locale = Locale.getDefault()
            return locale.country.equals("IN", ignoreCase = true)
        } catch (e: Throwable) {
            return false
        }
    }
}
