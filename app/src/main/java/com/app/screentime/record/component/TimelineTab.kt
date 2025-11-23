package com.app.screentime.record.component

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
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
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            HourRangeHeader(hour = item.hour, colors = colors)
                            Spacer(modifier = Modifier.height(12.dp))
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

