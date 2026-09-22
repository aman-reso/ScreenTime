package com.telekom.odsystem.molecules.fileupload

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.progressbar.ODSProgressBar
import com.telekom.odsystem.atoms.thumbnail.ODSThumbnail
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-27 (v1.32.3) - uid: 1dc2ba06
 * Figma link: https://figma.com/design/HS4hbbga3PU294sBjZBsi4/ODS_Content-Data-Components_Exploration?node-id=7904-10955
 */

/**
 * ODS File Upload component.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The ODSTheme for styling.
 * @param props The ODSFileUploadProps to configure the component.
 * @param onClick Callback for the action button click.
 */
@Composable
fun ODSFileUpload(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
    props: ODSFileUploadProps = ODSFileUploadProps(),
    onClick: () -> Unit = {}
) {
    val style = ODSFileUploadStyle().getStyle(scheme = scheme, props = props)

    ODSFileUploadContainer(
        modifier = modifier,
        style = style,
        props = props,
        scheme = scheme,
        onClick = onClick
    )
}

@Composable
private fun ODSFileUploadContainer(
    modifier: Modifier,
    style: ODSFileUploadStyle,
    props: ODSFileUploadProps,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        padding = style.padding,
        cornerRadius = style.cornerRadius,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
        background = style.background
    ) {
        ODSUploadContentContainer(
            style = style,
            props = props,
            scheme = scheme,
            onClick = onClick
        )
    }
}

@Composable
private fun ODSUploadContentContainer(
    style: ODSFileUploadStyle,
    props: ODSFileUploadProps,
    scheme: ODSTheme,
    onClick: () -> Unit
) {
    ODSRow(
        modifier = Modifier.fillMaxWidth(),
        gap = style.uploadContentGap,
        horizontalAlignment = style.uploadContentHorizontalAlignment,
        verticalAlignment = style.uploadContentVerticalAlignment,
        horizontalArrangement = style.uploadContentHorizontalArrangement
    ) {
        ODSIconProgressContainer(
            style = style,
            props = props,
            scheme = scheme
        )
        props.actionButtonProps?.let {
            ODSButton(
                scheme = scheme,
                props = it,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun RowScope.ODSIconProgressContainer(
    style: ODSFileUploadStyle,
    props: ODSFileUploadProps,
    scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier.weight(1f),
        gap = style.iconProgressGap,
        horizontalAlignment = style.iconProgressHorizontalAlignment,
        verticalAlignment = style.iconProgressVerticalAlignment,
        horizontalArrangement = style.iconProgressHorizontalArrangement
    ) {
        props.thumbnailProps?.let {
            ODSThumbnail(
                scheme = scheme,
                props = it
            )
        }
        if (props.type == ODSFileUploadType.EXTENDED) {
            props.progressBarProps?.let {
                ODSProgressBar(scheme = scheme, props = it)
            }
        }
        if (props.type == ODSFileUploadType.SIMPLE) {
            ODSTextLoadContainer(
                style = style,
                props = props,
                scheme = scheme
            )
        }
    }
}

@Composable
private fun RowScope.ODSTextLoadContainer(
    style: ODSFileUploadStyle,
    props: ODSFileUploadProps,
    scheme: ODSTheme
) {
    ODSRow(
        modifier = Modifier.weight(1f),
        verticalAlignment = style.textLoadVerticalAlignment,
        horizontalArrangement = style.textLoadHorizontalArrangement
    ) {
        if (!props.label.isNullOrEmpty()) {
            ODSText(
                text = props.label,
                style = style.filenameStyle,
                color = style.filenameColor,
                textAlign = style.filenameTextAlign
            )
        }
        props.loadingSpinnerProps?.let {
            ODSLoadingSpinner(
                scheme = scheme,
                props = it
            )
        }
    }
}
