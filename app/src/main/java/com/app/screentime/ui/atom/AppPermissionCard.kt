package com.app.screentime.ui.atom

import android.app.AppOpsManager
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.app.screentime.R
import com.app.screentime.ui.theme.LocalAppColors

/**
 * App Permission Card - Complete permission request screen
 *
 * This component handles all permission requests internally:
 * - Checks current permission status
 * - Requests permissions in order: Usage Stats → Notifications → VPN
 * - Displays detailed explanation about why app usage data is required
 * - Shows an "Allow" button to grant permissions
 *
 * @param modifier Modifier for the component
 * @param onAllPermissionsGranted Callback when all permissions are granted
 */
@Composable
fun AppPermissionCard(
    modifier: Modifier = Modifier,
    onAllPermissionsGranted: () -> Unit = {}
) {
    val context = LocalContext.current

    // Permission state - only check Usage Stats for now
    var hasUsageStatsPermission by remember { mutableStateOf(false) }

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasUsageStatsPermission = checkUsageStatsPermission(context)

        // If permission is already granted, notify immediately
        if (hasUsageStatsPermission) {
            onAllPermissionsGranted()
        }
    }

    // Usage stats permission launcher (opens settings)
    val usageStatsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Check permission after returning from settings
        hasUsageStatsPermission = checkUsageStatsPermission(context)
        if (hasUsageStatsPermission) {
            // Usage Stats permission granted - move to next screen
            onAllPermissionsGranted()
        }
    }

    // Handle Allow button click
    val handleAllowClick = {
        if (!hasUsageStatsPermission) {
            // Request Usage Stats permission
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.data = "package:${context.packageName}".toUri()
            usageStatsPermissionLauncher.launch(intent)
        } else {
            // Permission already granted
            onAllPermissionsGranted()
        }
    }
    val colors = LocalAppColors.current ?: return
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                modifier = Modifier.size(80.dp),
                contentDescription = stringResource(R.string.content_description_permissions_icon),
                tint = colors.accent
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            AppText(
                text = stringResource(R.string.permissions_required),
                style = AppTextStyle.SubTitle
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = colors.textHint
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Detailed Explanation
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppText(
                    text = stringResource(R.string.permission_explanation_title),
                    style = AppTextStyle.Body,
                    color = colors.textPrimary
                )

                AppText(
                    text = stringResource(R.string.permission_explanation_detail),
                    style = AppTextStyle.Label,
                    color = colors.textHint
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Why we need it section
                AppText(
                    text = stringResource(R.string.permission_why_required),
                    style = AppTextStyle.Body,
                    color = colors.textPrimary
                )

                AppText(
                    text = stringResource(R.string.permission_why_required_detail),
                    style = AppTextStyle.Label,
                    color = colors.textHint
                )

                Spacer(modifier = Modifier.height(8.dp))

                // What we track section
                AppText(
                    text = stringResource(R.string.permission_what_we_track),
                    style = AppTextStyle.Body,
                    color = colors.textPrimary
                )

                AppText(
                    text = stringResource(R.string.permission_what_we_track_detail),
                    style = AppTextStyle.Label,
                    color = colors.textHint
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Privacy assurance
                AppText(
                    text = stringResource(R.string.permission_privacy_assurance),
                    style = AppTextStyle.Body,
                    color = colors.textPrimary
                )

                AppText(
                    text = stringResource(R.string.permission_privacy_assurance_detail),
                    style = AppTextStyle.Label,
                    color = colors.textHint
                )
            }

            // Spacer to push button to bottom
            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(32.dp))

            // Privacy Policy and Terms and Conditions links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = stringResource(R.string.privacy_policy),
                    style = AppTextStyle.Label,
                    color = colors.success,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://aman-reso.github.io/AppTime-HTML/privacy-policy.html".toUri()
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.util.Log.e("AppPermissionCard", "Error opening Privacy Policy", e)
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    textDecoration = TextDecoration.Underline
                )
                
                AppText(
                    text = " • ",
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
                
                AppText(
                    text = stringResource(R.string.terms_of_service),
                    style = AppTextStyle.Label,
                    color = colors.success,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://aman-reso.github.io/AppTime-HTML/terms-and-conditions.html".toUri()
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.util.Log.e("AppPermissionCard", "Error opening Terms and Conditions", e)
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Allow Button
            AppPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.action_allow),
                onClick = handleAllowClick
            )

            Spacer(modifier = Modifier.height(16.dp))
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

