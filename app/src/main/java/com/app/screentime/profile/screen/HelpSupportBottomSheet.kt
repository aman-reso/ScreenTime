package com.app.screentime.profile.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportBottomSheetContent(
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val supportEmail = "help.testmate@gmail.com"

    ModalBottomSheet(
        containerColor = colors.background,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassBottomSheetBackground()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Help & Support",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Information text
            AppText(
                text = "We're here to help! If you have any questions, issues, or feedback, please reach out to us using the email below.",
                style = AppTextStyle.Body,
                color = colors.textSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colors.card
                ),
                onClick = {
                    // Open email client
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
                        putExtra(Intent.EXTRA_SUBJECT, "AppTime Support Request")
                    }
                    try {
                        context.startActivity(Intent.createChooser(intent, "Send email via"))
                    } catch (e: Exception) {
                        // No email client available
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = colors.success,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        AppText(
                            text = "Email Support",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Bold,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AppText(
                            text = supportEmail,
                            style = AppTextStyle.Label,
                            color = colors.success
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional information
            AppText(
                text = "Send your query",
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            AppText(
                text = "Click on the email card above to open your email client and send us your query. We typically respond within 24-48 hours.",
                style = AppTextStyle.Label,
                color = colors.textMuted
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

