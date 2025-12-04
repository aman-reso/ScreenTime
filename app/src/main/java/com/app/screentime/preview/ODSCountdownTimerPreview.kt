package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.screentime.common.component.*
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme

@Preview(showBackground = true)
@Composable
fun ODSCountdownTimerPreview() {
    var isRunning by remember { mutableStateOf(true) }
    val (remainingSeconds1, reset1) = useCountdownTimer(
        totalSeconds = 60,
        isRunning = isRunning,
        onComplete = { isRunning = false }
    )
    val (remainingSeconds2, reset2) = useCountdownTimer(
        totalSeconds = 120,
        isRunning = isRunning,
        onComplete = { }
    )
    val (remainingSeconds3, reset3) = useCountdownTimer(
        totalSeconds = 30,
        isRunning = isRunning,
        onComplete = { }
    )
    val (remainingSeconds4, reset4) = useCountdownTimer(
        totalSeconds = 90,
        isRunning = isRunning,
        onComplete = { }
    )
    val (remainingSeconds5, reset5) = useCountdownTimer(
        totalSeconds = 45,
        isRunning = isRunning,
        onComplete = { }
    )
    val (remainingSeconds6, reset6) = useCountdownTimer(
        totalSeconds = 15,
        isRunning = isRunning,
        onComplete = { }
    )
    
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent5
        ) {
            // Circular Style Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                // Circular - MM:SS format
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 60,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        label = "Circular Timer (MM:SS)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds1
                )
                
                // Circular - Seconds only
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 60,
                        format = ODSCountdownTimerFormat.SECONDS_ONLY,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        label = "Circular Timer (Seconds Only)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds2
                )
                
                // Circular - HH:MM:SS format
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 3661, // 1 hour, 1 minute, 1 second
                        format = ODSCountdownTimerFormat.HH_MM_SS,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        label = "Circular Timer (HH:MM:SS)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds2 + 3541
                )
                
                // Circular - Custom size
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 30,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        circularSize = 80.dp,
                        circularStrokeWidth = 5.dp,
                        label = "Circular Timer (Large)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds3
                )
                
                // Circular - Without progress
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 60,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.CIRCULAR,
                        showProgress = false,
                        label = "Circular Timer (No Progress)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds1
                )
            }
            
            // Linear Style Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                // Linear - MM:SS format
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 90,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.LINEAR,
                        label = "Linear Timer (MM:SS)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds4
                )
                
                // Linear - Seconds only
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 60,
                        format = ODSCountdownTimerFormat.SECONDS_ONLY,
                        style = ODSCountdownTimerStyle.LINEAR,
                        label = "Linear Timer (Seconds Only)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds1
                )
                
                // Linear - Without progress
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 60,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.LINEAR,
                        showProgress = false,
                        label = "Linear Timer (No Progress)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds1
                )
            }
            
            // Text Only Style Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent4
            ) {
                // Text Only - MM:SS format
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 45,
                        format = ODSCountdownTimerFormat.MM_SS,
                        style = ODSCountdownTimerStyle.TEXT_ONLY,
                        label = "Text Only Timer (MM:SS)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds5
                )
                
                // Text Only - Seconds only
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 15,
                        format = ODSCountdownTimerFormat.SECONDS_ONLY,
                        style = ODSCountdownTimerStyle.TEXT_ONLY,
                        label = "Text Only Timer (Seconds Only)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds6
                )
                
                // Text Only - HH:MM:SS format
                ODSCountdownTimer(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCountdownTimerProps(
                        totalSeconds = 3661,
                        format = ODSCountdownTimerFormat.HH_MM_SS,
                        style = ODSCountdownTimerStyle.TEXT_ONLY,
                        label = "Text Only Timer (HH:MM:SS)"
                    ),
                    isRunning = isRunning,
                    remainingSeconds = remainingSeconds2 + 3541
                )
            }
        }
    }
}

