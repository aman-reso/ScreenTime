package com.app.screentime.ui.atom

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.skeleton.ODSSkeleton
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonProps
import com.telekom.odsystem.atoms.skeleton.ODSSkeletonVariant
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Shimmer loading component for app screens
 * Displays skeleton loaders in a column layout
 */
@Preview(showBackground = true)
@Composable
fun AppScreenShimmer(
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    ODSColumn(
        modifier = modifier
            .fillMaxSize(),
        background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        ODSSkeleton(
            modifier = Modifier
                .width(
                    88.dp
                )
                .height(
                    26.dp
                ),
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.SMALL
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .width(
                    160.dp
                )
                .height(
                    26.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.SMALL
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    48.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.MEDIUM
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DSVariables.spacingLayout10
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.LARGE
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DSVariables.spacingLayout10
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.LARGE
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    48.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.MEDIUM
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    72.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.MEDIUM
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    72.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.MEDIUM
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ODSSkeleton(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    72.dp
                ),
            scheme = scheme,
            props = ODSSkeletonProps(
                variant = ODSSkeletonVariant.MEDIUM
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

