package com.app.screentime.blocking.screen

import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.screentime.blocking.model.BlockingRule
import com.app.screentime.blocking.viewmodel.AppBlockingViewModel
import com.app.screentime.search.component.GlassSearchBar
import com.app.screentime.ui.atom.AppIcon
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.atom.glassBottomSheetBackground
import com.app.screentime.ui.theme.LocalAppColors
import com.app.screentime.R
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBlockingScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    viewModel: AppBlockingViewModel = hiltViewModel(),
    onNavigateToAppSelection: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Check permissions
    var hasAccessibilityPermission by remember {
        mutableStateOf(
            isAccessibilityServiceEnabled(
                context
            )
        )
    }
    var hasOverlayPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }

    // Re-check permissions when screen is focused
    LaunchedEffect(Unit) {
        hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
        hasOverlayPermission = Settings.canDrawOverlays(context)
    }

    // Re-check permissions when lifecycle resumes (user returns from settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasAccessibilityPermission = isAccessibilityServiceEnabled(context)
                hasOverlayPermission = Settings.canDrawOverlays(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val allPermissionsGranted = hasAccessibilityPermission && hasOverlayPermission

    // Show permission screen if permissions not granted
    if (!allPermissionsGranted) {
        PermissionScreenContent(
            modifier = modifier,
            hasAccessibilityPermission = hasAccessibilityPermission,
            hasOverlayPermission = hasOverlayPermission,
            onAccessibilityPermissionClick = {
                try {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Please enable accessibility service in Accessibility Settings",
                        Toast.LENGTH_LONG
                    ).show()
                    // Re-check permission periodically when user might return
                    coroutineScope.launch {
                        repeat(10) {
                            delay(1000)
                            val newStatus = isAccessibilityServiceEnabled(context)
                            if (newStatus != hasAccessibilityPermission) {
                                hasAccessibilityPermission = newStatus
                                return@launch
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error opening Accessibility Settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onOverlayPermissionClick = {
                try {
                    if (!Settings.canDrawOverlays(context)) {
                        val intent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            "package:${context.packageName}".toUri()
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        Toast.makeText(
                            context,
                            "Please enable 'Display over other apps' permission",
                            Toast.LENGTH_LONG
                        ).show()
                        // Re-check permission periodically when user might return
                        coroutineScope.launch {
                            repeat(10) {
                                delay(1000)
                                val newStatus = Settings.canDrawOverlays(context)
                                if (newStatus != hasOverlayPermission) {
                                    hasOverlayPermission = newStatus
                                    return@launch
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Overlay permission already granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error opening Overlay Settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        return
    }

    // Show app blocking screen if permissions granted
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf("All Apps", "Blocked")
    val pagerState = rememberPagerState(pageCount = { tabs.size }, initialPage = 0)

    var showAddBlockDialog by remember { mutableStateOf(false) }
    var selectedPackageName by remember { mutableStateOf<String?>(null) }
    var selectedAppName by remember { mutableStateOf<String?>(null) }

    // Sync tab selection with pager state
    LaunchedEffect(pagerState.currentPage) {
        // This will be handled by the SegmentedControl onClick
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.tint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                AppText(
                    text = stringResource(R.string.app_blocking),
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Segmented Control
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                com.app.screentime.ui.atom.SegmentedControl(
                    items = tabs,
                    selectedIndex = pagerState.currentPage,
                    onItemSelected = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }

            // Tab Content with Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
                        // All Apps Tab
                        AllAppsTab(
                            onAppSelected = { packageName, appName ->
                                selectedPackageName = packageName
                                selectedAppName = appName
                                showAddBlockDialog = true
                            }
                        )
                    }

                    1 -> {
                        // Blocked Apps Tab
                        if (uiState.rules.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Block,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = colors.textMuted
                                    )
                                    AppText(
                                        text = "No blocking rules",
                                        style = AppTextStyle.Body,
                                        color = colors.textMuted,
                                        textAlign = TextAlign.Center
                                    )
                                    AppText(
                                        text = "Go to 'All Apps' tab to add a blocking rule",
                                        style = AppTextStyle.Label,
                                        color = colors.textMuted,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = uiState.rules,
                                    key = { rule ->
                                        when (rule) {
                                            is BlockingRule.InstantBlock -> rule.packageName
                                            is BlockingRule.LaunchBasedBlock -> rule.packageName
                                            is BlockingRule.DurationBasedBlock -> rule.packageName
                                        }
                                    }
                                ) { rule ->
                                    BlockingRuleCard(
                                        rule = rule,
                                        onRemove = {
                                            val packageName = when (rule) {
                                                is BlockingRule.InstantBlock -> rule.packageName
                                                is BlockingRule.LaunchBasedBlock -> rule.packageName
                                                is BlockingRule.DurationBasedBlock -> rule.packageName
                                            }
                                            viewModel.removeBlockingRule(packageName)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Block Bottom Sheet
        if (showAddBlockDialog && selectedPackageName != null && selectedAppName != null) {
            AddBlockingRuleBottomSheet(
                selectedAppName = selectedAppName!!,
                selectedPackageName = selectedPackageName!!,
                onDismiss = {
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockInstantly = { packageName, appName ->
                    viewModel.blockAppInstantly(packageName, appName)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockAfterLaunches = { packageName, appName, maxLaunches ->
                    viewModel.blockAppAfterLaunches(packageName, appName, maxLaunches)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                },
                onBlockAfterDuration = { packageName, appName, maxDurationMinutes ->
                    viewModel.blockAppAfterDuration(packageName, appName, maxDurationMinutes)
                    showAddBlockDialog = false
                    selectedPackageName = null
                    selectedAppName = null
                }
            )
        }
    }
}

@Composable
private fun BlockingRuleCard(
    rule: BlockingRule,
    onRemove: () -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current

    val (packageName, title, description) = when (rule) {
        is BlockingRule.InstantBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Blocked instantly"
        )

        is BlockingRule.LaunchBasedBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Block after ${rule.maxLaunches} launches (Current: ${rule.currentLaunches})"
        )

        is BlockingRule.DurationBasedBlock -> Triple(
            rule.packageName,
            rule.appName,
            "Block after ${rule.maxDurationMinutes} minutes (Current: ${rule.currentDurationMinutes} min)"
        )
    }

    val appInfo = remember(packageName) {
        try {
            context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIcon(
                appInfo = appInfo,
                size = 40.dp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AppText(
                    text = title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                AppText(
                    text = description,
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = colors.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBlockingRuleBottomSheet(
    selectedAppName: String,
    selectedPackageName: String,
    onDismiss: () -> Unit,
    onBlockInstantly: (String, String) -> Unit,
    onBlockAfterLaunches: (String, String, Int) -> Unit,
    onBlockAfterDuration: (String, String, Int) -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    
    var blockType by remember { mutableStateOf<BlockType?>(null) }
    var launchCount by remember { mutableIntStateOf(3) }
    var durationMinutes by remember { mutableIntStateOf(10) }
    
    val appInfo = remember(selectedPackageName) {
        try {
            context.packageManager.getApplicationInfo(selectedPackageName, 0)
        } catch (e: Exception) {
            null
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.background,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .glassBottomSheetBackground()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header with app info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                AppIcon(
                    appInfo = appInfo,
                    size = 40.dp,
                    modifier = Modifier.size(40.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    AppText(
                        text = "Block App",
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    AppText(
                        text = selectedAppName,
                        style = AppTextStyle.Label,
                        color = colors.textSecondary
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = colors.tint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Block Type Selection
            AppText(
                text = "Blocking Type:",
                style = AppTextStyle.Body,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            // Instant Block
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { blockType = BlockType.INSTANT },
                colors = CardDefaults.cardColors(
                    containerColor = if (blockType == BlockType.INSTANT) colors.success.copy(
                        alpha = 0.2f
                    ) else colors.card
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = null,
                            tint = colors.error,
                            modifier = Modifier.size(20.dp)
                        )
                        AppText(
                            text = "Block Instantly",
                            style = AppTextStyle.Body,
                            color = colors.textPrimary
                        )
                    }
                    RadioButton(
                        selected = blockType == BlockType.INSTANT,
                        onClick = { blockType = BlockType.INSTANT },
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Launch Based Block
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { blockType = BlockType.LAUNCH },
                colors = CardDefaults.cardColors(
                    containerColor = if (blockType == BlockType.LAUNCH) colors.success.copy(
                        alpha = 0.2f
                    ) else colors.card
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = colors.success,
                                modifier = Modifier.size(20.dp)
                            )
                            AppText(
                                text = "Block After Launches",
                                style = AppTextStyle.Body,
                                color = colors.textPrimary
                            )
                        }
                        RadioButton(
                            selected = blockType == BlockType.LAUNCH,
                            onClick = { blockType = BlockType.LAUNCH },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (blockType == BlockType.LAUNCH) {
                        AppText(
                            text = "$launchCount ${if (launchCount == 1) "launch" else "launches"}",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary
                        )
                        Slider(
                            value = launchCount.toFloat(),
                            onValueChange = { newValue ->
                                launchCount = newValue.toInt()
                            },
                            valueRange = 1f..20f,
                            steps = 18,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.success,
                                activeTrackColor = colors.success,
                                inactiveTrackColor = colors.textSecondary.copy(alpha = 0.3f)
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(1, 3, 5, 10, 15, 20).forEach { count ->
                                FilterChip(
                                    selected = launchCount == count,
                                    onClick = { launchCount = count },
                                    label = {
                                        AppText(
                                            text = "$count",
                                            style = AppTextStyle.Label,
                                            color = if (launchCount == count) colors.textPrimary else colors.textSecondary
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

            Spacer(modifier = Modifier.height(8.dp))

            // Duration Based Block
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { blockType = BlockType.DURATION },
                colors = CardDefaults.cardColors(
                    containerColor = if (blockType == BlockType.DURATION) colors.success.copy(
                        alpha = 0.2f
                    ) else colors.card
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = colors.success,
                                modifier = Modifier.size(20.dp)
                            )
                            AppText(
                                text = "Block After Duration",
                                style = AppTextStyle.Body,
                                color = colors.textPrimary
                            )
                        }
                        RadioButton(
                            selected = blockType == BlockType.DURATION,
                            onClick = { blockType = BlockType.DURATION },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    if (blockType == BlockType.DURATION) {
                        AppText(
                            text = "$durationMinutes ${if (durationMinutes == 1) "minute" else "minutes"}",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Medium,
                            color = colors.textPrimary
                        )
                        Slider(
                            value = durationMinutes.toFloat(),
                            onValueChange = { newValue ->
                                durationMinutes = newValue.toInt()
                            },
                            valueRange = 5f..60f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = colors.success,
                                activeTrackColor = colors.success,
                                inactiveTrackColor = colors.textSecondary.copy(alpha = 0.3f)
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(5, 10, 15, 30, 45, 60).forEach { minutes ->
                                FilterChip(
                                    selected = durationMinutes == minutes,
                                    onClick = { durationMinutes = minutes },
                                    label = {
                                        AppText(
                                            text = "${minutes}m",
                                            style = AppTextStyle.Label,
                                            color = if (durationMinutes == minutes) colors.textPrimary else colors.textSecondary
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Confirm Button
            Button(
                onClick = {
                    when (blockType) {
                        BlockType.INSTANT -> onBlockInstantly(selectedPackageName, selectedAppName)
                        BlockType.LAUNCH -> onBlockAfterLaunches(selectedPackageName, selectedAppName, launchCount)
                        BlockType.DURATION -> onBlockAfterDuration(selectedPackageName, selectedAppName, durationMinutes)
                        null -> {}
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = blockType != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.success
                ),
                shape = MaterialTheme.shapes.small
            ) {
                AppText(
                    text = "Add Blocking Rule",
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AllAppsTab(
    onAppSelected: (String, String) -> Unit
) {
    val colors = LocalAppColors.current ?: return
    val context = LocalContext.current

    val installedApps = remember { com.app.screentime.blocking.component.getInstalledApps(context) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredApps = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            installedApps
        } else {
            installedApps.filter {
                it.appName.contains(searchQuery, ignoreCase = true) ||
                        it.packageName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search bar
        GlassSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search apps...",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Apps list
        if (filteredApps.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = "No apps found",
                    style = AppTextStyle.Body,
                    color = colors.textMuted,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(
                    items = filteredApps,
                    key = { it.packageName }
                ) { app ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAppSelected(app.packageName, app.appName)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = colors.card
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppIcon(
                                appInfo = app.applicationInfo,
                                size = 40.dp,
                                modifier = Modifier.size(40.dp)
                            )
                            AppText(
                                text = app.appName,
                                style = AppTextStyle.Body,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = colors.textMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionScreenContent(
    modifier: Modifier = Modifier,
    hasAccessibilityPermission: Boolean,
    hasOverlayPermission: Boolean,
    onAccessibilityPermissionClick: () -> Unit,
    onOverlayPermissionClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Icon
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = colors.success
            )

            // Title
            AppText(
                text = "App Blocking Permissions",
                style = AppTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )

            // Description
            AppText(
                text = "To enable app blocking, we need the following permissions:",
                style = AppTextStyle.Body,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Accessibility Permission Card
            PermissionCard(
                title = "Accessibility Service",
                description = "Required to detect app launches and usage",
                isGranted = hasAccessibilityPermission,
                icon = Icons.Default.Accessibility,
                onClick = onAccessibilityPermissionClick
            )

            // Overlay Permission Card
            PermissionCard(
                title = "Display Over Other Apps",
                description = "Required to show blocking overlay when limits are exceeded",
                isGranted = hasOverlayPermission,
                icon = Icons.Default.Layers,
                onClick = onOverlayPermissionClick
            )
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val colors = LocalAppColors.current ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) colors.success.copy(alpha = 0.1f) else colors.card
        ),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isGranted) colors.success else colors.textMuted
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AppText(
                    text = title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                AppText(
                    text = description,
                    style = AppTextStyle.Label,
                    color = colors.textMuted
                )
            }

            if (isGranted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Granted",
                    modifier = Modifier.size(24.dp),
                    tint = colors.success
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Not granted",
                    modifier = Modifier.size(24.dp),
                    tint = colors.textMuted
                )
            }
        }
    }
}

/**
 * Check if accessibility service is enabled
 */
private fun isAccessibilityServiceEnabled(context: android.content.Context): Boolean {
    val accessibilityManager =
        context.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices =
        accessibilityManager.getEnabledAccessibilityServiceList(android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

    val serviceClassName = com.app.screentime.service.AppAccessibilityService::class.java.name
    val packageName = context.packageName

    // Check if our service is in the enabled services list
    // The service info name is the class name, and we need to match it with the package
    return enabledServices.any { serviceInfo ->
        val serviceInfoName = serviceInfo.resolveInfo.serviceInfo.name
        val servicePackageName = serviceInfo.resolveInfo.serviceInfo.packageName

        // Match by package name and class name
        servicePackageName == packageName && serviceInfoName == serviceClassName
    }
}

private enum class BlockType {
    INSTANT, LAUNCH, DURATION
}

