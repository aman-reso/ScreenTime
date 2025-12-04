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
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasic
import com.telekom.odsystem.slots.cardcontentbasic.ODSCardContentBasicProps

@Preview(showBackground = true)
@Composable
fun ODSCardBasicPreview() {
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
            // ODSCardContentBasic Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // CardContentBasic - Label only
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                label = "Label"
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Heading only
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card Heading"
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Label and Heading
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                label = "Category",
                                heading = "Card Heading"
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Heading and Subtitle
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card Heading",
                                subtitle = "Card Subtitle"
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Heading and Content
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card Heading",
                                content = "This is the main content text of the card. It provides additional information about the card's purpose."
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Full content (Label, Heading, Subtitle, Content)
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                label = "Information",
                                heading = "Complete Card Example",
                                subtitle = "This is a subtitle",
                                content = "This is the main content text of the card. It provides detailed information about the card's purpose and can span multiple lines if needed."
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Content only
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                content = "This is a card with only content text. No heading, label, or subtitle is provided."
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Subtitle and Content
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                subtitle = "Why We Need This Permission",
                                content = "To provide you with accurate insights into your digital habits, ScreenTime requires access to your app usage statistics. This permission allows us to track which applications you use and how much time you spend on each one."
                            )
                        )
                    },
                    onClick = {}
                )

                // CardContentBasic - Long text example
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                label = "Important",
                                heading = "Long Text Card Example",
                                subtitle = "This demonstrates how the card handles long text content",
                                content = "This is a very long content text that demonstrates how the card component handles extensive text content. The text should wrap appropriately to fit within the bounds of the card. This ensures that all information is displayed correctly regardless of the content length. The card should maintain its visual integrity and readability even with longer text passages."
                            )
                        )
                    },
                    onClick = {}
                )
            }

            // ODSCardBasic with Action Slot Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // CardBasic with Action Button
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card with Action",
                                content = "This card includes an action button in the action slot."
                            )
                        )
                    },
                    actionSlot = {
                        ODSButton(
                            scheme = neutralScheme,
                            props = ODSButtonProps(
                                label = "Action",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )

                // CardBasic with Multiple Actions
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card with Multiple Actions",
                                content = "This card includes multiple action buttons."
                            )
                        )
                    },
                    actionSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSButton(
                                scheme = neutralScheme,
                                props = ODSButtonProps(
                                    label = "Primary Action",
                                    variant = ODSButtonVariant.PRIMARY
                                ),
                                onClick = {}
                            )
                            ODSButton(
                                scheme = neutralScheme,
                                props = ODSButtonProps(
                                    label = "Secondary Action",
                                    variant = ODSButtonVariant.OUTLINE
                                ),
                                onClick = {}
                            )
                        }
                    },
                    onClick = {}
                )

                // CardBasic with Icon in Action Slot
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Card with Icon Action",
                                content = "This card includes an icon in the action slot."
                            )
                        )
                    },
                    actionSlot = {
                        ODSIcon(
                            iconModel = ODSIconModel(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Navigate"
                            )
                        )
                    },
                    onClick = {}
                )
            }

            // ODSCardBasic Horizontal Layout Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // Horizontal CardBasic
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(isHorizontal = true),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                heading = "Horizontal Card",
                                content = "This card uses horizontal layout arrangement."
                            )
                        )
                    },
                    actionSlot = {
                        ODSButton(
                            scheme = neutralScheme,
                            props = ODSButtonProps(
                                label = "Action",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )

                // Horizontal CardBasic with Full Content
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(isHorizontal = true),
                    contentSlot = {
                        ODSCardContentBasic(
                            modifier = Modifier.fillMaxWidth(),
                            scheme = neutralScheme,
                            props = ODSCardContentBasicProps(
                                label = "Category",
                                heading = "Horizontal Layout",
                                subtitle = "Subtitle",
                                content = "This card demonstrates horizontal layout with all content fields."
                            )
                        )
                    },
                    actionSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSButton(
                                scheme = neutralScheme,
                                props = ODSButtonProps(
                                    label = "Action 1",
                                    variant = ODSButtonVariant.PRIMARY
                                ),
                                onClick = {}
                            )
                            ODSButton(
                                scheme = neutralScheme,
                                props = ODSButtonProps(
                                    label = "Action 2",
                                    variant = ODSButtonVariant.OUTLINE
                                ),
                                onClick = {}
                            )
                        }
                    },
                    onClick = {}
                )
            }

            // ODSCardBasic Custom Content Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                // CardBasic with Custom Content (no ODSCardContentBasic)
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    contentSlot = {
                        ODSColumn(
                            modifier = Modifier.fillMaxWidth(),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info"
                                )
                            )
                            ODSColumn(
                                modifier = Modifier.fillMaxWidth(),
                                gap = DSVariables.spacingComponent1
                            ) {
                                // Custom content can be added here
                            }
                        }
                    },
                    onClick = {}
                )

                // CardBasic with Only Action Slot
                ODSCardBasic(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardBasicProps(),
                    actionSlot = {
                        ODSButton(
                            scheme = neutralScheme,
                            props = ODSButtonProps(
                                label = "Action Only Card",
                                variant = ODSButtonVariant.PRIMARY
                            ),
                            onClick = {}
                        )
                    },
                    onClick = {}
                )
            }
        }
    }
}

