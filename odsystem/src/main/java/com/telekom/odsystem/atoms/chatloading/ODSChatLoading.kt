package com.telekom.odsystem.atoms.chatloading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Composable function that displays a loading indicator for chat messages.
 * It uses an [ODSSkeleton] with a small variant to indicate loading.
 *
 * @param modifier The modifier to be applied to the component.
 * @param scheme The [ODSTheme] to be used for styling the component. Defaults to [neutralScheme].
 */
@Composable
fun ODSChatLoading(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme,
) {

    val style = ODSChatLoadingStyle().getStyle(scheme = scheme)

    ODSColumn(
        modifier = modifier,
        padding = style.padding,
        verticalAlignment = style.verticalAlignment,
        horizontalAlignment = style.horizontalAlignment,
        verticalArrangement = style.verticalArrangement,
        width = style.width,
        height = style.height
    ) {
        ODSSkeleton(
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.SMALL
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
