package com.telekom.odsystem.slots.dialogbottomsheetpreferredactions

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSWrap
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Composable
/**
 * ODSDialogBottomSheetPreferredActions composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onTertiaryButtonClick Callback triggered when action occurs.
 * @param onSecondaryButtonClick Callback triggered when action occurs.
 * @param onMainButtonClick Callback triggered when action occurs.
 */
fun ODSDialogBottomSheetPreferredActions(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDialogBottomSheetPreferredActionsProps = ODSDialogBottomSheetPreferredActionsProps(),
    onTertiaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onMainButtonClick: () -> Unit = {}
) {

    val style = ODSDialogBottomSheetPreferredActionsStyle().getStyle(props = props)
    when (props.variant) {
        ODSDialogBottomSheetPreferredActionsVariant.SIDE_BY_SIDE -> {
            ODSSideBySide(
                modifier = modifier,
                scheme = scheme,
                props = props,
                style = style,
                onTertiaryButtonClick = onTertiaryButtonClick,
                onSecondaryButtonClick = onSecondaryButtonClick,
                onMainButtonClick = onMainButtonClick
            )
        }

        ODSDialogBottomSheetPreferredActionsVariant.SIDE_BY_SIDE_FILL -> {
            ODSSideBySideFill(
                modifier = modifier,
                scheme = scheme,
                props = props,
                style = style,
                onTertiaryButtonClick = onTertiaryButtonClick,
                onSecondaryButtonClick = onSecondaryButtonClick,
                onMainButtonClick = onMainButtonClick
            )
        }

        ODSDialogBottomSheetPreferredActionsVariant.STACKED -> {
            ODSStacked(
                modifier = modifier,
                scheme = scheme,
                props = props,
                style = style,
                onTertiaryButtonClick = onTertiaryButtonClick,
                onSecondaryButtonClick = onSecondaryButtonClick,
                onMainButtonClick = onMainButtonClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ODSSideBySide(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSDialogBottomSheetPreferredActionsProps,
    style: ODSDialogBottomSheetPreferredActionsStyle,
    onTertiaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onMainButtonClick: () -> Unit = {}
) {
    ODSWrap(
        modifier = modifier.fillMaxWidth(),
        horizontalGap = style.gap,
        verticalGap = style.gap,
        verticalArrangement = style.verticalArrangement,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.tertiaryActionProps?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                onClick = onTertiaryButtonClick
            )
        }

        props.secondaryActionProps?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                onClick = onSecondaryButtonClick
            )
        }

        props.mainActionProps?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                onClick = onMainButtonClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ODSSideBySideFill(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSDialogBottomSheetPreferredActionsProps,
    style: ODSDialogBottomSheetPreferredActionsStyle,
    onTertiaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onMainButtonClick: () -> Unit = {}
) {
    ODSWrap(
        modifier = modifier.fillMaxWidth(),
        horizontalGap = style.gap,
        verticalGap = style.gap,
        verticalArrangement = style.verticalArrangement,
        horizontalArrangement = style.horizontalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.tertiaryActionProps?.let {
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = it,
                onClick = onTertiaryButtonClick
            )
        }

        props.secondaryActionProps?.let {
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = it,
                onClick = onSecondaryButtonClick
            )
        }

        props.mainActionProps?.let {
            ODSButton(
                modifier = Modifier.weight(1f),
                scheme = scheme,
                props = it,
                onClick = onMainButtonClick
            )
        }
    }
}

@Composable
private fun ODSStacked(
    modifier: Modifier,
    scheme: ODSTheme,
    props: ODSDialogBottomSheetPreferredActionsProps,
    style: ODSDialogBottomSheetPreferredActionsStyle,
    onTertiaryButtonClick: () -> Unit = {},
    onSecondaryButtonClick: () -> Unit = {},
    onMainButtonClick: () -> Unit = {}
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
    ) {
        props.tertiaryActionProps?.let {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = it,
                onClick = onTertiaryButtonClick
            )
        }

        props.secondaryActionProps?.let {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = it,
                onClick = onSecondaryButtonClick
            )
        }

        props.mainActionProps?.let {
            ODSButton(
                modifier = Modifier.fillMaxWidth(),
                scheme = scheme,
                props = it,
                onClick = onMainButtonClick
            )
        }
    }
}
