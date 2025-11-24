package com.app.screentime.record.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.app.screentime.ui.atom.AppLoader
import com.app.screentime.ui.atom.AppLoaderType
import com.app.screentime.ui.atom.AppSecondaryButton
import com.app.screentime.ui.atom.AppText
import com.app.screentime.ui.atom.AppTextStyle
import com.app.screentime.ui.theme.AppColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
fun TimelineTab(
    uiState: RecordDetailUiState, viewModel: RecordDetailViewModel, colors: AppColors
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppLoader(type = AppLoaderType.CIRCULAR)
            }
        }

        uiState.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                        containerColor = colors.error.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        AppText(
                            text = "Error", style = AppTextStyle.SubTitle, color = colors.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AppText(
                            text = uiState.error,
                            style = AppTextStyle.Body,
                            color = colors.textPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppSecondaryButton(
                            text = "Dismiss", onClick = { viewModel.clearError() })
                    }
                }
            }
        }

        uiState.stats.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                AppText(
                    text = "No usage records found",
                    style = AppTextStyle.Body,
                    color = colors.textSecondary
                )
            }
        }

        else -> {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            var selectedHour by remember { mutableStateOf<Int?>(null) }
            
            // Extract unique hours from timeline items
            val availableHours = remember(uiState.timeLines) {
                uiState.timeLines
                    .filterIsInstance<TimelineListItem.HourHeaderItem>()
                    .map { it.hour }
                    .sortedDescending()
            }
            
            // Create a map of hour to item index for scrolling
            val hourToIndexMap = remember(uiState.timeLines) {
                uiState.timeLines.mapIndexedNotNull { index, item ->
                    if (item is TimelineListItem.HourHeaderItem) {
                        item.hour to index
                    } else null
                }.toMap()
            }
            
            Column(modifier = Modifier.fillMaxSize()) {
                // Hour range chips
                if (availableHours.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableHours.forEach { hour ->
                            HourRangeChip(
                                hour = hour,
                                colors = colors,
                                selected = selectedHour == hour,
                                onClick = {
                                    selectedHour = hour
                                    hourToIndexMap[hour]?.let { index ->
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
                
                // Timeline list
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = uiState.timeLines,
                        key = { index, item ->
                            when (item) {
                                is TimelineListItem.HourHeaderItem -> "header-${item.hour}"
                                is TimelineListItem.TimelineEventItem -> "item-${item.stat.packageName}-${item.stat.eventTimestamp}-$index"
                            }
                        }
                    ) { listIndex, item ->
                        when (item) {
                            is TimelineListItem.HourHeaderItem -> {
                                if (listIndex > 0) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                HourRangeHeader(hour = item.hour, colors = colors)
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            is TimelineListItem.TimelineEventItem -> {
                                TimelineItem(
                                    stat = item.stat,
                                    isFirst = item.isFirst,
                                    isLast = item.isLast,
                                    colors = colors
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

