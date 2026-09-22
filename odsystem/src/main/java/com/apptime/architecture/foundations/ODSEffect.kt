package com.telekom.odsystem.foundations

import kotlinx.serialization.Serializable

@Serializable
data class ODSEffect(
    val elevations: List<ODSElevation>,
)
