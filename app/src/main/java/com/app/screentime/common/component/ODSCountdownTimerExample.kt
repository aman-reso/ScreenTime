package com.app.screentime.common.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.neutralScheme

/**
 * Example usage of ODSCountdownTimer
 * 
 * This file demonstrates how to use the ODSCountdownTimer component
 * in different scenarios.
 */
@Composable
fun ODSCountdownTimerExample() {
    var isRunning by remember { mutableStateOf(true) }
    
    // Use the countdown timer hook
    val (remainingSeconds, resetCountdown) = useCountdownTimer(
        totalSeconds = 60,
        isRunning = isRunning,
        onComplete = {
            // Handle countdown completion
            isRunning = false
        }
    )
    
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent4
    ) {
        // Example 1: Circular countdown timer with MM:SS format
        ODSCountdownTimer(
            modifier = Modifier.fillMaxWidth(),
            scheme = neutralScheme,
            props = ODSCountdownTimerProps(
                totalSeconds = 60,
                format = ODSCountdownTimerFormat.MM_SS,
                style = ODSCountdownTimerStyle.CIRCULAR,
                label = "Session Timeout"
            ),
            isRunning = isRunning,
            remainingSeconds = remainingSeconds
        )
        
        // Example 2: Linear countdown timer
        ODSCountdownTimer(
            modifier = Modifier.fillMaxWidth(),
            scheme = neutralScheme,
            props = ODSCountdownTimerProps(
                totalSeconds = 60,
                format = ODSCountdownTimerFormat.MM_SS,
                style = ODSCountdownTimerStyle.LINEAR,
                label = "Processing"
            ),
            isRunning = isRunning,
            remainingSeconds = remainingSeconds
        )
        
        // Example 3: Text-only countdown
        ODSCountdownTimer(
            modifier = Modifier.fillMaxWidth(),
            scheme = neutralScheme,
            props = ODSCountdownTimerProps(
                totalSeconds = 60,
                format = ODSCountdownTimerFormat.SECONDS_ONLY,
                style = ODSCountdownTimerStyle.TEXT_ONLY,
                label = "Remaining Time"
            ),
            isRunning = isRunning,
            remainingSeconds = remainingSeconds
        )
        
        // Control buttons
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = DSVariables.spacingComponent2
        ) {
            ODSButton(
                scheme = neutralScheme,
                props = ODSButtonProps(
                    label = if (isRunning) "Pause" else "Resume",
                    variant = ODSButtonVariant.PRIMARY
                ),
                onClick = { isRunning = !isRunning }
            )
            
            ODSButton(
                scheme = neutralScheme,
                props = ODSButtonProps(
                    label = "Reset",
                    variant = ODSButtonVariant.OUTLINE
                ),
                onClick = {
                    resetCountdown()
                    isRunning = true
                }
            )
        }
    }
}

