package com.telekom.odsystem.slots.contentpanel

import ODSContentPanelProps
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.carouseltimer.ODSCarouselTimer
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import toODSButtonProps

/**
 * Displays a content panel with a title, text, and action buttons. Supports a carousel timer for multiple content segments.
 *
 * @param modifier Modifier for this composable.
 * @param scheme ODSTheme for styling.
 * @param props Configuration for the content panel.
 * @param titleSlot Optional composable for the title.
 * @param onClick Action for the main button.
 * @param onPreviousClick Action for the previous button (if applicable).
 * @param onNextClick Action for the next button (if applicable).
 * @param onPlayPauseClick Action for the play/pause button (if applicable).
 * @param segmentCompleted Callback when a carousel segment completes, providing the segment index.
 */
@Suppress("LongMethod", "MultiLineIfElse")
@Composable
fun ODSContentPanel(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSContentPanelProps = ODSContentPanelProps(),
    titleSlot: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPlayPauseClick: () -> Unit = {},
    segmentCompleted: (Int) -> Unit = {}
) {

    val style = ODSContentPanelStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement
    ) {
        ODSColumn(
            modifier = Modifier.fillMaxWidth(),
            gap = style.cardContentGap,
            verticalAlignment = style.cardContentVerticalAlignment,
            horizontalAlignment = style.cardContentHorizontalAlignment,
            verticalArrangement = style.cardContentVerticalArrangement
        ) {
            props.carouselTimerProps?.let {
                ODSCarouselTimer(
                    scheme = scheme,
                    props = it,
                    isInProgressElementIndex = props.isInProgressElementIndex,
                    isRunning = props.isRunning,
                    segmentCompleted = segmentCompleted
                )
            }
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = style.contentContainerGap,
                verticalAlignment = style.contentContainerVerticalAlignment,
                horizontalAlignment = style.contentContainerHorizontalAlignment,
                verticalArrangement = style.contentContainerVerticalArrangement
            ) {
                titleSlot?.invoke()
                if (!props.segmentText.isNullOrEmpty()) {
                    ODSText(
                        modifier = Modifier.fillMaxWidth(),
                        text = props.segmentText,
                        style = style.segmentTextStyle,
                        color = style.segmentTextColor,
                        textAlign = style.segmentTextTextAlign,
                        minHeight = style.segmentTextMinHeight
                    )
                }
            }
        }
        ODSRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = style.actionButtonsVerticalAlignment,
            horizontalArrangement = style.actionButtonsHorizontalArrangement
        ) {
            props.buttonProps?.let {
                ODSButton(scheme = scheme, props = it.toODSButtonProps(), onClick = onClick)
            }
            ODSRow(
                horizontalAlignment = style.controlsHorizontalAlignment,
                verticalAlignment = style.controlsVerticalAlignment,
                horizontalArrangement = style.controlsHorizontalArrangement
            ) {
                if (props.carouselTimerProps?.segmentsDuration.orEmpty().size > 1) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                drawableRes = R.drawable.navigation_left_type_standard_size_standard,
                                contentDescription = stringResource(R.string.semantics_previous)
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            size = ODSButtonSize.SMALL,
                            variant = ODSButtonVariant.GHOST
                        ),
                        onClick = onPreviousClick
                    )
                }
                if (props.carouselTimerProps?.segmentsDuration.orEmpty().size > 1) {
                    ODSButton(
                        scheme = scheme,
                        props = ODSButtonProps(
                            buttonIcon = ODSIconModel(
                                drawableRes = R.drawable.navigation_right_type_standard_size_standard,
                                contentDescription = stringResource(R.string.semantics_next)
                            ),
                            buttonType = ODSButtonButtonType.ICON_ONLY,
                            size = ODSButtonSize.SMALL,
                            variant = ODSButtonVariant.GHOST
                        ),
                        onClick = onNextClick
                    )
                }
                ODSButton(
                    scheme = scheme,
                    props = ODSButtonProps(
                        buttonIcon = ODSIconModel(
                            drawableRes = if (props.isRunning) R.drawable.pause_type_standard_size_standard else R.drawable.play_type_standard_size_standard,
                            contentDescription = if (props.isRunning) {
                                stringResource(R.string.semantics_pause)
                            } else {
                                stringResource(R.string.semantics_play)
                            }
                        ),
                        buttonType = ODSButtonButtonType.ICON_ONLY,
                        size = ODSButtonSize.SMALL,
                        variant = ODSButtonVariant.OUTLINE
                    ),
                    onClick = onPlayPauseClick
                )
            }
        }
    }
}
