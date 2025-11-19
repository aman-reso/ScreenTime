package com.app.screentime.focus.screen

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import com.app.screentime.focus.FocusModeService
import com.app.screentime.focus.viewmodel.FocusModeViewModel
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import androidx.compose.ui.res.stringResource
import com.app.screentime.record.repository.formatDuration

@Composable
fun FocusModeScreen(
    modifier: Modifier = Modifier,
    viewModel: FocusModeViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var sessionDurationMinutes by remember { mutableIntStateOf(25) }

    LaunchedEffect(Unit) {
        viewModel.checkServiceStatus(context)
        sessionDurationMinutes = viewModel.loadSessionDuration(context)
    }

    // Periodically sync with service
    LaunchedEffect(uiState.isRunning) {
        while (uiState.isRunning) {
            viewModel.checkServiceStatus(context)
            delay(2000) // Check every 2 seconds
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Title
                AppText(
                    text = "Focus Mode",
                    style = AppTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
            }

            item {
                // Description
                AppText(
                    text = "Stay focused by only using reading apps. The timer resets if you switch to other apps.",
                    style = AppTextStyle.Body,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Circular Progress Timer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Gradient background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        colors.accent.copy(alpha = 0.2f), // Light accent
                                        colors.success.copy(alpha = 0.2f) // Light success
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Circular Progress Timer
                            CircularProgressTimer(
                                elapsedTime = uiState.elapsedTime,
                                sessionDurationMinutes = sessionDurationMinutes,
                                isRunning = uiState.isRunning,
                                modifier = Modifier.size(280.dp)
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            // Control Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Restart Button
                                IconButton(
                                    onClick = {
                                        if (uiState.isRunning) {
                                            val intent = Intent(
                                                context,
                                                FocusModeService::class.java
                                            ).apply {
                                                action = FocusModeService.ACTION_STOP
                                            }
                                            context.stopService(intent)
                                            viewModel.stopFocusMode(context)

                                            val restartIntent = Intent(
                                                context,
                                                FocusModeService::class.java
                                            ).apply {
                                                action = FocusModeService.ACTION_START
                                            }
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                context.startForegroundService(restartIntent)
                                            } else {
                                                context.startService(restartIntent)
                                            }
                                            viewModel.startFocusMode()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = colors.card,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Restart",
                                        tint = colors.tint
                                    )
                                }

                                // Play/Pause Button
                                IconButton(
                                    onClick = {
                                        if (uiState.isRunning) {
                                            val intent = Intent(
                                                context,
                                                FocusModeService::class.java
                                            ).apply {
                                                action = FocusModeService.ACTION_STOP
                                            }
                                            context.stopService(intent)
                                            viewModel.stopFocusMode(context)
                                        } else {
                                            // Start focus mode
                                            val intent = Intent(
                                                context,
                                                FocusModeService::class.java
                                            ).apply {
                                                action = FocusModeService.ACTION_START
                                            }
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                context.startForegroundService(intent)
                                            } else {
                                                context.startService(intent)
                                            }
                                            viewModel.startFocusMode()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(
                                            color = if (uiState.isRunning) colors.error else colors.success,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (uiState.isRunning) "Pause" else "Play",
                                        tint = colors.textOnPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                // Stop Button
                                IconButton(
                                    onClick = {
                                        // Only stop the service - do not start any service
                                        val intent =
                                            Intent(context, FocusModeService::class.java).apply {
                                                action = FocusModeService.ACTION_STOP
                                            }
                                        context.stopService(intent)
                                        viewModel.stopFocusMode(context)
                                    },
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = colors.card,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Stop,
                                        contentDescription = "Stop",
                                        tint = colors.tint
                                    )
                                }
                            }

                            // Show total day time if available
                            if (uiState.totalDayTime > 0) {
                                Spacer(modifier = Modifier.height(24.dp))
                                AppText(
                                    text = "Total Today: ${formatDuration(uiState.totalDayTime)}",
                                    style = AppTextStyle.Label,
                                    color = colors.textSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Session Duration Configuration (only show when not running)
            if (!uiState.isRunning) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.card.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            AppText(
                                text = stringResource(com.app.screentime.R.string.session_duration),
                                style = AppTextStyle.Body,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = stringResource(com.app.screentime.R.string.session_duration_description),
                                style = AppTextStyle.Label,
                                color = colors.textSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Duration display and slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AppText(
                                    text = "${sessionDurationMinutes} ${stringResource(com.app.screentime.R.string.minutes)}",
                                    style = AppTextStyle.SubTitle,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Slider(
                                value = sessionDurationMinutes.toFloat(),
                                onValueChange = { newValue ->
                                    sessionDurationMinutes = newValue.toInt()
                                    viewModel.setSessionDuration(context, newValue.toInt())
                                },
                                valueRange = 5f..60f,
                                steps = 10, // Steps of 5 minutes (5, 10, 15, ..., 60)
                                colors = SliderDefaults.colors(
                                    thumbColor = colors.success,
                                    activeTrackColor = colors.success,
                                    inactiveTrackColor = colors.textSecondary.copy(alpha = 0.3f)
                                )
                            )

                            // Quick preset buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(15, 25, 30, 45, 60).forEach { minutes ->
                                    FilterChip(
                                        selected = sessionDurationMinutes == minutes,
                                        onClick = {
                                            sessionDurationMinutes = minutes
                                            viewModel.setSessionDuration(context, minutes)
                                        },
                                        label = {
                                            AppText(
                                                text = "${minutes}m",
                                                style = AppTextStyle.Label,
                                                color = if (sessionDurationMinutes == minutes) colors.textPrimary else colors.textSecondary
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = colors.success.copy(alpha = 0.2f),
                                            containerColor = colors.card
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Button(
                        onClick = {
                            val intent = Intent(context, FocusModeService::class.java).apply {
                                action = FocusModeService.ACTION_START
                            }
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                context.startForegroundService(intent)
                            } else {
                                context.startService(intent)
                            }
                            viewModel.startFocusMode()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.success
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AppText(
                            text = "Start Session",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = colors.textOnPrimary
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colors.card.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppText(
                            text = "How it works:",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        AppText(
                            text = "• Only reading apps are allowed",
                            style = AppTextStyle.Label,
                            color = colors.textSecondary
                        )
                        AppText(
                            text = "• Timer resets if you switch to other apps",
                            style = AppTextStyle.Label,
                            color = colors.textSecondary
                        )
                        AppText(
                            text = "• A notification shows your focus time",
                            style = AppTextStyle.Label,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            // History Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AppText(
                    text = stringResource(com.app.screentime.R.string.focus_history),
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }

            if (uiState.history.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colors.card.copy(alpha = 0.5f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AppText(
                                text = stringResource(com.app.screentime.R.string.no_history),
                                style = AppTextStyle.Body,
                                color = colors.textMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(uiState.history) { session ->
                    FocusHistoryItem(session = session)
                }
            }
        }
    }
}


@Composable
private fun FocusHistoryItem(session: com.app.screentime.focus.viewmodel.FocusSession) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = formatDate(session.startTime),
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppText(
                    text = "${formatDuration(session.duration)} • ${if (session.completed) "Completed" else "Incomplete"}",
                    style = AppTextStyle.Label,
                    color = colors.textSecondary
                )
            }
            if (session.completed) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = colors.success,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format =
        java.text.SimpleDateFormat("MMM dd, yyyy • HH:mm", java.util.Locale.getDefault())
    return format.format(date)
}

/**
 * Formats duration in milliseconds to MM:SS format
 * Example: 1500000ms -> "25:00", 1499000ms -> "24:59"
 */
private fun formatDurationMMSS(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = (totalSeconds / 60).toInt()
    val seconds = (totalSeconds % 60).toInt()
    return String.format("%02d:%02d", minutes, seconds)
}

@Composable
private fun CircularProgressTimer(
    elapsedTime: Long,
    sessionDurationMinutes: Int,
    isRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current ?: return

    // Calculate session duration in milliseconds
    val sessionDurationMs = sessionDurationMinutes * 60 * 1000L

    // Calculate remaining time
    val remainingTime = maxOf(0L, sessionDurationMs - elapsedTime)

    // Calculate progress for circular progress bar (0 to 1, where 1 is complete)
    val progress = if (sessionDurationMs > 0) {
        (elapsedTime.toFloat() / sessionDurationMs.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    // Animate the progress smoothly
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "circular_progress"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = (size.minDimension / 2f) * 0.85f
            val strokeWidth = size.minDimension * 0.12f

            // Draw background circle (unfilled portion)
            drawCircle(
                color = colors.border,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidth)
            )

            // Draw progress arc (filled portion) - green, clockwise from top
            if (isRunning && sessionDurationMs > 0) {
                // Show progress as elapsed time (clockwise from top)
                val sweepAngle = 360f * animatedProgress
                drawArc(
                    color = colors.success,
                    startAngle = -90f, // Start from top
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerX - radius, centerY - radius),
                    size = Size(radius * 2f, radius * 2f),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Time display in center
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main time display (countdown format when running, or initial duration when not)
            if (isRunning && sessionDurationMs > 0) {
                if (remainingTime <= 0) {
                    // Show complete icon when countdown reaches 00:00
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Session Complete",
                        modifier = Modifier.size(80.dp),
                        tint = colors.success
                    )
                } else {
                    // Show countdown in MM:SS format
                    AppText(
                        text = formatDurationMMSS(remainingTime),
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Show initial duration when not running
                AppText(
                    text = formatDurationMMSS(sessionDurationMs),
                    style = AppTextStyle.Title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Session Time label
            if (isRunning) {
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = if (remainingTime <= 0) "Session Complete" else "Remaining Time",
                    style = AppTextStyle.Label,
                    fontSize = 14.sp,
                    color = colors.textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


