package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.cardengagement.ODSCardEngagement
import com.telekom.odsystem.atoms.cardengagement.ODSCardEngagementProps
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme

@Preview(showBackground = true)
@Composable
fun ODSCardEngagementPreview() {
    ODSBox(
        modifier = Modifier,
        background = listOf(ODSColorModel(neutralScheme.basicBackground))
    ) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Basic Card Engagement - Label only
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "Challenge Card"
                ),
                onClick = {}
            )

            // Card Engagement - With Image
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "Reduce Screen Time Challenge",
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    )
                ),
                onClick = {}
            )

            // Card Engagement - Long Label
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "30-Day Digital Detox Challenge - Reduce your daily screen time by 30% and earn rewards",
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    )
                ),
                onClick = {}
            )

            // Card Engagement - Challenge Example 1
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "Daily Screen Time Limit",
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    )
                ),
                onClick = {}
            )

            // Card Engagement - Challenge Example 2
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "Weekly Focus Challenge",
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    )
                ),
                onClick = {}
            )

            // Card Engagement - Challenge Example 3
            ODSCardEngagement(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSCardEngagementProps(
                    label = "App Blocking Master",
                    image = ODSImageModel(
                        drawableRes = android.R.drawable.ic_menu_gallery
                    )
                ),
                onClick = {}
            )
        }
    }
}

