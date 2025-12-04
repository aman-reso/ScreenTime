//package com.app.screentime.focus.screen
//
//import android.content.Intent
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Pause
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material.icons.filled.Stop
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.hilt.navigation.compose.hiltViewModel
//import kotlinx.coroutines.delay
//import com.app.screentime.focus.FocusModeService
//import com.app.screentime.focus.viewmodel.FocusModeViewModel
//import com.telekom.odsystem.atoms.ODSText
//import com.telekom.odsystem.atoms.ODSTextStyle
//
//import androidx.compose.ui.res.stringResource
//import com.app.screentime.record.repository.formatDuration
//import com.telekom.odsystem.tokens.tokens.ODSTheme
//import com.telekom.odsystem.foundations.HexColor
//import com.telekom.odsystem.DSTextStyles
//import com.telekom.odsystem.atoms.ODSBox
//import com.telekom.odsystem.atoms.ODSColumn
//import com.telekom.odsystem.atoms.ODSRow
//import com.telekom.odsystem.atoms.button.ODSButton
//import com.telekom.odsystem.atoms.button.ODSButtonProps
//import com.telekom.odsystem.atoms.button.ODSButtonVariant
//import com.telekom.odsystem.atoms.icon.ODSIcon
//import com.telekom.odsystem.atoms.icon.ODSIconModel
//import com.telekom.odsystem.foundations.ODSColorModel
//import com.telekom.odsystem.foundations.ODSPadding
//
//@Composable
//fun FocusModeScreen(
//    modifier: Modifier = Modifier,
//    viewModel: FocusModeViewModel = hiltViewModel(),
//    scheme: ODSTheme = neutralScheme
//) {
//
//    val uiState by viewModel.uiState.collectAsState()
//    val context = LocalContext.current
//    var sessionDurationMinutes by remember { mutableIntStateOf(25) }
//
//    LaunchedEffect(Unit) {
//        viewModel.checkServiceStatus(context)
//        sessionDurationMinutes = viewModel.loadSessionDuration(context)
//    }
//
//    // Periodically sync with service
//    LaunchedEffect(uiState.isRunning) {
//        while (uiState.isRunning) {
//            viewModel.checkServiceStatus(context)
//            delay(2000) // Check every 2 seconds
//        }
//    }
//
//    ODSBox(
//        modifier = modifier.fillMaxSize(),
//        background = listOf(ODSColorModel(scheme.basicBackground)),
//        padding = ODSPadding(horizontal = 8.dp)
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                // Title
//                ODSText(
//                    text = "Focus Mode",
//                    style = DSTextStyles.titleS,
//                    color = scheme.basicText,
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            item {
//                // Description
//                ODSText(
//                    text = "Stay focused by only using reading apps. The timer resets if you switch to other apps.",
//                    style = DSTextStyles.bodyMRegular,
//                    color = scheme.basicTextRecessive,
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            item {
//                // Circular Progress Timer
//                ODSBox(
//                    modifier = Modifier.fillMaxWidth(),
//                    padding = ODSPadding(horizontal = 16.dp)
//                ) {
//                    // Gradient background
//                    ODSBox(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(400.dp),
//                        background = listOf(ODSColorModel(
//                            com.telekom.odsystem.foundations.ODSLinearGradientModel(
//                                colors = listOf(
//                                    scheme.basicAccent.copy(alpha = 0.2f),
//                                    scheme.functionalSuccessStandard.copy(alpha = 0.2f)
//                                )
//                            )
//                        )),
//                        cornerRadius = com.telekom.odsystem.foundations.ODSCorners(all = 16.dp)
//                    ) {
//                        ODSColumn(
//                            modifier = Modifier.fillMaxSize(),
//                            padding = ODSPadding(all = 32.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            // Circular Progress Timer
//                            CircularProgressTimer(
//                                elapsedTime = uiState.elapsedTime,
//                                sessionDurationMinutes = sessionDurationMinutes,
//                                isRunning = uiState.isRunning,
//                                modifier = Modifier.size(280.dp)
//                            )
//
//                            Spacer(modifier = Modifier.height(32.dp))
//
//                            // Control Buttons
//                            ODSRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceEvenly,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                // Restart Button
//                                ODSButton(
//                                    scheme = scheme,
//                                    props = ODSButtonProps(
//                                        buttonIcon = ODSIconModel(
//                                            imageVector = Icons.Default.Refresh,
//                                            tint = scheme.basicText,
//                                            contentDescription = "Restart"
//                                        ),
//                                        buttonType = ODSButtonButtonType.ICON_ONLY,
//                                        variant = ODSButtonVariant.GHOST,
//                                        size = com.telekom.odsystem.atoms.button.ODSButtonSize.LARGE
//                                    ),
//                                    onClick = {
//                                        if (uiState.isRunning) {
//                                            val intent = Intent(
//                                                context,
//                                                FocusModeService::class.java
//                                            ).apply {
//                                                action = FocusModeService.ACTION_STOP
//                                            }
//                                            context.stopService(intent)
//                                            viewModel.stopFocusMode(context)
//
//                                            val restartIntent = Intent(
//                                                context,
//                                                FocusModeService::class.java
//                                            ).apply {
//                                                action = FocusModeService.ACTION_START
//                                            }
//                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                context.startForegroundService(restartIntent)
//                                            } else {
//                                                context.startService(restartIntent)
//                                            }
//                                            viewModel.startFocusMode()
//                                        }
//                                    }
//                                )
//
//                                // Play/Pause Button
//                                IconButton(
//                                    onClick = {
//                                        if (uiState.isRunning) {
//                                            val intent = Intent(
//                                                context,
//                                                FocusModeService::class.java
//                                            ).apply {
//                                                action = FocusModeService.ACTION_STOP
//                                            }
//                                            context.stopService(intent)
//                                            viewModel.stopFocusMode(context)
//                                        } else {
//                                            // Start focus mode
//                                            val intent = Intent(
//                                                context,
//                                                FocusModeService::class.java
//                                            ).apply {
//                                                action = FocusModeService.ACTION_START
//                                            }
//                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                                context.startForegroundService(intent)
//                                            } else {
//                                                context.startService(intent)
//                                            }
//                                            viewModel.startFocusMode()
//                                        }
//                                    },
//                                    modifier = Modifier
//                                        .size(64.dp)
//                                        .background(
//                                            color = HexColor(if.value) (uiState.isRunning) colors.error else colors.success,
//                                            shape = MaterialTheme.shapes.medium
//                                        )
//                                ) {
//                                    Icon(
//                                        imageVector = if (uiState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
//                                        contentDescription = if (uiState.isRunning) "Pause" else "Play",
//                                        tint = colors.textOnPrimary,
//                                        modifier = Modifier.size(32.dp)
//                                    )
//                                }
//
//                                // Stop Button
//                                IconButton(
//                                    onClick = {
//                                        // Only stop the service - do not start any service
//                                        val intent =
//                                            Intent(context, FocusModeService::class.java).apply {
//                                                action = FocusModeService.ACTION_STOP
//                                            }
//                                        context.stopService(intent)
//                                        viewModel.stopFocusMode(context)
//                                    },
//                                    modifier = Modifier
//                                        .size(56.dp)
//                                        .background(
//                                            color = HexColor(colors.card.value),
//                                            shape = MaterialTheme.shapes.medium
//                                        )
//                                ) {
//                                    Icon(
//                                        imageVector = Icons.Default.Stop,
//                                        contentDescription = "Stop",
//                                        tint = colors.tint
//                                    )
//                                }
//                            }
//
//                            // Show total day time if available
//                            if (uiState.totalDayTime > 0) {
//                                Spacer(modifier = Modifier.height(24.dp))
//                                ODSText(
//                                    text = "Total Today: ${formatDuration(uiState.totalDayTime)}",
//                                    style = DSTextStyles.bodyMBold,
//                                    color = HexColor(colors.textSecondary.value),
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Session Duration Configuration (only show when not running)
//            if (!uiState.isRunning) {
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = MaterialTheme.shapes.medium,
//                        colors = CardDefaults.cardColors(
//                            containerColor = colors.card.copy(alpha = 0.5f)
//                        )
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp)
//                        ) {
//                            ODSText(
//                                text = stringResource(com.app.screentime.R.string.session_duration),
//                                style = DSTextStyles.bodyMRegular,
//                                color = HexColor(colors.textPrimary.value)
//                            )
//                            Spacer(modifier = Modifier.height(4.dp))
//                            ODSText(
//                                text = stringResource(com.app.screentime.R.string.session_duration_description),
//                                style = DSTextStyles.bodyMBold,
//                                color = HexColor(colors.textSecondary.value)
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//
//                            // Duration display and slider
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                ODSText(
//                                    text = "${sessionDurationMinutes} ${stringResource(com.app.screentime.R.string.minutes)}",
//                                    style = DSTextStyles.subtitle,
//                                    color = HexColor(colors.textPrimary.value)
//                                )
//                            }
//
//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            Slider(
//                                value = sessionDurationMinutes.toFloat(),
//                                onValueChange = { newValue ->
//                                    sessionDurationMinutes = newValue.toInt()
//                                    viewModel.setSessionDuration(context, newValue.toInt())
//                                },
//                                valueRange = 5f..60f,
//                                steps = 10, // Steps of 5 minutes (5, 10, 15, ..., 60)
//                                colors = SliderDefaults.colors(
//                                    thumbColor = colors.success,
//                                    activeTrackColor = colors.success,
//                                    inactiveTrackColor = colors.textSecondary.copy(alpha = 0.3f)
//                                )
//                            )
//
//                            // Quick preset buttons
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.spacedBy(8.dp)
//                            ) {
//                                listOf(15, 25, 30, 45, 60).forEach { minutes ->
//                                    FilterChip(
//                                        selected = sessionDurationMinutes == minutes,
//                                        onClick = {
//                                            sessionDurationMinutes = minutes
//                                            viewModel.setSessionDuration(context, minutes)
//                                        },
//                                        label = {
//                                            ODSText(
//                                                text = "${minutes}m",
//                                                style = DSTextStyles.bodyMBold,
//                                                color = HexColor(if.value) (sessionDurationMinutes == minutes) colors.textPrimary else colors.textSecondary
//                                            )
//                                        },
//                                        colors = FilterChipDefaults.filterChipColors(
//                                            selectedContainerColor = colors.success.copy(alpha = 0.2f),
//                                            containerColor = colors.card
//                                        )
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//
//                item {
//                    Button(
//                        onClick = {
//                            val intent = Intent(context, FocusModeService::class.java).apply {
//                                action = FocusModeService.ACTION_START
//                            }
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                                context.startForegroundService(intent)
//                            } else {
//                                context.startService(intent)
//                            }
//                            viewModel.startFocusMode()
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = colors.success
//                        ),
//                        shape = MaterialTheme.shapes.medium
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PlayArrow,
//                            contentDescription = null,
//                            modifier = Modifier.size(24.dp)
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        ODSText(
//                            text = "Start Session",
//                            style = DSTextStyles.bodyMRegular,
//                            color = HexColor(colors.textOnPrimary.value)
//                        )
//                    }
//                }
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            item {
//                // Info Card
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(MaterialTheme.shapes.medium)
//                        .background(colors.card.copy(alpha = 0.5f))
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        ODSText(
//                            text = "How it works:",
//                            style = DSTextStyles.bodyMRegular,
//                            color = HexColor(colors.textPrimary.value)
//                        )
//                        ODSText(
//                            text = "• Only reading apps are allowed",
//                            style = DSTextStyles.bodyMBold,
//                            color = HexColor(colors.textSecondary.value)
//                        )
//                        ODSText(
//                            text = "• Timer resets if you switch to other apps",
//                            style = DSTextStyles.bodyMBold,
//                            color = HexColor(colors.textSecondary.value)
//                        )
//                        ODSText(
//                            text = "• A notification shows your focus time",
//                            style = DSTextStyles.bodyMBold,
//                            color = HexColor(colors.textSecondary.value)
//                        )
//                    }
//                }
//            }
//
//            // History Section
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//                ODSText(
//                    text = stringResource(com.app.screentime.R.string.focus_history),
//                    style = DSTextStyles.subtitle,
//                    color = HexColor(colors.textPrimary.value)
//                )
//            }
//
//            if (uiState.history.isEmpty()) {
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = MaterialTheme.shapes.medium,
//                        colors = CardDefaults.cardColors(
//                            containerColor = colors.card.copy(alpha = 0.5f)
//                        )
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(32.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            ODSText(
//                                text = stringResource(com.app.screentime.R.string.no_history),
//                                style = DSTextStyles.bodyMRegular,
//                                color = HexColor(colors.textMuted.value),
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }
//            } else {
//                items(uiState.history) { session ->
//                    FocusHistoryItem(session = session)
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//private fun FocusHistoryItem(session: com.app.screentime.focus.viewmodel.FocusSession) {
//
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = MaterialTheme.shapes.medium,
//        colors = CardDefaults.cardColors(
//            containerColor = colors.card
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                ODSText(
//                    text = formatDate(session.startTime),
//                    style = DSTextStyles.bodyMRegular,
//                    color = HexColor(colors.textPrimary.value)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                ODSText(
//                    text = "${formatDuration(session.duration)} • ${if (session.completed) "Completed" else "Incomplete"}",
//                    style = DSTextStyles.bodyMBold,
//                    color = HexColor(colors.textSecondary.value)
//                )
//            }
//            if (session.completed) {
//                Icon(
//                    imageVector = Icons.Default.CheckCircle,
//                    contentDescription = null,
//                    tint = colors.success,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//    }
//}
//
//private fun formatDate(timestamp: Long): String {
//    val date = java.util.Date(timestamp)
//    val format =
//        java.text.SimpleDateFormat("MMM dd, yyyy • HH:mm", java.util.Locale.getDefault())
//    return format.format(date)
//}
//
///**
// * Formats duration in milliseconds to MM:SS format
// * Example: 1500000ms -> "25:00", 1499000ms -> "24:59"
// */
//private fun formatDurationMMSS(ms: Long): String {
//    val totalSeconds = (ms / 1000).coerceAtLeast(0)
//    val minutes = (totalSeconds / 60).toInt()
//    val seconds = (totalSeconds % 60).toInt()
//    return String.format("%02d:%02d", minutes, seconds)
//}
//
//@Composable
//private fun CircularProgressTimer(
//    elapsedTime: Long,
//    sessionDurationMinutes: Int,
//    isRunning: Boolean,
//    modifier: Modifier = Modifier
//) {
//
//
//    // Calculate session duration in milliseconds
//    val sessionDurationMs = sessionDurationMinutes * 60 * 1000L
//
//    // Calculate remaining time
//    val remainingTime = maxOf(0L, sessionDurationMs - elapsedTime)
//
//    // Calculate progress for circular progress bar (0 to 1, where 1 is complete)
//    val progress = if (sessionDurationMs > 0) {
//        (elapsedTime.toFloat() / sessionDurationMs.toFloat()).coerceIn(0f, 1f)
//    } else {
//        0f
//    }
//
//    // Animate the progress smoothly
//    val animatedProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(durationMillis = 500),
//        label = "circular_progress"
//    )
//
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val centerX = size.width / 2f
//            val centerY = size.height / 2f
//            val radius = (size.minDimension / 2f) * 0.85f
//            val strokeWidth = size.minDimension * 0.12f
//
//            // Draw background circle (unfilled portion)
//            drawCircle(
//                color = HexColor(colors.border.value),
//                radius = radius,
//                center = Offset(centerX, centerY),
//                style = Stroke(width = strokeWidth)
//            )
//
//            // Draw progress arc (filled portion) - green, clockwise from top
//            if (isRunning && sessionDurationMs > 0) {
//                // Show progress as elapsed time (clockwise from top)
//                val sweepAngle = 360f * animatedProgress
//                drawArc(
//                    color = HexColor(colors.success.value),
//                    startAngle = -90f, // Start from top
//                    sweepAngle = sweepAngle,
//                    useCenter = false,
//                    topLeft = Offset(centerX - radius, centerY - radius),
//                    size = Size(radius * 2f, radius * 2f),
//                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                )
//            }
//        }
//
//        // Time display in center
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // Main time display (countdown format when running, or initial duration when not)
//            if (isRunning && sessionDurationMs > 0) {
//                if (remainingTime <= 0) {
//                    // Show complete icon when countdown reaches 00:00
//                    Icon(
//                        imageVector = Icons.Default.CheckCircle,
//                        contentDescription = "Session Complete",
//                        modifier = Modifier.size(80.dp),
//                        tint = colors.success
//                    )
//                } else {
//                    // Show countdown in MM:SS format
//                    ODSText(
//                        text = formatDurationMMSS(remainingTime),
//                        style = DSTextStyles.titleS,
//                        color = HexColor(colors.textPrimary.value),
//                        textAlign = TextAlign.Center
//                    )
//                }
//            } else {
//                // Show initial duration when not running
//                ODSText(
//                    text = formatDurationMMSS(sessionDurationMs),
//                    style = DSTextStyles.titleS,
//                    color = HexColor(colors.textSecondary.value),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            // Session Time label
//            if (isRunning) {
//                Spacer(modifier = Modifier.height(8.dp))
//                ODSText(
//                    text = if (remainingTime <= 0) "Session Complete" else "Remaining Time",
//                    style = DSTextStyles.bodyMBold,
//                    color = HexColor(colors.textSecondary.value),
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}
//
//
