package com.app.screentime.applock.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.screentime.applock.composelock.Dot
import com.app.screentime.applock.composelock.LockCallback
import com.app.screentime.applock.composelock.PatternLock
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Pattern lock view component that wraps PatternLock and provides callbacks
 */
@androidx.compose.ui.ExperimentalComposeUiApi
@Composable
fun PatternLockView(
    modifier: Modifier = Modifier,
    scheme: ODSTheme,
    onPatternComplete: (String) -> Unit,
    errorMessage: String? = null
) {
    var patternString by remember { mutableStateOf<String?>(null) }
    
    PatternLock(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(DSVariables.spacingComponent4),
        dimension = 3, // 3x3 grid
        sensitivity = 50f,
        dotsColor = if (errorMessage != null) {
            scheme.functionalDestructiveStandard.getColor()
        } else {
            scheme.basicText.getColor()
        },
        dotsSize = 20f,
        linesColor = if (errorMessage != null) {
            scheme.functionalDestructiveStandard.getColor()
        } else {
            scheme.functionalSuccessStandard.getColor()
        },
        linesStroke = 8f,
        animationDuration = 200,
        animationDelay = 100,
        callback = object : LockCallback {
            override fun onStart(dot: Dot) {
                // Pattern started
            }
            
            override fun onDotConnected(dot: Dot) {
                // Dot connected
            }
            
            override fun onResult(result: List<Dot>) {
                // Convert dots to pattern string (comma-separated dot IDs)
                if (result.size >= 4) { // Minimum 4 dots for pattern
                    patternString = result.joinToString(",") { it.id.toString() }
                    onPatternComplete(patternString!!)
                    patternString = null
                } else {
                    // Pattern too short, show error
                    patternString = null
                }
            }
        }
    )
}

