package com.telekom.odsystem.slots.bottomsheetheader

import androidx.compose.foundation.layout.RowScope
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
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-09-04 (v1.32.3) - uid: 5c52583b
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=17924-44
 */

/**
 * ODS BottomSheetHeader component.
 *
 * This component is used to display a header for a bottom sheet. It can display a title, subtitle, and a back arrow.
 * The size of the header can be either large or small.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The ODSTheme to be used for the component.
 * @param props The ODSBottomSheetHeaderProps to be used for the component.
 * @param onBackClicked The callback to be invoked when the back arrow is clicked.
 *
 */
@Composable
fun ODSBottomSheetHeader(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSBottomSheetHeaderProps = ODSBottomSheetHeaderProps(),
    onBackClicked: () -> Unit = {}
) {
    val style = ODSBottomSheetHeaderStyle().getStyle(scheme = scheme, props = props)

    ODSRow(
        modifier = modifier.fillMaxWidth(),
        padding = style.padding,
        horizontalAlignment = style.horizontalAlignment,
        verticalAlignment = style.verticalAlignment,
        horizontalArrangement = style.horizontalArrangement,
        minHeight = style.minHeight
    ) {
        if (props.showBackArrow) {
            ODSButton(
                scheme = scheme,
                props = ODSButtonProps(
                    buttonIcon = ODSIconModel(
                        drawableRes = R.drawable.left_condensed_type_standard_size_standard,
                        contentDescription = stringResource(R.string.semantic_navigation_back_icon)
                    ),
                    buttonType = ODSButtonButtonType.ICON_ONLY,
                    size = ODSButtonSize.SMALL,
                    variant = ODSButtonVariant.GHOST
                ),
                onClick = onBackClicked
            )
        }
        when (props.size) {
            ODSBottomSheetHeaderSize.LARGE -> ODSBottomSheetLargeHeaderContent(
                style = style,
                props = props
            )

            ODSBottomSheetHeaderSize.SMALL -> ODSBottomSheetSmallHeaderContent(
                style = style,
                props = props
            )
        }
    }
}

@Composable
private fun RowScope.ODSBottomSheetLargeHeaderContent(
    style: ODSBottomSheetHeaderStyle,
    props: ODSBottomSheetHeaderProps
) {
    ODSRow(
        modifier = Modifier.weight(1f),
        gap = style.textContainerGap,
        padding = style.textContainerPadding,
        horizontalAlignment = style.textContainerHorizontalAlignment,
        verticalAlignment = style.textContainerVerticalAlignment,
        horizontalArrangement = style.textContainerHorizontalArrangement
    ) {
        if (!props.largeHeading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.weight(1f),
                text = props.largeHeading,
                style = style.titleLabelStyle,
                color = style.titleLabelColor,
                textAlign = style.titleLabelTextAlign
            )
        }
    }
}

@Composable
private fun RowScope.ODSBottomSheetSmallHeaderContent(
    style: ODSBottomSheetHeaderStyle,
    props: ODSBottomSheetHeaderProps
) {
    ODSColumn(
        modifier = Modifier.weight(1f),
        gap = style.textContainerGap,
        padding = style.textContainerPadding,
        verticalAlignment = style.textContainerVerticalAlignment,
        horizontalAlignment = style.textContainerHorizontalAlignment,
        verticalArrangement = style.textContainerVerticalArrangement
    ) {
        if (!props.smallHeading.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.smallHeading,
                style = style.titleLabelStyle,
                color = style.titleLabelColor,
                textAlign = style.titleLabelTextAlign
            )
        }
        if (!props.subtitle.isNullOrEmpty()) {
            ODSText(
                modifier = Modifier.fillMaxWidth(),
                text = props.subtitle,
                style = style.subtitleLabelStyle,
                color = style.subtitleLabelColor,
                textAlign = style.subtitleLabelTextAlign
            )
        }
    }
}
