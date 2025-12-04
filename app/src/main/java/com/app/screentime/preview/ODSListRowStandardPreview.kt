package com.app.screentime.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant

@Preview(showBackground = true)
@Composable
fun ODSListRowStandardPreview() {
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
            // Standard - Label Only
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Standard List Row",
                    label = "Label Only"
                )
            )

            // Standard - Label with Description
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Standard List Row",
                    label = "Label with Description",
                    descriptionText = "This is a description text below the label"
                )
            )

            // Standard - Label with Description Title
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Standard List Row",
                    label = "Label with Description Title",
                    descriptionTitle = "Description Title",
                    descriptionText = "This is a description text with a title"
                )
            )

            // Standard - Full Content
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Full Content Example",
                    label = "Standard List Row",
                    descriptionTitle = "Description Title",
                    descriptionText = "This is a complete example with all text fields",
                    showDescriptionTitle = true
                )
            )

            // Standard - Without Description Title
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "Without Description Title",
                    label = "Standard List Row",
                    descriptionText = "This description has no title",
                    showDescriptionTitle = false
                )
            )

            // Icon - Label Only
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Icon List Row",
                    label = "Label Only",
                    icon = ODSIconModel(imageVector = Icons.Default.Settings)
                )
            )

            // Icon - Label with Description
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Icon List Row",
                    label = "Label with Description",
                    descriptionText = "This row has an icon and description",
                    icon = ODSIconModel(imageVector = Icons.Default.Info)
                )
            )

            // Icon - Full Content
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Full Content with Icon",
                    label = "Icon List Row",
                    descriptionTitle = "Description Title",
                    descriptionText = "This is a complete example with icon and all text fields",
                    icon = ODSIconModel(imageVector = Icons.Default.Star),
                    showDescriptionTitle = true
                )
            )

            // Icon - Different Icons
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Notifications",
                    label = "Notification Icon",
                    descriptionText = "This row uses a notification icon",
                    icon = ODSIconModel(imageVector = Icons.Default.Notifications)
                )
            )

            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Home",
                    label = "Home Icon",
                    descriptionText = "This row uses a home icon",
                    icon = ODSIconModel(imageVector = Icons.Default.Home)
                )
            )

            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Person",
                    label = "Person Icon",
                    descriptionText = "This row uses a person icon",
                    icon = ODSIconModel(imageVector = Icons.Default.Person)
                )
            )

            // Image - Label Only
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.IMAGE,
                    labelText = "Image List Row",
                    label = "Label Only",
                    image = ODSImageModel(
                        imageVector = Icons.Default.Image
                    )
                )
            )

            // Image - Label with Description
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.IMAGE,
                    labelText = "Image List Row",
                    label = "Label with Description",
                    descriptionText = "This row has an image and description",
                    image = ODSImageModel(
                        imageVector = Icons.Default.Photo
                    )
                )
            )

            // Image - Full Content
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.IMAGE,
                    labelText = "Full Content with Image",
                    label = "Image List Row",
                    descriptionTitle = "Description Title",
                    descriptionText = "This is a complete example with image and all text fields",
                    image = ODSImageModel(
                        imageVector = Icons.Default.PhotoLibrary
                    ),
                    showDescriptionTitle = true
                )
            )

            // Long Text Examples
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.STANDARD,
                    labelText = "This is a very long label text that might wrap to multiple lines if the content is too long",
                    label = "Long Text Example",
                    descriptionText = "This is also a very long description text that demonstrates how the component handles longer content that might wrap to multiple lines"
                )
            )

            // Icon with Long Text
            ODSListRowStandard(
                modifier = Modifier.fillMaxWidth(),
                scheme = neutralScheme,
                props = ODSListRowStandardProps(
                    variant = ODSListRowStandardVariant.ICON,
                    labelText = "Long label text with icon that wraps",
                    label = "Long Text with Icon",
                    descriptionText = "Long description text that demonstrates wrapping behavior",
                    icon = ODSIconModel(imageVector = Icons.Default.Description)
                )
            )
        }
    }
}

