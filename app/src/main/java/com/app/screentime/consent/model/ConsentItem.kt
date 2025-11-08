package com.app.screentime.consent.model

/**
 * Data class representing a single consent item
 * @param title The title of the consent item
 * @param subtitle The subtitle/description of the consent item
 * @param isMandatory Whether this consent is mandatory (if true, value will always be enabled)
 * @param value The default value for this consent item
 */
data class ConsentItem(
    val title: String,
    val subtitle: String,
    val isMandatory: Boolean,
    val value: Boolean = false
) {
    /**
     * Get the effective value - if mandatory, always returns true
     */
    fun getEffectiveValue(): Boolean {
        return if (isMandatory) true else value
    }
}

