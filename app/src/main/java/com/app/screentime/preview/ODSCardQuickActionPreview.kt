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
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.extensions.background
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize
import com.telekom.odsystem.slots.quickactioncardpreferredcontent.ODSQuickActionCardPreferredContent
import com.telekom.odsystem.slots.quickactioncardpreferredcontent.ODSQuickActionCardPreferredContentProps
import com.telekom.odsystem.slots.quickactioncardpreferredcontent.ODSQuickActionCardPreferredContentTitleType
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Preview(showBackground = true)
@Composable
fun ODSCardQuickActionPreview() {
    ODSBox(modifier = Modifier, background = listOf(ODSColorModel(neutralScheme.basicBackground))) {
        ODSColumn(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(DSVariables.spacingComponent5),
            gap = DSVariables.spacingComponent4
        ) {
            // Basic Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Basic Examples",
                    style = DSTextStyles.titleS
                )

                // Medium, Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Medium Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "This is a medium-sized card with filled background",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )

                // Medium, Not Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = false
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Medium Not Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "This is a medium-sized card without filled background",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )

                // Small, Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.SMALL,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent3),
                            gap = DSVariables.spacingComponent1
                        ) {
                            ODSText(
                                text = "Small Filled Card",
                                style = DSTextStyles.bodyMBold,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Compact card with filled background",
                                style = DSTextStyles.bodySRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )

                // Small, Not Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.SMALL,
                        filled = false
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent3),
                            gap = DSVariables.spacingComponent1
                        ) {
                            ODSText(
                                text = "Small Not Filled Card",
                                style = DSTextStyles.bodyMBold,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Compact card without filled background",
                                style = DSTextStyles.bodySRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )
            }

            // Subtle Variants
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Subtle Variants",
                    style = DSTextStyles.titleS
                )

                // Medium, Subtle, Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true,
                        subtle = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Subtle Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Uses a more subtle color scheme",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )

                // Medium, Subtle, Not Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = false,
                        subtle = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Subtle Not Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Subtle variant without filled background",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )
            }

            // Disabled States
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Disabled States",
                    style = DSTextStyles.titleS
                )

                // Disabled, Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true,
                        disabled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Disabled Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "This card is disabled and cannot be clicked",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )

                // Disabled, Not Filled
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = false,
                        disabled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSColumn(
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent2
                        ) {
                            ODSText(
                                text = "Disabled Not Filled Card",
                                style = DSTextStyles.subtitle,
                                color = neutralScheme.basicText
                            )
                            ODSText(
                                text = "Disabled card without filled background",
                                style = DSTextStyles.bodyMRegular,
                                color = neutralScheme.basicTextRecessive
                            )
                        }
                    }
                )
            }

            // With Preferred Content
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "With Preferred Content",
                    style = DSTextStyles.titleS
                )

                // With Title and Subtitle
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSQuickActionCardPreferredContent(
                            scheme = neutralScheme,
                            props = ODSQuickActionCardPreferredContentProps(
                                title = "Quick Action with Title",
                                subtitle = "This card uses preferred content slot",
                                titleType = ODSQuickActionCardPreferredContentTitleType.TEXT
                            )
                        )
                    }
                )

                // With Tags
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSQuickActionCardPreferredContent(
                            scheme = neutralScheme,
                            props = ODSQuickActionCardPreferredContentProps(
                                title = "Card with Tags",
                                subtitle = "Includes tag components",
                                titleType = ODSQuickActionCardPreferredContentTitleType.TEXT,
                                tag1Props = ODSTagStaticProps(label = "Tag 1"),
                                tag2Props = ODSTagStaticProps(label = "Tag 2")
                            )
                        )
                    }
                )

                // With Logo
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSQuickActionCardPreferredContent(
                            scheme = neutralScheme,
                            props = ODSQuickActionCardPreferredContentProps(
                                title = null,
                                subtitle = "Card with logo instead of text title",
                                titleType = ODSQuickActionCardPreferredContentTitleType.LOGO,
                                logo = ODSImageModel(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Logo"
                                )
                            )
                        )
                    }
                )
            }

            // Custom Content Examples
            ODSColumn(
                modifier = Modifier.fillMaxWidth(),
                gap = DSVariables.spacingComponent3
            ) {
                ODSText(
                    text = "Custom Content Examples",
                    style = DSTextStyles.titleS
                )

                // With Icon
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = true
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent3,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings"
                                ),
                                width = DSVariables.sizingComponent8,
                                height = DSVariables.sizingComponent8
                            )
                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = DSVariables.spacingComponent1
                            ) {
                                ODSText(
                                    text = "Settings",
                                    style = DSTextStyles.subtitle,
                                    color = neutralScheme.basicText
                                )
                                ODSText(
                                    text = "Configure your preferences",
                                    style = DSTextStyles.bodySRegular,
                                    color = neutralScheme.basicTextRecessive
                                )
                            }
                        }
                    }
                )

                // With Multiple Icons
                ODSCardQuickAction(
                    modifier = Modifier.fillMaxWidth(),
                    scheme = neutralScheme,
                    props = ODSCardQuickActionProps(
                        size = ODSCardQuickActionSize.MEDIUM,
                        filled = false
                    ),
                    onClick = {},
                    contentSlot = {
                        ODSRow(
                            modifier = Modifier.fillMaxWidth(),
                            padding = ODSPadding(all = DSVariables.spacingComponent4),
                            gap = DSVariables.spacingComponent3,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications"
                                ),
                                width = DSVariables.sizingComponent8,
                                height = DSVariables.sizingComponent8
                            )
                            ODSColumn(
                                modifier = Modifier.weight(1f),
                                gap = DSVariables.spacingComponent1
                            ) {
                                ODSText(
                                    text = "Notifications",
                                    style = DSTextStyles.bodyMBold,
                                    color = neutralScheme.basicText
                                )
                                ODSText(
                                    text = "Manage notification settings",
                                    style = DSTextStyles.bodySRegular,
                                    color = neutralScheme.basicTextRecessive
                                )
                            }
                            ODSIcon(
                                iconModel = ODSIconModel(
                                    imageVector = Icons.Default.Info,
                                    tint = neutralScheme.functionalInformationalStandard,
                                    contentDescription = "Info"
                                ),
                                width = DSVariables.sizingComponent7,
                                height = DSVariables.sizingComponent7
                            )
                        }
                    }
                )
            }
        }
    }
}

