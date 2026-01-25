package com.app.screentime.customisation.screen

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.screentime.config.R
import com.app.screentime.customisation.manager.CustomisationRefreshManager
import com.app.screentime.customisation.model.ColorOption
import com.app.screentime.customisation.viewmodel.CustomisationViewModel
import com.app.screentime.ui.theme.LocalThemeMode
import kotlinx.coroutines.launch
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.ODSBorder
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.pageheader.ODSPageHeader
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderProps
import com.telekom.odsystem.organisms.pageheader.ODSPageHeaderType
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
fun CustomisationScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onApplyChanges: (() -> Unit)? = null,
    viewModel: CustomisationViewModel = hiltViewModel(),
    scheme: ODSTheme = neutralScheme
) {
    val activity = LocalActivity.current
    val useDarkTheme = LocalThemeMode.current

    LaunchedEffect(Unit) {
        viewModel.loadCustomisationData()
    }

    SideEffect {
        if (activity is AppCompatActivity) {
            activity.enableEdgeToEdge(
                statusBarStyle = if (useDarkTheme) {
                    SystemBarStyle.dark(scheme.basicBackground.getIntColor())
                } else {
                    SystemBarStyle.light(
                        scheme.basicBackground.getIntColor(),
                        darkScrim = scheme.basicBackground.getIntColor()
                    )
                }
            )
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isExpandedScreen = windowSizeClass.isWidthAtLeastBreakpoint(840)

    ODSColumn(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars),
        background = listOf(ODSColorModel(scheme.basicBackground))
    ) {
        ODSPageHeader(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSPageHeaderProps(
                type = ODSPageHeaderType.SUB_PAGE_HEADER
            ),
            subPageTitleSlot = {
                ODSText(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.customisation),
                    style = DSTextStyles.bodyMBold,
                    color = scheme.basicText
                )
            },
            onBackButtonClick = onNavigateBack
        )

        if (isExpandedScreen) {
            ODSRow(
                gap = DSVariables.spacingLayout1,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(
                        horizontal = DSVariables.spacingLayout2,
                        vertical = DSVariables.spacingLayout1
                    ),
                background = listOf(ODSColorModel(scheme.basicBackground))
            ) {
                ODSColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(vertical = DSVariables.spacingComponent4),
                    gap = DSVariables.spacingComponent5,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomisationPreviewCard(
                        selectedColorOption = uiState.selectedColorOption,
                        scheme = scheme
                    )
                    ODSBox(
                        modifier = Modifier.fillMaxWidth(),
                        background = listOf(ODSColorModel(scheme.basicBackground)),
                        padding = ODSPadding(
                            horizontal = DSVariables.spacingLayout2,
                            vertical = DSVariables.spacingComponent4
                        )
                    ) {
                        ODSButton(
                            modifier = Modifier.fillMaxWidth(),
                            props = ODSButtonProps(
                                label = stringResource(R.string.apply_changes),
                                variant = ODSButtonVariant.SECONDARY,
                                size = ODSButtonSize.SMALL,
                                buttonType = ODSButtonButtonType.STANDARD
                            ),
                            onClick = {
                                // Trigger refresh via CustomisationRefreshManager
                                coroutineScope.launch {
                                    CustomisationRefreshManager.triggerRefresh()
                                }
                                // Invoke optional callback
                                onApplyChanges?.invoke()
                                onNavigateBack()
                            },
                            scheme = scheme
                        )
                    }
                }

                ODSBox(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight(),
                    background = listOf(ODSColorModel(scheme.basicStrokeSubtle))
                ) {}

                ODSColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(vertical = DSVariables.spacingComponent4),
                    gap = DSVariables.spacingComponent7,
                    verticalArrangement = Arrangement.Center
                ) {
                    ODSText(
                        text = stringResource(R.string.customisation_description),
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )

                    ODSColumn(
                        gap = DSVariables.spacingComponent5
                    ) {
                        ODSText(
                            text = stringResource(R.string.change_colour),
                            style = DSTextStyles.bodyMBold,
                            color = scheme.basicText
                        )

                        ColorPicker(
                            colors = uiState.availableColors,
                            selectedColorOption = uiState.selectedColorOption,
                            onColorSelected = { colorOption ->
                                viewModel.updateSelectedColor(colorOption)
                            }
                        )
                    }
                }
            }
        } else {
            // Single-pane layout for phones
            ODSColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = DSVariables.spacingLayout2),
                gap = DSVariables.spacingComponent7
            ) {
                Spacer(modifier = Modifier.height(DSVariables.spacingComponent4))

                ODSText(
                    text = stringResource(R.string.customisation_description),
                    style = DSTextStyles.bodyMRegular,
                    color = scheme.basicTextRecessive
                )

                CustomisationPreviewCard(
                    selectedColorOption = uiState.selectedColorOption,
                    scheme = scheme
                )

                ODSColumn(
                    gap = DSVariables.spacingComponent5
                ) {
                    ODSText(
                        text = stringResource(R.string.change_colour),
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )

                    ColorPicker(
                        colors = uiState.availableColors,
                        selectedColorOption = uiState.selectedColorOption,
                        onColorSelected = { colorOption ->
                            viewModel.updateSelectedColor(colorOption)
                        }
                    )
                }
            }
        }
        if (!isExpandedScreen) {
            ODSBox(
                modifier = Modifier.fillMaxWidth(),
                background = listOf(ODSColorModel(scheme.basicBackground)),
                padding = ODSPadding(
                    horizontal = DSVariables.spacingLayout2,
                    vertical = DSVariables.spacingComponent4
                )
            ) {
                ODSButton(
                    modifier = Modifier.fillMaxWidth(),
                    props = ODSButtonProps(
                        label = stringResource(R.string.apply_changes),
                        variant = ODSButtonVariant.SECONDARY,
                        size = ODSButtonSize.SMALL,
                        buttonType = ODSButtonButtonType.STANDARD
                    ),
                    onClick = {
                        // Trigger refresh via CustomisationRefreshManager
                        coroutineScope.launch {
                            CustomisationRefreshManager.triggerRefresh()
                        }
                        // Invoke optional callback
                        onApplyChanges?.invoke()
                        onNavigateBack()
                    },
                    scheme = scheme
                )
            }
        }
    }

}

/**
 * Preview card that matches the YearlyUsageStatsCard layout
 * Shows how the card will look with the selected color
 */
@Composable
private fun CustomisationPreviewCard(
    selectedColorOption: ColorOption,
    scheme: ODSTheme
) {
    val activeScheme = selectedColorOption.scheme
    val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

    ODSColumn(
        gap = DSVariables.spacingComponent2
    ) {
        ODSText(
            text = stringResource(R.string.preview),
            style = DSTextStyles.bodySBold,
            color = scheme.basicTextRecessive
        )

        ODSBox(
            modifier = Modifier.fillMaxWidth(),
            background = listOf(ODSColorModel(activeScheme.basicBackgroundCard)),
            cornerRadius = ODSCorners(DSVariables.radiusMedium),
            padding = ODSPadding(all = DSVariables.spacingComponent5)
        ) {
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // Top row: Today's Screen Time and Daily Goal
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    ODSColumn(
                        modifier = Modifier.weight(1f),
                        gap = DSVariables.spacingComponent1
                    ) {
                        ODSText(
                            text = stringResource(R.string.todays_total),
                            style = DSTextStyles.bodyMRegular,
                            color = activeScheme.basicTextRecessive
                        )
                        ODSText(
                            text = stringResource(R.string.sample_daily_time),
                            style = DSTextStyles.subtitle,
                            color = activeScheme.basicText
                        )
                    }

                    ODSColumn(
                        gap = DSVariables.spacingComponent1,
                        horizontalAlignment = Alignment.End
                    ) {
                        ODSText(
                            text = stringResource(R.string.daily_goal),
                            style = DSTextStyles.bodyMRegular,
                            color = activeScheme.basicText
                        )
                        ODSText(
                            text = "6h",
                            style = DSTextStyles.subtitle,
                            color = activeScheme.basicText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(DSVariables.spacingComponent2))

                // Bottom row: Yearly Usage and Customize link
                ODSRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ODSColumn(
                        gap = DSVariables.spacingComponent1
                    ) {
                        ODSText(
                            text = stringResource(R.string.yearly_usage_stats_title, year),
                            style = DSTextStyles.bodySRegular,
                            color = activeScheme.basicText
                        )
                        ODSText(
                            text = stringResource(R.string.sample_yearly_time),
                            style = DSTextStyles.bodyMBold,
                            color = activeScheme.basicText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorPicker(
    colors: List<ColorOption>,
    selectedColorOption: ColorOption,
    onColorSelected: (ColorOption) -> Unit
) {
    ODSColumn(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        gap = DSVariables.spacingComponent4
    ) {
        colors.chunked(4).forEach { rowColors ->
            ODSRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowColors.forEach { colorOption ->
                    ColorOptionItem(
                        colorOption = colorOption,
                        isSelected = colorOption.id == selectedColorOption.id,
                        onClick = { onColorSelected(colorOption) },
                        scheme = neutralScheme
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorOptionItem(
    colorOption: ColorOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    scheme: ODSTheme,
) {
    ODSBox(
        cornerRadius = ODSCorners(DSVariables.radiusFull),
        modifier = Modifier
            .size(58.dp)
            .clickable {
                onClick.invoke()
            },
        border = if (isSelected) {
            ODSBorder(
                3.dp, colorList = listOf(ODSColorModel(scheme.basicStroke))
            )
        } else {
            null
        },
        background = listOf(ODSColorModel(colorOption.scheme.basicBackgroundCard)),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            ODSIcon(
                iconModel = ODSIconModel(
                    imageVector = Icons.Outlined.Check,
                    tint = colorOption.scheme.basicText,
                    contentDescription = "Selected"
                ),
                width = DSVariables.spacingComponent6,
                height = DSVariables.spacingComponent6
            )
        }
    }
}

