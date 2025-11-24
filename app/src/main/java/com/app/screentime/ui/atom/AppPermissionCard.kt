package com.app.screentime.ui.atom

import android.app.AppOpsManager
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.app.screentime.R
import com.app.screentime.ui.atom.AppPrimaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import kotlinx.coroutines.delay

/**
 * App Permission Card - Complete permission request screen
 * Redesigned to match the HTML/CSS design
 */
@Composable
fun AppPermissionCard(
    modifier: Modifier = Modifier,
    onAllPermissionsGranted: () -> Unit = {}
) {
    val context = LocalContext.current

    // Permission state
    var hasUsageStatsPermission by remember { mutableStateOf(false) }

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Usage stats permission launcher
    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Handle Allow button click
    val handleAllowClick = {
        if (!hasUsageStatsPermission) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.data = "package:${context.packageName}".toUri()
            usageStatsPermissionLauncher.launch(intent)
        } else {
            onAllPermissionsGranted()
        }
    }

    val primaryColor = Color(0xFFD0BCFF) // Light purple
    val onPrimaryColor = Color(0xFF381E72) // Dark purple
    val surfaceVariant = Color(0xFF49454F) // Dark grey
    val onSurfaceVariant = Color(0xFFCAC4D0) // Light grey
    val onSurface = Color(0xFFE6E1E5) // White/light

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp)
                    .padding(top = 40.dp, bottom = 140.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero Section
                HeroSection(
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Info Cards
                InfoCard(
                    title = "Why We Need App Usage Data",
                    description = "To provide you with accurate insights into your digital habits, ScreenTime requires access to your app usage statistics. This permission allows us to track which applications you use and how much time you spend on each one.",
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoCard(
                    title = "Why This Permission is Essential",
                    description = "Without this permission, we cannot accurately measure your screen time, identify your most-used apps, or provide meaningful insights about your digital wellness. This data is the foundation of all features in ScreenTime.",
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoCard(
                    title = "What We Track",
                    description = "We only track essential information: app names, usage duration, and timestamps. We do NOT access your personal data, messages, passwords, or any sensitive information within apps. Your privacy is our top priority.",
                )

                Spacer(modifier = Modifier.height(16.dp))

                HighlightInfoCard(
                    title = "Your Privacy is Protected",
                    description = "All usage data is stored locally on your device and is only used to generate your personal screen time reports. We never share your data with third parties.",
                    delay = 0.5f,
                    primaryColor = primaryColor
                )
            }

            BottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                onAllowClick = handleAllowClick,
                primaryColor = primaryColor,
                onPrimaryColor = onPrimaryColor,
                onSurfaceVariant = onSurfaceVariant,
            )
        }
    }
}


@Composable
private fun HeroSection(primaryColor: Color) {
    // Shield icon with diamond background
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        // Diamond background (rotated square)
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .rotate(45f)
                .scale(scale)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF0080), // #FF0080
                            Color(0xFF7928CA), // #7928CA
                            Color(0xFFFF0080) // #FF0080
                        )
                    ),
                    shape = RoundedCornerShape(40.dp)
                ),
            contentAlignment = Alignment.Center
        ) {}


        // Shield icon with plus
        val floatTransition = rememberInfiniteTransition(label = "float")
        val floatY by floatTransition.animateFloat(
            initialValue = 0f,
            targetValue = -10f,
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "float"
        )

        Column(
            modifier = Modifier
                .graphicsLayer {
                    translationY = floatY
                }
                .size(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = primaryColor
            )
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = primaryColor
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Title with gradient
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
            text = "App Usage Permission",
            style = AppTextStyle.Title,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    description: String,
) {
    val surfaceVariant = Color(0xFF49454F)
    val onSurface = Color(0xFFE6E1E5)
    val onSurfaceVariant = Color(0xFFCAC4D0)
    val glassBorder = Color(0x1AFFFFFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = surfaceVariant
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, glassBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            AppText(
                text = title,
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Medium,
                color = onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppText(
                text = description,
                style = AppTextStyle.Label,
                color = onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HighlightInfoCard(
    title: String,
    description: String,
    delay: Float,
    primaryColor: Color
) {
    val onSurface = Color(0xFFE6E1E5)
    val onSurfaceVariant = Color(0xFFCAC4D0)
    val primaryContainer = Color(0xFF4F378B)
    val onPrimaryContainer = Color(0xFFEADDFF)

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = primaryContainer
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = primaryColor
            )
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppText(
                    text = description,
                    style = AppTextStyle.Label,
                    color = onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    onAllowClick: () -> Unit,
    primaryColor: Color,
    onPrimaryColor: Color,
    onSurfaceVariant: Color,
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Allow Access Button
            AppPrimaryButton(
                onClick = onAllowClick,
                modifier = Modifier.fillMaxWidth(),
                text = "Allow Access"
            )

            // Legal Links
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://aman-reso.github.io/AppTime-HTML/privacy-policy.html".toUri()
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.util.Log.e(
                                "AppPermissionCard",
                                "Error opening Privacy Policy",
                                e
                            )
                        }
                    }
                ) {
                    AppText(
                        text = "Privacy Policy",
                        style = AppTextStyle.Label
                    )
                }

                AppText(
                    text = "•",
                    style = AppTextStyle.Label,
                    color = onSurfaceVariant
                )

                TextButton(
                    onClick = {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://aman-reso.github.io/AppTime-HTML/terms-and-conditions.html".toUri()
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            android.util.Log.e("AppPermissionCard", "Error opening Terms", e)
                        }
                    }
                ) {
                    AppText(
                        text = "Terms of Service",
                        style = AppTextStyle.Label,
                        color = primaryColor
                    )
                }
            }
        }
    }
}

/**
 * Helper function to check usage stats permission
 */
private fun checkUsageStatsPermission(context: android.content.Context): Boolean {
    val appOps = context.getSystemService(android.content.Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}
