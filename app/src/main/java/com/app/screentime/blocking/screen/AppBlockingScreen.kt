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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.border
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
import com.app.screentime.ui.atom.AppPrimaryButton
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
            onContinue = {
                // This will be handled by the permission check
            },
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
            .background(Color(0xFFFFFFFF)) // White background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFFFF))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (navController != null) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1C1B1F),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                AppText(
                    text = "App blocking",
                    style = AppTextStyle.SubTitle,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1C1B1F),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(40.dp)) // Balance for back button
            }

            // Segmented Tabs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Custom segmented control matching design
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            Color(0xFFFAFAFA), // Surface container
                            shape = RoundedCornerShape(9999.dp)
                        )
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                AppText(
                                    text = tab,
                                    style = AppTextStyle.Body,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else Color(0xFF49454F)
                                )
                            }
                        }
                    }
                }
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
fun AddBlockingRuleBottomSheet(
    selectedAppName: String,
    selectedPackageName: String,
    onDismiss: () -> Unit,
    onBlockInstantly: (String, String) -> Unit,
    onBlockAfterLaunches: (String, String, Int) -> Unit,
    onBlockAfterDuration: (String, String, Int) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var blockType by remember { mutableStateOf<BlockType?>(BlockType.INSTANT) }
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
        containerColor = Color(0xFFFFFFFF),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIcon(
                        appInfo = appInfo,
                        size = 64.dp,
                        modifier = Modifier.size(64.dp)
                    )
                    Column {
                        AppText(
                            text = "Block App",
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C1B1F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AppText(
                            text = selectedAppName,
                            style = AppTextStyle.Label,
                            color = Color(0xFF49454F)
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF1C1B1F),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Section Label
            AppText(
                text = "BLOCKING TYPE",
                style = AppTextStyle.Label,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF49454F),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Radio Group
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Block Instantly
                RadioCard(
                    title = "Block Instantly",
                    icon = Icons.Default.Block,
                    isSelected = blockType == BlockType.INSTANT,
                    onClick = { blockType = BlockType.INSTANT }
                )

                // Block After Launches
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioCard(
                        title = "Block After Launches",
                        icon = Icons.Default.Refresh,
                        isSelected = blockType == BlockType.LAUNCH,
                        onClick = { blockType = BlockType.LAUNCH }
                    )

                    // Config Section for Launches
                    if (blockType == BlockType.LAUNCH) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0x0F6750A4), // rgba(103, 80, 164, 0.06)
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(20.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AppText(
                                    text = "$launchCount launches",
                                    style = AppTextStyle.Body,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6750A4)
                                )

                                // Slider
                                Slider(
                                    value = launchCount.toFloat(),
                                    onValueChange = { launchCount = it.toInt() },
                                    valueRange = 1f..20f,
                                    steps = 18,
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFF6750A4),
                                        activeTrackColor = Color(0xFF6750A4),
                                        inactiveTrackColor = Color(0xFFF5F5F5)
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                // Chips
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    listOf(1, 3, 5, 10, 15, 20).forEach { count ->
                                        FilterChip(
                                            selected = launchCount == count,
                                            onClick = { launchCount = count },
                                            label = {
                                                AppText(
                                                    text = "$count",
                                                    style = AppTextStyle.Label,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (launchCount == count) Color.White else Color(
                                                        0xFF1C1B1F
                                                    )
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = Color(0xFF6750A4),
                                                containerColor = Color(0xFFFFFFFF),
                                                selectedLabelColor = Color.White,
                                                labelColor = Color(0xFF1C1B1F)
                                            ),
                                            shape = RoundedCornerShape(9999.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Block After Duration
                RadioCard(
                    title = "Block After Duration",
                    icon = Icons.Default.Timer,
                    isSelected = blockType == BlockType.DURATION,
                    onClick = { blockType = BlockType.DURATION }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Add Blocking Rule Button
            AppPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Add Blocking Rule",
                enabled = blockType != null,
                onClick = {
                    when (blockType) {
                        BlockType.INSTANT -> onBlockInstantly(selectedPackageName, selectedAppName)
                        BlockType.LAUNCH -> onBlockAfterLaunches(
                            selectedPackageName,
                            selectedAppName,
                            launchCount
                        )

                        BlockType.DURATION -> onBlockAfterDuration(
                            selectedPackageName,
                            selectedAppName,
                            durationMinutes
                        )

                        null -> {}
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RadioCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                Color(0xFFEADDFF) // Primary container
            } else {
                Color(0xFFFFFFFF) // White
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = if (isSelected) {
                Color(0xFF6750A4) // Primary
            } else {
                Color(0xFFE0E0E0) // Outline variant
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 2.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Color(0xFF6750A4) else Color(0xFF1C1B1F)
                )
                AppText(
                    text = title,
                    style = AppTextStyle.Body,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color(0xFF21005D) else Color(0xFF1C1B1F)
                )
            }

            // Radio Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isSelected) Color(0xFF6750A4) else Color.Transparent,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color(0xFF6750A4) else Color(0xFF79747E),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(0xFFEADDFF),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun AllAppsTab(
    onAppSelected: (String, String) -> Unit
) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        Color(0xFFF8F9FA),
                        shape = RoundedCornerShape(9999.dp)
                    )
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFF49454F)
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        color = Color(0xFF1C1B1F)
                    ),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            AppText(
                                text = "Search apps...",
                                style = AppTextStyle.Body,
                                color = Color(0xFF49454F)
                            )
                        }
                        innerTextField()
                    }
                )
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFF49454F)
                )
            }
        }

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
                    color = Color(0xFF49454F),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = filteredApps,
                    key = { it.packageName }
                ) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                            .clickable {
                                onAppSelected(app.packageName, app.appName)
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppIcon(
                            appInfo = app.applicationInfo,
                            size = 48.dp,
                            modifier = Modifier.size(48.dp)
                        )
                        AppText(
                            text = app.appName,
                            style = AppTextStyle.Body,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1C1B1F),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF79747E),
                            modifier = Modifier.size(20.dp)
                        )
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
    onOverlayPermissionClick: () -> Unit,
    onContinue: () -> Unit = {}
) {
    val colors = LocalAppColors.current ?: return
    val allPermissionsGranted = hasAccessibilityPermission && hasOverlayPermission

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // White background
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Hero Icon with gradient background
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6750A4), // Primary purple
                                Color(0xFF8B7AC7)  // Lighter purple
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            AppText(
                text = "App Blocking Permissions",
                style = AppTextStyle.Title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1B1F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            AppText(
                text = "To enable app blocking, we need the following permissions to ensure your focus is protected.",
                style = AppTextStyle.Body,
                color = Color(0xFF49454F),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Permission Cards
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Accessibility Permission Card (Purple gradient)
                PermissionCard(
                    title = "Accessibility Service",
                    description = "Required to detect app launches and usage.",
                    isGranted = hasAccessibilityPermission,
                    icon = Icons.Default.Accessibility,
                    onClick = onAccessibilityPermissionClick,
                    gradientColors = listOf(
                        Color(0x147F378B), // rgba(103, 80, 164, 0.08)
                        Color(0x1E8B7AC7)  // rgba(139, 122, 199, 0.12)
                    ),
                    borderColor = Color(0x337F378B), // rgba(103, 80, 164, 0.2)
                    iconGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF6750A4), Color(0xFF8B7AC7))
                    )
                )

                // Overlay Permission Card (Blue gradient)
                PermissionCard(
                    title = "Display Over Other Apps",
                    description = "Required to show blocking overlay when limits are exceeded.",
                    isGranted = hasOverlayPermission,
                    icon = Icons.Default.Layers,
                    onClick = onOverlayPermissionClick,
                    gradientColors = listOf(
                        Color(0x140EA5E9), // rgba(14, 165, 233, 0.08)
                        Color(0x1E3B82F6)  // rgba(59, 130, 246, 0.12)
                    ),
                    borderColor = Color(0x330EA5E9), // rgba(14, 165, 233, 0.2)
                    iconGradient = Brush.linearGradient(
                        colors = listOf(Color(0xFF0EA5E9), Color(0xFF3B82F6))
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            if (allPermissionsGranted) {
                com.app.screentime.ui.atom.AppPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Continue",
                    onClick = onContinue
                )
            }
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    gradientColors: List<Color>,
    borderColor: Color,
    iconGradient: Brush
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with gradient background
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            iconGradient,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                }

                // Content
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AppText(
                        text = title,
                        style = AppTextStyle.Body,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1B1F)
                    )
                    AppText(
                        text = description,
                        style = AppTextStyle.Label,
                        color = Color(0xFF49454F)
                    )
                }

                // Arrow forward icon
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Action",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF49454F)
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

