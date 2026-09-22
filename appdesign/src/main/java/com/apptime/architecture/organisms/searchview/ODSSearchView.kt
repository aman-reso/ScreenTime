package com.telekom.odsystem.organisms.searchview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.telekom.odsystem.R
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonSize
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.searchbar.ODSSearchBar
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Composable for rendering an ODS Search View.
 *
 * Combines a search input with an optional results slot and navigation controls.
 * Provides full customization over input behavior, keyboard interaction, and user events.
 *
 * @param modifier Modifier for layout and styling.
 * @param scheme Color scheme used for theming. Defaults to [neutralScheme].
 * @param props Properties that define the search view's behavior and appearance.
 * @param resultListSlot Optional composable slot for displaying search results.
 * @param keyboardOptions Configuration for the software keyboard (type, IME action, etc.).
 * @param keyboardActions Defines actions triggered by the keyboard's IME buttons.
 * @param visualTransformation `VisualTransformation` for the input field.
 * @param focusRequester `FocusRequester` for the search bar.
 * @param onSearchValueChange Callback when the input text changes.
 * @param onButtonClick Callback when the end-button (e.g., search or clear) is clicked.
 * @param onBackButtonClick Callback when the back button is pressed, often to dismiss the view.
 * @param onFocusChange Callback when the input field gains or loses focus.
 */
@Composable
fun ODSSearchView(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSSearchViewProps = ODSSearchViewProps(),
    resultListSlot: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onSearchValueChange: (String) -> Unit,
    onButtonClick: (String) -> Unit = {},
    onBackButtonClick: () -> Unit = {},
    onFocusChange: (FocusState) -> Unit = { },
) {
    val style = ODSSearchViewStyle().getStyle(scheme = scheme)

    ODSColumn(
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        modifier = modifier.fillMaxWidth()
    ) {
        ODSSearchContainer(
            scheme = scheme,
            style = style,
            props = props,
            onBackButtonClick = onBackButtonClick,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            focusRequester = focusRequester,
            onValueChange = onSearchValueChange,
            onButtonClick = onButtonClick,
            onFocusChange = onFocusChange
        )

        resultListSlot?.let {
            ODSResultListContainer(style = style, resultListSlot = it)
        }
    }
}

@Composable
private fun ODSSearchContainer(
    scheme: ODSTheme,
    style: ODSSearchViewStyle,
    props: ODSSearchViewProps,
    onBackButtonClick: () -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onButtonClick: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
) {
    ODSRow(
        horizontalArrangement = style.searchContainerHorizontalArrangement,
        verticalAlignment = style.searchContainerVerticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        AnimatedVisibility(
            visible = props.showBackButton
        ) {
            ODSButton(
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(drawableRes = R.drawable.arrow_left_type_standard),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST
                ),
                scheme = scheme,
                onClick = onBackButtonClick
            )
        }

        props.searchBarProps?.let {
            ODSSearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged(onFocusChange),
                scheme = scheme,
                props = it,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                onButtonClick = onButtonClick,
                onValueChange = onValueChange,
            )
        }
    }
}

@Composable
private fun ODSResultListContainer(
    style: ODSSearchViewStyle,
    resultListSlot: @Composable () -> Unit
) {
    ODSColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = style.resultContainerVerticalArrangement,
        verticalAlignment = style.resultContainerVerticalAlignment,
        horizontalAlignment = style.resultContainerHorizontalAlignment
    ) {
        resultListSlot()
    }
}
