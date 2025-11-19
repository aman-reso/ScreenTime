package com.app.screentime.profile.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.app.screentime.preferences.PreferencesManager
import com.app.screentime.security.TOTP
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.ui.theme.appColor
import kotlinx.coroutines.delay
import java.time.Instant

private const val TIME_STEP_SECONDS = 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTOTPBottomSheetContent(
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    var otp by remember { mutableStateOf("------") }
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var progress by remember { mutableFloatStateOf(1f) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "TOTP Countdown"
    )

    // Get TOTP secret from preferences
    val preferencesManager = remember { PreferencesManager(context) }
    val totpSecret = remember { preferencesManager.getTOTPSecret() }

    LaunchedEffect(Unit) {
        while (true) {
            val epochSeconds = Instant.now().epochSecond
            val remaining = (60 - (epochSeconds % 60)).toInt()
            remainingSeconds = remaining
            otp = TOTP.generateTOTP(totpSecret)

            for (i in remaining downTo 0) {
                remainingSeconds = i
                progress = i.toFloat() / 60
                delay(1000)
            }
        }
    }

    ModalBottomSheet(
        containerColor = colors.background,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassBottomSheetBackground()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "One-Time Password",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.tint
                    )
                }
            }
            AppText(
                text = "This code allows you to securely share your app usage with someone you trust. It regenerates every 60 seconds and can only be used for this purpose.",
                style = AppTextStyle.Label,
                color = colors.textMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                ) {
                    // Circular progress
                    CircularProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .matchParentSize(),
                        color = colors.success,
                        strokeWidth = 3.dp,
                        trackColor = colors.textMuted.copy(alpha = 0.3f)
                    )

                    AppText(
                        text = "$remainingSeconds s",
                        style = AppTextStyle.Label,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    AppText(
                        text = otp,
                        style = AppTextStyle.Title,
                        fontWeight = FontWeight.Bold,
                        color = colors.success,
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AppText(
                        text = "This code regenerates every 60 seconds",
                        style = AppTextStyle.Label,
                        color = colors.textMuted
                    )
                }
            }
        }
    }
}


