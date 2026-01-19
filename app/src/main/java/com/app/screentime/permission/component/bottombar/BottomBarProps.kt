package com.app.screentime.permission.component.bottombar

import android.content.Context
import com.app.screentime.config.R

/**
 * Properties used to configure the BottomBar component.
 *
 * @property buttonLabel The label text for the primary button.
 * @property showLegalLinks Indicates whether legal links should be displayed.
 * @property privacyPolicyUrl URL for the privacy policy link.
 * @property termsOfServiceUrl URL for the terms of service link.
 */
data class BottomBarProps(
    var buttonLabel: String = "",
    var showLegalLinks: Boolean = true,
    var privacyPolicyUrl: String = "https://aman-reso.github.io/AppTime-HTML/privacy-policy.html",
    var termsOfServiceUrl: String = "https://aman-reso.github.io/AppTime-HTML/terms-and-conditions.html"
) {
    companion object {
        fun default(context: Context): BottomBarProps {
            return BottomBarProps(
                buttonLabel = context.getString(R.string.allow_access)
            )
        }
    }
}

