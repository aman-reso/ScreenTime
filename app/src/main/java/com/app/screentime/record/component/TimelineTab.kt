package com.app.screentime.record.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.app.screentime.R
import kotlinx.coroutines.launch
import com.app.screentime.record.model.TimelineListItem
import com.app.screentime.record.viewmodel.RecordDetailUiState
import com.app.screentime.record.viewmodel.RecordDetailViewModel
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant

import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotification
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationMode
import com.telekom.odsystem.organisms.inlinenotification.ODSInlineNotificationProps

@Composable
fun TimelineTab(
    uiState: RecordDetailUiState, viewModel: RecordDetailViewModel, scheme: com.telekom.odsystem.tokens.tokens.ODSTheme
) {
    
    when {
        uiState.isLoading -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSLoadingSpinner(
                    scheme = scheme,
                    props = ODSLoadingSpinnerProps(
                        size = ODSLoadingSpinnerSize.LARGE,
                        variant = ODSLoadingSpinnerVariant.STANDARD,
                        labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL,
                        labelText = stringResource(R.string.loading)
                    )
                )
            }
        }

        uiState.error != null -> {
            ODSColumn(
                modifier = Modifier.fillMaxSize(),
                padding = ODSPadding(all = DSVariables.spacingComponent5)
            ) {
                ODSInlineNotification(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = scheme,
                    props = ODSInlineNotificationProps(
                        mode = ODSInlineNotificationMode.ERROR,
                        title = stringResource(R.string.error),
                        text = uiState.error ?: stringResource(R.string.failed_to_load_usage_data),
                        link1Props = ODSLinkProps(
                            label = stringResource(R.string.retry)
                        ),
                        showCloseButton = true
                    ),
                    onDismiss = { viewModel.clearError() },
                    onFirstLinkClicked = {
                        viewModel.clearError()
                    }
                )
            }
        }

        uiState.stats.isEmpty() -> {
            ODSBox(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ODSText(
                    text = "No usage records found",
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
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
            
            ODSColumn(modifier = Modifier.fillMaxSize()) {
                // Hour range chips
                if (availableHours.isNotEmpty()) {
                    ODSRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        padding = ODSPadding(
                            horizontal = DSVariables.spacingComponent4,
                            vertical = DSVariables.spacingComponent2
                        ),
                        gap = DSVariables.spacingComponent2
                    ) {
                        availableHours.forEach { hour ->
                            HourRangeChip(
                                hour = hour,
                                scheme = scheme,
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
                ODSLazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    padding = ODSPadding(
                        horizontal = DSVariables.spacingComponent4,
                        vertical = DSVariables.spacingComponent2
                    ),
                    gap = DSVariables.spacingComponent2
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
                                    Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
                                }
                                HourRangeHeader(hour = item.hour, scheme = scheme)
                                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))
                            }

                            is TimelineListItem.TimelineEventItem -> {
                                TimelineItem(
                                    stat = item.stat,
                                    isFirst = item.isFirst,
                                    isLast = item.isLast,
                                    scheme = scheme
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

