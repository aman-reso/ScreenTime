package com.telekom.odsystem.slots.dialogpreferredcontent

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImage
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.textarea.ODSTextArea
import com.telekom.odsystem.foundations.sizeWithinBounds
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * ODSDialogPreferredContent composable.
 *
 * @param modifier Modifier applied to this component.
 * @param scheme Color scheme used for theming.
 * @param props Visual configuration for the component.
 * @param onValueChange Callback triggered when action occurs.
 * @param onFocusChange Callback triggered when action occurs.
 * @param keyboardOptions Parameter for customization.
 * @param keyboardActions Parameter for customization.
 */
@Composable
fun ODSDialogPreferredContent(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSDialogPreferredContentProps = ODSDialogPreferredContentProps(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    onValueChange: (String) -> Unit = {},
    onFocusChange: (FocusState) -> Unit = { },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {

    val style = ODSDialogPreferredContentStyle().getStyle(scheme = scheme, props = props)

    ODSColumn(
        modifier = modifier,
        gap = style.gap,
        verticalArrangement = style.verticalArrangement,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
    ) {
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE) {
            ODSGeneralWithImage(
                style = style,
                props = props
            )
        }

        if (!props.subtitle.isNullOrEmpty() || !props.content.isNullOrEmpty()) {
            ODSBodyContent(
                style = style,
                props = props
            )
        }

        if (props.variant == ODSDialogPreferredContentVariant.TEXT_AREA_INPUT) {
            props.textAreaProps?.let {
                ODSTextArea(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = focusRequester)
                        .onFocusChanged(onFocusChange),
                    scheme = scheme,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    onValueChange = onValueChange,
                    props = it
                )
            }
        }
    }
}

@Composable
private fun ODSGeneralWithImage(
    style: ODSDialogPreferredContentStyle,
    props: ODSDialogPreferredContentProps
) {
    props.imageModel?.let {
        ODSRow(
            modifier = Modifier
                .sizeWithinBounds(
                    maxHeight = style.imageContainerMaxHeight ?: Dp.Unspecified
                )
                .fillMaxWidth(),
            clipContent = style.imageContainerClipContent != false,
            horizontalArrangement = style.imageContainerHorizontalArrangement,
            horizontalAlignment = style.imageContainerHorizontalAlignment,
            verticalAlignment = style.imageContainerVerticalAlignment,
        ) {
            ODSImage(
                modifier = Modifier.fillMaxWidth(),
                imageModel = props.imageModel,
                contentScale = style.imageObjectFit ?: ContentScale.Crop
            )
        }
    }

    ODSColumn(
        gap = style.labelHeadingGap,
        verticalArrangement = style.labelHeadingVerticalArrangement,
        verticalAlignment = style.labelHeadingVerticalAlignment,
        horizontalAlignment = style.labelHeadingHorizontalAlignment
    ) {
        if (!props.heading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.heading,
                style = style.headerTextStyle,
                color = style.headerColor,
                textAlign = style.headerTextAlign
            )
        }
    }
}

@Composable
private fun ODSBodyContent(
    style: ODSDialogPreferredContentStyle,
    props: ODSDialogPreferredContentProps
) {
    ODSColumn(
        gap = style.bodyContentGap,
        verticalArrangement = style.bodyContentVerticalArrangement,
        verticalAlignment = style.bodyContentVerticalAlignment,
        horizontalAlignment = style.bodyContentHorizontalAlignment
    ) {
        if (props.variant == ODSDialogPreferredContentVariant.GENERAL_WITH_IMAGE && !props.subtitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.subtitle,
                style = style.subtitleTextStyle,
                color = style.subtitleColor,
                textAlign = style.subtitleTextAlign
            )
        }
        if (!props.content.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.content,
                style = style.bodyTextTextStyle,
                color = style.bodyTextColor,
                textAlign = style.bodyTextTextAlign
            )
        }
    }
}
