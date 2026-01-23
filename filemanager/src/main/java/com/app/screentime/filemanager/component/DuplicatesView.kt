package com.app.screentime.filemanager.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.app.screentime.config.R
import com.app.screentime.filemanager.repository.FileManagerRepository
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSLazyColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerSize
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * View to display and manage duplicate files
 */
@Composable
fun DuplicatesView(
    duplicateGroups: List<FileManagerRepository.DuplicateFileGroup>,
    isScanning: Boolean,
    scheme: ODSTheme,
    onScanClick: () -> Unit,
    onDeleteGroup: (FileManagerRepository.DuplicateFileGroup) -> Unit,
    onDeleteAll: () -> Unit
) {
    when {
        isScanning -> {
            ODSBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DSVariables.spacingComponent4),
                contentAlignment = Alignment.Center
            ) {
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = DSVariables.spacingComponent3
                ) {
                    ODSLoadingSpinner(
                        modifier = Modifier,
                        scheme = scheme,
                        props = ODSLoadingSpinnerProps(
                            labelText = stringResource(R.string.scanning_for_duplicates),
                            size = ODSLoadingSpinnerSize.SMALL,
                            variant = ODSLoadingSpinnerVariant.STANDARD,
                            labelAlignment = ODSLoadingSpinnerLabelAlignment.HORIZONTAL
                        )
                    )
                }
            }
        }

        duplicateGroups.isEmpty() -> {
            ODSBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(DSVariables.spacingComponent4),
                contentAlignment = Alignment.Center
            ) {
                ODSColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    gap = DSVariables.spacingComponent3
                ) {
                    ODSText(
                        text = stringResource(R.string.no_duplicates_found),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            label = stringResource(R.string.scan_for_duplicates),
                            variant = ODSButtonVariant.PRIMARY,
                            size = ODSButtonSize.SMALL
                        ),
                        onClick = onScanClick
                    )
                }
            }
        }

        else -> {
            val context = LocalContext.current
            val totalSize = duplicateGroups.sumOf { it.totalSize }
            val totalFiles =
                duplicateGroups.sumOf { it.files.size - 1 } // Files that can be deleted

            ODSLazyColumn(
                modifier = Modifier.fillMaxSize(),
                gap = DSVariables.spacingComponent3,
                padding = ODSPadding(
                    horizontal = DSVariables.spacingComponent4,
                    vertical = DSVariables.spacingComponent3
                )
            ) {
                item {
                    // Summary card
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                        padding = ODSPadding(all = DSVariables.spacingComponent3)
                    ) {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = stringResource(
                                    R.string.found_duplicate_groups,
                                    duplicateGroups.size
                                ),
                                style = DSTextStyles.bodyMBold,
                                color = scheme.basicText
                            )
                            ODSText(
                                text = stringResource(
                                    R.string.can_free_up_space,
                                    formatFileSize(context, totalSize)
                                ),
                                style = DSTextStyles.bodySRegular,
                                color = scheme.basicTextRecessive
                            )
                            ODSRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2)
                            ) {
                                ODSButton(
                                    modifier = Modifier.weight(1f),
                                    scheme = scheme,
                                    props = ODSButtonProps(
                                        label = stringResource(
                                            R.string.delete_all_files,
                                            totalFiles
                                        ),
                                        variant = ODSButtonVariant.PRIMARY,
                                        size = ODSButtonSize.SMALL
                                    ),
                                    onClick = onDeleteAll
                                )
                                ODSButton(
                                    modifier = Modifier.weight(1f),
                                    scheme = scheme,
                                    props = ODSButtonProps(
                                        label = stringResource(R.string.rescan),
                                        variant = ODSButtonVariant.SECONDARY,
                                        size = ODSButtonSize.SMALL
                                    ),
                                    onClick = onScanClick
                                )
                            }
                        }
                    }
                }

                items(
                    items = duplicateGroups,
                    key = { it.files.first().file.absolutePath }
                ) { group ->
                    DuplicateGroupItem(
                        group = group,
                        scheme = scheme,
                        onDelete = { onDeleteGroup(group) }
                    )
                }
            }
        }
    }
}

/**
 * Item showing a group of duplicate files
 */
@Composable
fun DuplicateGroupItem(
    group: FileManagerRepository.DuplicateFileGroup,
    scheme: ODSTheme,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    ODSBox(
        modifier = Modifier.fillMaxWidth(),
        background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
        cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
        padding = ODSPadding(all = DSVariables.spacingComponent3)
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = DSVariables.spacingComponent2
        ) {
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ODSColumn(
                    modifier = Modifier.weight(1f),
                    gap = DSVariables.spacingComponent1
                ) {
                    ODSText(
                        text = group.files.first().name,
                        style = DSTextStyles.bodySRegular,
                        color = scheme.basicText
                    )
                    ODSText(
                        text = stringResource(
                            R.string.copies_each,
                            group.files.size,
                            formatFileSize(context, group.files.first().size)
                        ),
                        style = DSTextStyles.oxMicrocopyRegular,
                        color = scheme.basicTextRecessive
                    )
                    ODSText(
                        text = stringResource(
                            R.string.can_free,
                            formatFileSize(context, group.totalSize)
                        ),
                        style = DSTextStyles.oxMicrocopyRegular,
                        color = scheme.functionalSuccessStandard
                    )
                }
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        label = stringResource(R.string.delete),
                        variant = ODSButtonVariant.PRIMARY,
                        size = ODSButtonSize.SMALL
                    ),
                    onClick = onDelete
                )
            }

            // Show file paths
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent1
            ) {
                group.files.forEachIndexed { index, file ->
                    ODSRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent2),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ODSText(
                            text = if (index == 0) stringResource(R.string.keep) else stringResource(
                                R.string.delete_duplicate
                            ),
                            style = DSTextStyles.oxMicrocopyRegular,
                            color = if (index == 0) scheme.functionalSuccessStandard else scheme.functionalDestructiveStandard
                        )
                        ODSText(
                            text = file.file.parent ?: "",
                            style = DSTextStyles.oxMicrocopyRegular,
                            color = scheme.basicTextRecessive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

private fun formatFileSize(context: android.content.Context, bytes: Long): String {
    if (bytes < 1024) return context.getString(R.string.file_size_bytes, bytes.toInt())
    val kb = bytes / 1024.0
    if (kb < 1024) return context.getString(R.string.file_size_kb, kb)
    val mb = kb / 1024.0
    if (mb < 1024) return context.getString(R.string.file_size_mb, mb)
    val gb = mb / 1024.0
    return context.getString(R.string.file_size_gb, gb)
}

