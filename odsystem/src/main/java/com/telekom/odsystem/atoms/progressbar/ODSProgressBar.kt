package com.telekom.odsystem.atoms.progressbar

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrack
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackMode
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackProps
import com.telekom.odsystem.atoms.dataprogresstrack.ODSDataProgressTrackSize
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSProgressBar composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 */
@Composable
fun ODSProgressBar(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSProgressBarProps = ODSProgressBarProps(),
) {
    val style = ODSProgressBarStyle().getStyle(scheme = scheme, props = props)
    ODSProgressBarContainer(
        style = style,
        props = props,
        scheme = scheme,
        modifier = modifier
    )
}

@Composable
fun ODSProgressBarContainer(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
    scheme: ODSTheme,
    modifier: Modifier,
) {
    ODSColumn(
        gap = style.gap,
        modifier = modifier.sizeWithinBounds(
            minWidth = style.minWidth ?: Dp.Unspecified,
            minHeight = Dp.Unspecified
        ),
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment
    ) {
        ODSProgressBarDataContainer(style = style, props = props, scheme = scheme)
        if (props.helperText != null) {
            ODSText(
                modifier = Modifier.clearAndSetSemantics { },
                text = props.helperText,
                style = style.helperTextTextStyle,
                color = style.helperTextColor,
                textAlign = style.helperTextTextAlign
            )
        }
    }
}

@Composable
fun ODSProgressBarDataContainer(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
    scheme: ODSTheme,
) {
    ODSRow(
        gap = style.dataGap,
        horizontalArrangement = style.dataHorizontalArrangement,
        horizontalAlignment = style.dataHorizontalAlignment,
        verticalAlignment = style.dataVerticalAlignment,
        clipContent = style.dataClipContent != false
    ) {
        ODSProgressBarMainDataContainer(
            style = style,
            props = props,
            scheme = scheme,
            modifier = Modifier.weight(1f)
        )
        if (props.extraDataProgress != null) {
            ODSExtraDataProgressBar(
                props = props,
                style = style,
                scheme = scheme
            )
        }
    }
}

@Composable
fun ODSProgressBarMainDataContainer(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
    scheme: ODSTheme,
    modifier: Modifier,
) {
    ODSColumn(
        gap = style.mainDataGap,
        modifier = modifier.applySemantics(props, LocalContext.current, props.mainDataProgress),
        verticalArrangement = style.mainDataVerticalArrangement,
        verticalAlignment = style.mainDataVerticalAlignment,
        horizontalAlignment = style.mainDataHorizontalAlignment
    ) {
        ODSMainDataProgressBar(
            style = style,
            props = props,
            scheme = scheme
        )
    }
}

@Composable
fun ODSMainDataProgressBar(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
    scheme: ODSTheme,
) {
    ODSMainDataLabelCounterAndIconContainer(style, props)
    ODSColumn(
        verticalAlignment = style.dataProgressTrackContainerVerticalAlignment,
        horizontalAlignment = style.dataProgressTrackContainerHorizontalAlignment,
        verticalArrangement = style.dataProgressTrackContainerVerticalArrangement,
    ) {
        ODSDataProgressTrack(
            modifier = Modifier.clearAndSetSemantics { /*Handle semantics in ODSProgressBarMainDataContainer*/ },
            scheme = scheme,
            props = ODSDataProgressTrackProps(
                progress = props.mainDataProgress,
                mode = getDataProgressType(props),
                size = getDataProgressSize(props)
            )
        )
    }
}

@Composable
fun ODSMainDataLabelCounterAndIconContainer(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
) {
    val context = LocalContext.current
    ODSRow(
        gap = style.mainDataTextGap,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = style.mainDataTextHorizontalArrangement,
        verticalAlignment = style.mainDataTextVerticalAlignment,
        horizontalAlignment = style.mainDataTextHorizontalAlignment
    ) {
        if (props.label != null) {
            ODSText(
                modifier = Modifier
                    .weight(1f)
                    .clearAndSetSemantics { },
                text = props.label,
                style = style.labelTextTextStyle,
                color = style.labelTextColor,
                textAlign = style.labelTextTextAlign,
                overflow = style.labelTextTextOverflow
            )
        }
        if (props.counterText != null) {
            ODSText(
                modifier = Modifier.weight(1f),
                text = props.counterText,
                style = style.counterTextTextStyle,
                color = style.counterTextColor,
                textAlign = style.counterTextTextAlign,
                overflow = style.counterTextTextOverflow
            )
        }

        when (props.mode) {
            ODSProgressBarMode.SUCCESS -> {
                ODSIcon(
                    width = style.dataSuccessWidth,
                    height = style.dataSuccessHeight,
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.success_type_standard,
                        contentDescription = context.getString(R.string.semantic_success_icon)
                    ),
                    tint = style.dataSuccessColor?.getColor()
                )
            }

            ODSProgressBarMode.ERROR -> {
                ODSIcon(
                    width = style.dataWarningWidth,
                    height = style.dataWarningHeight,
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.warning_type_standard,
                        contentDescription = context.getString(R.string.semantic_warning_icon)
                    ),
                    tint = style.dataWarningColor?.getColor()
                )
            }

            else -> { /*Nothing*/
            }
        }
    }
}

@Composable
fun ODSExtraDataProgressBar(
    props: ODSProgressBarProps,
    style: ODSProgressBarStyle,
    scheme: ODSTheme,
) {
    ODSColumn(
        modifier = Modifier.applySemantics(props, LocalContext.current, props.extraDataProgress),
        gap = style.extraDataGap,
        verticalAlignment = style.extraDataVerticalAlignment,
        verticalArrangement = style.extraDataVerticalArrangement,
        horizontalAlignment = style.extraDataHorizontalAlignment,
        width = style.extraDataWidth
    ) {
        ODSExtraDataTextAndIconContainer(style, props)
        ODSColumn(
            verticalAlignment = style.extraDataProgressTrackContainerVerticalAlignment,
            horizontalAlignment = style.extraDataProgressTrackContainerHorizontalAlignment,
            verticalArrangement = style.extraDataProgressTrackContainerVerticalArrangement,
        ) {
            ODSDataProgressTrack(
                modifier = Modifier.clearAndSetSemantics {},
                scheme = scheme,
                props = ODSDataProgressTrackProps(
                    progress = props.extraDataProgress,
                    mode = getDataProgressType(props),
                    size = getDataProgressSize(props)
                )
            )
        }
    }
}

@Composable
fun ODSExtraDataTextAndIconContainer(
    style: ODSProgressBarStyle,
    props: ODSProgressBarProps,
) {
    val context = LocalContext.current
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = style.extraDataTextHorizontalAlignment,
        verticalAlignment = style.extraDataTextVerticalAlignment,
        horizontalArrangement = style.extraDataTextHorizontalArrangement
    ) {
        if (props.extraDataText != null) {
            ODSText(
                modifier = Modifier.weight(1f),
                text = props.extraDataText ?: "",
                style = style.extraDataTextTextStyle,
                color = style.extraDataTextColor,
                textAlign = style.extraDataTextTextAlign,
                overflow = style.extraDataTextTextOverflow
            )
        }

        when (props.mode) {
            ODSProgressBarMode.SUCCESS -> {
                ODSIcon(
                    width = style.extraDataSuccessWidth,
                    height = style.extraDataSuccessHeight,
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.success_type_standard,
                        contentDescription = context.getString(R.string.semantic_success_icon)
                    ),
                    tint = style.extraDataSuccessColor?.getColor()
                )
            }

            ODSProgressBarMode.ERROR -> {
                ODSIcon(
                    width = style.extraDataWarningWidth,
                    height = style.extraDataWarningHeight,
                    iconModel = ODSIconModel(
                        drawableRes = R.drawable.warning_type_standard,
                        contentDescription = context.getString(R.string.semantic_warning_icon)
                    ),
                    tint = style.extraDataWarningColor?.getColor()
                )
            }

            else -> { /*Nothing*/
            }
        }
    }
}

private fun getDataProgressType(props: ODSProgressBarProps): ODSDataProgressTrackMode {
    return if (props.disabled) {
        ODSDataProgressTrackMode.DISABLED
    } else if (props.mode == ODSProgressBarMode.SUCCESS) {
        ODSDataProgressTrackMode.SUCCESS
    } else if (props.mode == ODSProgressBarMode.ERROR) {
        ODSDataProgressTrackMode.ERROR
    } else {
        ODSDataProgressTrackMode.STANDARD
    }
}

private fun getDataProgressSize(props: ODSProgressBarProps): ODSDataProgressTrackSize {
    return when (props.size) {
        ODSProgressBarSize.SMALL -> ODSDataProgressTrackSize.SMALL
        ODSProgressBarSize.MEDIUM -> ODSDataProgressTrackSize.MEDIUM
        ODSProgressBarSize.LARGE -> ODSDataProgressTrackSize.LARGE
    }
}

private fun Modifier.applySemantics(
    props: ODSProgressBarProps,
    context: Context,
    current: Float?,
): Modifier {
    val isError = props.mode == ODSProgressBarMode.ERROR && !props.disabled
    val isSuccess = props.mode == ODSProgressBarMode.SUCCESS && !props.disabled
    val helperText = when {
        isError -> context.getString(R.string.semantic_error, props.helperText)
        isSuccess -> context.getString(R.string.semantic_success, props.helperText)
        else -> props.helperText ?: ""
    }
    return this.semantics(mergeDescendants = true) {
        this.contentDescription = "$helperText \n ${props.label ?: ""}"
        this.progressBarRangeInfo = ProgressBarRangeInfo(
            current = current ?: 0f,
            range = 0f..1f
        )
        if (props.disabled) {
            this.disabled()
        }
    }
}
