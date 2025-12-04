package com.telekom.odsystem.componenttokens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

/**
 * Created by dmarinopoulos on 3/4/24
 */

data class ODSDimTokens(
    var verticalAlignment: Alignment.Vertical,
    var horizontalAlignment: Alignment.Horizontal,
    var horizontalArrangement: Arrangement.Horizontal
)

var defaultODSDimTokens = ODSDimTokens(
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    horizontalArrangement = Arrangement.Center
)

var DSDimTokens: ODSDimTokens = defaultODSDimTokens
