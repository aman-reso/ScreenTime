package com.app.screentime.blocking.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.R
import com.app.screentime.blocking.viewmodel.BlockedLinksViewModel
import com.app.screentime.database.entity.BlockedLinkEntity
import com.app.screentime.service.ScreenTimeVpnService
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.LocalAppColors
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedLinksScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: BlockedLinksViewModel = hiltViewModel()
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBlockedLinks()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.background)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.tint
                        )
                    }
                    AppText(
                        text = "Blocked Links",
                        style = AppTextStyle.SubTitle,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }
                IconButton(
                    onClick = { viewModel.showAddLinkDialog() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Link",
                        tint = colors.success
                    )
                }
            }
        }

        // Content with LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp) // Space for header
                .padding(horizontal = 16.dp)
        ) {
            // Stats Card with Gradient
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    colors.success.copy(alpha = 0.25f),
                                    colors.success.copy(alpha = 0.15f),
                                    colors.success.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            AppText(
                                text = "Total Blocked",
                                style = AppTextStyle.Label,
                                color = colors.textSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = "${uiState.blockedLinks.size}",
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            AppText(
                                text = "Total Blocks",
                                style = AppTextStyle.Label,
                                color = colors.textSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            AppText(
                                text = "${uiState.totalBlockCount}",
                                style = AppTextStyle.Title,
                                fontWeight = FontWeight.Bold,
                                color = colors.success
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.success)
                    }
                }
            } else if (uiState.blockedLinks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = colors.textMuted,
                                modifier = Modifier.size(64.dp)
                            )
                            AppText(
                                text = "No blocked links",
                                style = AppTextStyle.Body,
                                color = colors.textMuted
                            )
                            AppText(
                                text = "Tap + to add a link to block",
                                style = AppTextStyle.Label,
                                color = colors.textSecondary
                            )
                        }
                    }
                }
            } else {
                items(
                    items = uiState.blockedLinks,
                    key = { it.id }
                ) { link ->
                    BlockedLinkItem(
                        link = link,
                        onDelete = {
                            viewModel.deleteBlockedLink(link.id)
                            // Reload VPN service if running
                            reloadVpnBlockedLinks(context)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { viewModel.showAddLinkDialog() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colors.success
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Link",
                tint = colors.textOnPrimary
            )
        }
    }

    // Add Link Bottom Sheet
    if (uiState.showAddDialog) {
        AddBlockedLinkBottomSheet(
            onDismiss = { viewModel.hideAddLinkDialog() },
            onAdd = { link ->
                viewModel.addBlockedLink(link)
                // Reload VPN service if running
                reloadVpnBlockedLinks(context)
            }
        )
    }
}

@Composable
private fun BlockedLinkItem(
    link: BlockedLinkEntity,
    onDelete: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        shape = RoundedCornerShape(12.dp)
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
                    text = link.link,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = colors.error,
                            modifier = Modifier.size(14.dp)
                        )
                        AppText(
                            text = "Blocked ${link.blockedCount} times",
                            style = AppTextStyle.Label,
                            color = colors.textSecondary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    AppText(
                        text = dateFormat.format(Date(link.createdAt)),
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = colors.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBlockedLinkBottomSheet(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var linkText by remember { mutableStateOf("") }

    ModalBottomSheet(
        containerColor = colors.background,
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppText(
                    text = "Add Blocked Link",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
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

            // Input field
            OutlinedTextField(
                value = linkText,
                onValueChange = { linkText = it },
                label = {
                    Text(
                        "Domain or URL (e.g., example.com)",
                        color = colors.textSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    focusedLabelColor = colors.textSecondary,
                    unfocusedLabelColor = colors.textSecondary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Add button
            Button(
                onClick = {
                    if (linkText.isNotBlank()) {
                        onAdd(linkText.trim())
                        linkText = ""
                        onDismiss()
                    }
                },
                enabled = linkText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.success,
                    disabledContainerColor = colors.textMuted
                )
            ) {
                Text(
                    "Add Link",
                    color = colors.textOnPrimary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun reloadVpnBlockedLinks(context: android.content.Context) {
    // Try to reload blocked links in VPN service if it's running
    try {
        val serviceIntent = android.content.Intent(context, ScreenTimeVpnService::class.java)
        serviceIntent.putExtra("reload_links", true)
        context.startService(serviceIntent)
    } catch (e: Exception) {
        // VPN service might not be running, ignore
    }
}

