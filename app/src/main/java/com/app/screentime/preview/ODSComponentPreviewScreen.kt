package com.app.screentime.preview

import ODSListRowNavigationProps
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.DSTextStyles
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSRow
import com.telekom.odsystem.atoms.ODSText
import com.telekom.odsystem.atoms.avatar.ODSAvatar
import com.telekom.odsystem.atoms.avatar.ODSAvatarBadgeType
import com.telekom.odsystem.atoms.avatar.ODSAvatarProps
import com.telekom.odsystem.atoms.avatar.ODSAvatarSize
import com.telekom.odsystem.atoms.avatar.ODSAvatarVariant
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIcon
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconMode
import com.telekom.odsystem.atoms.badgeicon.ODSBadgeIconProps
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumber
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberProps
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberSize
import com.telekom.odsystem.atoms.badgenumber.ODSBadgeNumberVariant
import com.telekom.odsystem.atoms.button.ODSButton
import com.telekom.odsystem.atoms.button.ODSButtonButtonType
import com.telekom.odsystem.atoms.button.ODSButtonProps
import com.telekom.odsystem.atoms.button.ODSButtonVariant
import com.telekom.odsystem.atoms.checkbox.ODSCheckbox
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxProps
import com.telekom.odsystem.atoms.checkbox.ODSCheckboxSelected
import com.telekom.odsystem.atoms.dismissiblechip.ODSDismissibleChip
import com.telekom.odsystem.atoms.dismissiblechip.ODSDismissibleChipProps
import com.telekom.odsystem.atoms.divider.ODSDivider
import com.telekom.odsystem.atoms.divider.ODSDividerProps
import com.telekom.odsystem.atoms.divider.ODSDividerVariant
import com.telekom.odsystem.atoms.filterchip.ODSFilterChip
import com.telekom.odsystem.atoms.filterchip.ODSFilterChipProps
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButton
import com.telekom.odsystem.atoms.floatingactionbutton.ODSFloatingActionButtonProps
import com.telekom.odsystem.atoms.icon.ODSIcon
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.atoms.link.ODSLink
import com.telekom.odsystem.atoms.link.ODSLinkProps
import com.telekom.odsystem.atoms.loadingbar.ODSLoadingBar
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinner
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerLabelAlignment
import com.telekom.odsystem.atoms.loadingspinner.ODSLoadingSpinnerProps
import com.telekom.odsystem.atoms.progressbar.ODSProgressBar
import com.telekom.odsystem.atoms.progressbar.ODSProgressBarProps
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButton
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonProps
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonMode
import com.telekom.odsystem.atoms.radiobutton.ODSRadioButtonSize
import com.telekom.odsystem.atoms.switch.ODSSwitch
import com.telekom.odsystem.atoms.switch.ODSSwitchProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStatic
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticProps
import com.telekom.odsystem.atoms.tagstatic.ODSTagStaticType
import com.telekom.odsystem.atoms.textarea.ODSTextArea
import com.telekom.odsystem.atoms.textarea.ODSTextAreaProps
import com.telekom.odsystem.atoms.textfield.ODSTextField
import com.telekom.odsystem.atoms.textfield.ODSTextFieldProps
import com.telekom.odsystem.atoms.togglechip.ODSToggleChip
import com.telekom.odsystem.atoms.togglechip.ODSToggleChipProps
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControls
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsProps
import com.telekom.odsystem.molecules.listrowcontrols.ODSListRowControlsVariant
import com.telekom.odsystem.molecules.listrownavigation.ODSListRowNavigation
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigation
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationItemProps
import com.telekom.odsystem.organisms.bottomnavigation.ODSBottomNavigationProps
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasicProps
import com.telekom.odsystem.organisms.barchart.ODSBarChart
import com.telekom.odsystem.organisms.barchart.ODSBarChartProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemDirection
import com.telekom.odsystem.organisms.barchart.ODSBarItemProps
import com.telekom.odsystem.organisms.barchart.ODSBarItemShape
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickAction
import com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionProps
import com.telekom.odsystem.tokens.tokens.*
import com.telekom.odsystem.tokens.tokens.allSchemes

/**
 * ODS Component Preview Screen - Showcase all ODS components with different schemes.
 */
@Composable
fun ODSComponentPreviewScreen(
    modifier: Modifier = Modifier,
    initialScheme: ODSTheme = neutralScheme
) {
    var selectedScheme by remember { mutableStateOf(initialScheme) }

    ODSBox(
        modifier = modifier.fillMaxSize(),
        background = listOf(com.telekom.odsystem.foundations.ODSColorModel(hexColor = selectedScheme.basicBackground))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = DSVariables.spacingComponent5,
                vertical = DSVariables.spacingComponent5
            ),
            verticalArrangement = Arrangement.spacedBy(DSVariables.spacingComponent7)
        ) {
            // Scheme Selector
            item {
                SchemeSelector(
                    selectedScheme = selectedScheme,
                    onSchemeSelected = { selectedScheme = it }
                )
            }

            item {
                ODSDivider(
                    scheme = selectedScheme,
                    props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
                )
            }

            // Atoms Section
            item {
                SectionHeader(scheme = selectedScheme, title = "Atoms")
            }

            // Buttons
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Buttons",
                    content = {
                        ButtonsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Form Controls
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Form Controls",
                    content = {
                        FormControlsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Text & Typography
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Text & Typography",
                    content = {
                        TextPreview(scheme = selectedScheme)
                    }
                )
            }

            // Icons
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Icons",
                    content = {
                        IconsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Avatars
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Avatars",
                    content = {
                        AvatarsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Badges
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Badges",
                    content = {
                        BadgesPreview(scheme = selectedScheme)
                    }
                )
            }

            // Chips
            item {
//                ComponentSection(
//                    scheme = selectedScheme,
//                    title = "Chips",
//                    content = {
//                        ChipsPreview(scheme = selectedScheme)
//                    }
//                )
            }

            // Tags
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Tags",
                    content = {
                        TagsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Progress Indicators
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Progress Indicators",
                    content = {
                        ProgressIndicatorsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Loading States
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Loading States",
                    content = {
                        LoadingStatesPreview(scheme = selectedScheme)
                    }
                )
            }

            // Links
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Links",
                    content = {
                        LinksPreview(scheme = selectedScheme)
                    }
                )
            }

            // Floating Action Button
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Floating Action Button",
                    content = {
                        FloatingActionButtonPreview(scheme = selectedScheme)
                    }
                )
            }

            // Dividers
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Dividers",
                    content = {
                        DividersPreview(scheme = selectedScheme)
                    }
                )
            }

            // Molecules Section
            item {
                ODSBox(height = DSVariables.spacingComponent7) {}
            }
            item {
                SectionHeader(scheme = selectedScheme, title = "Molecules")
            }

            // List Rows
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "List Rows",
                    content = {
                        ListRowsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Cards
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Cards",
                    content = {
                        CardsPreview(scheme = selectedScheme)
                    }
                )
            }

            // Organisms Section
            item {
                ODSBox(height = DSVariables.spacingComponent7) {}
            }
            item {
                SectionHeader(scheme = selectedScheme, title = "Organisms")
            }

            // Card Quick Action
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Card Quick Action",
                    content = {
                        CardQuickActionPreview(scheme = selectedScheme)
                    }
                )
            }

            // Bar Chart
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Bar Chart",
                    content = {
                        BarChartPreview(scheme = selectedScheme)
                    }
                )
            }

            // Bottom Navigation
            item {
                ComponentSection(
                    scheme = selectedScheme,
                    title = "Bottom Navigation",
                    content = {
                        BottomNavigationPreview(scheme = selectedScheme)
                    }
                )
            }
        }
    }
}

@Composable
private fun SchemeSelector(
    selectedScheme: ODSTheme,
    onSchemeSelected: (ODSTheme) -> Unit
) {
    ODSColumn(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSText(
            text = "Select Theme Scheme",
            style = DSTextStyles.titleS,
            color = selectedScheme.basicText
        )

        ODSColumn(
            gap = DSVariables.spacingComponent2
        ) {
            allSchemes.take(10).forEachIndexed { index, scheme ->
                val isSelected = scheme.name == selectedScheme.name
                ODSButton(
                    scheme = selectedScheme,
                    props = ODSButtonProps(
                        buttonType = ODSButtonButtonType.STANDARD,
                        label = scheme.name,
                        variant = if (isSelected)
                            ODSButtonVariant.PRIMARY else ODSButtonVariant.GHOST
                    ),
                    onClick = { onSchemeSelected(scheme) }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    scheme: ODSTheme,
    title: String
) {
    ODSText(
        text = title,
        style = DSTextStyles.titleM,
        color = scheme.basicText,
        modifier = Modifier.padding(vertical = DSVariables.spacingComponent3)
    )
}

@Composable
private fun ComponentSection(
    scheme: ODSTheme,
    title: String,
    content: @Composable () -> Unit
) {
    ODSColumn(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSText(
            text = title,
            style = DSTextStyles.subtitle,
            color = scheme.basicText
        )
        content()
    }
}

@Composable
private fun ButtonsPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Primary Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonType = ODSButtonButtonType.STANDARD,
                label = "Primary Button",
                variant = ODSButtonVariant.PRIMARY
            ),
            onClick = {}
        )

        // Secondary Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonType = ODSButtonButtonType.STANDARD,
                label = "Secondary Button",
                variant = ODSButtonVariant.SECONDARY
            ),
            onClick = {}
        )

        // Ghost Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonType = ODSButtonButtonType.STANDARD,
                label = "Ghost Button",
                variant = ODSButtonVariant.GHOST
            ),
            onClick = {}
        )

        // Outline Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonType = ODSButtonButtonType.STANDARD,
                label = "Outline Button",
                variant = ODSButtonVariant.OUTLINE
            ),
            onClick = {}
        )

        // Icon Button
        ODSButton(
            scheme = scheme,
            props = ODSButtonProps(
                buttonType = ODSButtonButtonType.ICON_ONLY,
                buttonIcon = ODSIconModel(imageVector = Icons.Default.Add),
                variant = ODSButtonVariant.PRIMARY
            ),
            onClick = {}
        )
    }
}

@Composable
private fun FormControlsPreview(scheme: ODSTheme) {
    var checkboxState by remember { mutableStateOf(false) }
    var switchState by remember { mutableStateOf(false) }
    var radioState by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    var textAreaValue by remember { mutableStateOf("") }

    ODSColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Checkbox
        ODSCheckbox(
            scheme = scheme,
            props = ODSCheckboxProps(
                selected = if (checkboxState) ODSCheckboxSelected.SELECTED else ODSCheckboxSelected.UNSELECTED
            ),
            onClick = { checkboxState = !checkboxState }
        )

        // Switch
        ODSSwitch(
            scheme = scheme,
            props = ODSSwitchProps(
                selected = switchState
            ),
            onCheckedChange = { switchState = !switchState }
        )

        // Radio Button
        ODSRadioButton(
            scheme = scheme,
            props = ODSRadioButtonProps(
                selected = radioState,
                label = "Radio Button",
                size = ODSRadioButtonSize.LARGE,
                mode = ODSRadioButtonMode.STANDARD
            ),
            onClick = { radioState = !radioState }
        )

        // Text Field
        ODSTextField(
            scheme = scheme,
            props = ODSTextFieldProps(
                label = "Text Field",
                inputText = textFieldValue
            ),
            onValueChange = { textFieldValue = it }
        )

        // Text Area
        ODSTextArea(
            scheme = scheme,
            props = ODSTextAreaProps(
                labelText = "Text Area",
                inputText = textAreaValue
            ),
            onValueChange = { textAreaValue = it }
        )
    }
}

@Composable
private fun TextPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent2,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSText(
            text = "Title S",
            style = DSTextStyles.titleS,
            color = scheme.basicText
        )
        ODSText(
            text = "Subtitle",
            style = DSTextStyles.subtitle,
            color = scheme.basicText
        )
        ODSText(
            text = "Body M Regular",
            style = DSTextStyles.bodyMRegular,
            color = scheme.basicText
        )
        ODSText(
            text = "Body M Bold",
            style = DSTextStyles.bodyMBold,
            color = scheme.basicText
        )
        ODSText(
            text = "Body S Regular",
            style = DSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
        ODSText(
            text = "Microcopy Regular",
            style = DSTextStyles.microcopyRegular,
            color = scheme.basicTextRecessive
        )
    }
}

@Composable
private fun IconsPreview(scheme: ODSTheme) {
    ODSRow(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = Icons.Default.Home,
                tint = scheme.basicText
            ),
            width = DSVariables.sizingComponent10,
            height = DSVariables.sizingComponent10
        )
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = Icons.Default.Settings,
                tint = scheme.basicAccent
            ),
            width = DSVariables.sizingComponent10,
            height = DSVariables.sizingComponent10
        )
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = Icons.Default.Star,
                tint = scheme.functionalDestructiveStandard
            ),
            width = DSVariables.sizingComponent10,
            height = DSVariables.sizingComponent10
        )
        ODSIcon(
            iconModel = ODSIconModel(
                imageVector = Icons.Default.Favorite,
                tint = scheme.functionalSuccessStandard
            ),
            width = DSVariables.sizingComponent10,
            height = DSVariables.sizingComponent10
        )
    }
}

@Composable
private fun AvatarsPreview(scheme: ODSTheme) {
    ODSRow(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Avatar with Initials
        ODSAvatar(
            scheme = scheme,
            props = ODSAvatarProps(
                variant = ODSAvatarVariant.INITIALS,
                initials = "JD",
                size = ODSAvatarSize.LARGE,
                showBadge = false
            )
        )

        // Avatar with Icon
        ODSAvatar(
            scheme = scheme,
            props = ODSAvatarProps(
                variant = ODSAvatarVariant.ICON,
                icon = ODSIconModel(imageVector = Icons.Default.Person),
                size = ODSAvatarSize.MEDIUM,
                showBadge = true,
                badgeType = ODSAvatarBadgeType.NUMBER,
                badgeNumberProps = com.telekom.odsystem.atoms.avatar.ODSAvatarBadgeNumberProps(
                    notificationNumber = "5"
                )
            )
        )

        // Small Avatar
        ODSAvatar(
            scheme = scheme,
            props = ODSAvatarProps(
                variant = ODSAvatarVariant.INITIALS,
                initials = "AB",
                size = ODSAvatarSize.SMALL,
                showBadge = true,
                badgeType = ODSAvatarBadgeType.ICON,
                badgeIconProps = com.telekom.odsystem.atoms.avatar.ODSAvatarBadgeIconProps(
                    mode = ODSBadgeIconMode.SUCCESS
                )
            )
        )
    }
}

@Composable
private fun BadgesPreview(scheme: ODSTheme) {
    ODSRow(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Badge Number
        ODSBadgeNumber(
            scheme = scheme,
            props = ODSBadgeNumberProps(
                notificationNumber = "5",
                variant = ODSBadgeNumberVariant.NOTIFICATION,
                size = ODSBadgeNumberSize.STANDARD
            )
        )

        ODSBadgeNumber(
            scheme = scheme,
            props = ODSBadgeNumberProps(
                notificationNumber = "99+",
                variant = ODSBadgeNumberVariant.NEUTRAL,
                size = ODSBadgeNumberSize.LARGE
            )
        )

        // Badge Icon
        ODSBadgeIcon(
            scheme = scheme,
            props = ODSBadgeIconProps(
                mode = ODSBadgeIconMode.SUCCESS
            )
        )

        ODSBadgeIcon(
            scheme = scheme,
            props = ODSBadgeIconProps(
                mode = ODSBadgeIconMode.ERROR
            )
        )
    }
}

//@Composable
//private fun ChipsPreview(scheme: ODSTheme) {
//    var filterChipSelected by remember { mutableStateOf(false) }
//    var toggleChipSelected by remember { mutableStateOf(false) }
//
//    ODSColumn(
//        gap = DSVariables.spacingComponent3,
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // Filter Chip
//        ODSFilterChip(
//            scheme = scheme,
//            props = ODSFilterChipProps(
//                label = "Filter Chip",
//                selected = if (filterChipSelected) ODSFilterChipSelected.SELECTED else ODSFilterChipSelected.UNSELECTED
//            ),
//            onClick = { filterChipSelected = !filterChipSelected }
//        )
//
//        // Toggle Chip
//        ODSToggleChip(
//            scheme = scheme,
//            props = ODSToggleChipProps(
//                label = "Toggle Chip",
//                selected = if (toggleChipSelected) ODSToggleChipSelected.SELECTED else ODSToggleChipSelected.UNSELECTED
//            ),
//            onToggle = { toggleChipSelected = !toggleChipSelected }
//        )
//
//        // Dismissible Chip
//        ODSDismissibleChip(
//            scheme = scheme,
//            props = ODSDismissibleChipProps(
//                label = "Dismissible Chip"
//            ),
//            onDismiss = {},
//        )
//    }
//}

@Composable
private fun TagsPreview(scheme: ODSTheme) {
    ODSRow(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ODSTagStatic(
            scheme = scheme,
            props = ODSTagStaticProps(
                label = "Tag",
                type = ODSTagStaticType.SUCCESS
            )
        )

        ODSTagStatic(
            scheme = scheme,
            props = ODSTagStaticProps(
                label = "Success Tag",
                type = ODSTagStaticType.ERROR
            )
        )

        ODSTagStatic(
            scheme = scheme,
            props = ODSTagStaticProps(
                label = "Error Tag",
                type = ODSTagStaticType.SAVINGS
            )
        )
    }
}

@Composable
private fun ProgressIndicatorsPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Progress Bar
        ODSProgressBar(
            scheme = scheme,
            props = ODSProgressBarProps(
                extraDataProgress = 0.6f
            )
        )

        ODSProgressBar(
            scheme = scheme,
            props = ODSProgressBarProps(
                mainDataProgress = 0.3f
            )
        )
    }
}

@Composable
private fun LoadingStatesPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent5,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Loading Spinner
        ODSLoadingSpinner(
            scheme = scheme,
            props = ODSLoadingSpinnerProps(
                labelAlignment = ODSLoadingSpinnerLabelAlignment.NONE
            )
        )

        ODSLoadingSpinner(
            scheme = scheme,
            props = ODSLoadingSpinnerProps(
                labelAlignment = ODSLoadingSpinnerLabelAlignment.VERTICAL,
                labelText = "Loading..."
            )
        )

        // Loading Bar
        ODSLoadingBar(scheme = scheme)
    }
}

@Composable
private fun LinksPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSLink(
            scheme = scheme,
            props = ODSLinkProps(label = "Primary Link"),
            onClick = {}
        )
        ODSLink(
            scheme = scheme,
            props = ODSLinkProps(label = "Secondary Link"),
            onClick = {}
        )
    }
}

@Composable
private fun FloatingActionButtonPreview(scheme: ODSTheme) {
    ODSFloatingActionButton(
        scheme = scheme,
        props = ODSFloatingActionButtonProps(
            icon = ODSIconModel(imageVector = Icons.Default.Add)
        ),
        onClick = {}
    )
}

@Composable
private fun DividersPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSText(
            text = "Horizontal Divider",
            style = DSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
        ODSDivider(
            scheme = scheme,
            props = ODSDividerProps(variant = ODSDividerVariant.HORIZONTAL)
        )
        ODSText(
            text = "Vertical Divider",
            style = DSTextStyles.bodySRegular,
            color = scheme.basicTextRecessive
        )
        ODSRow(
            gap = DSVariables.spacingComponent4,
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Left",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText
            )
            ODSDivider(
                scheme = scheme,
                props = ODSDividerProps(variant = ODSDividerVariant.VERTICAL)
            )
            ODSText(
                text = "Right",
                style = DSTextStyles.bodyMRegular,
                color = scheme.basicText
            )
        }
    }
}

@Composable
private fun ListRowsPreview(scheme: ODSTheme) {
    var switchState by remember { mutableStateOf(false) }

    ODSColumn(
        gap = DSVariables.spacingComponent3,
        modifier = Modifier.fillMaxWidth()
    ) {
        // List Row Standard
        ODSListRowStandard(
            scheme = scheme,
            props = ODSListRowStandardProps(
                labelText = "Standard List Row",
                descriptionText = "This is a standard list row with description",
                variant = ODSListRowStandardVariant.STANDARD
            )
        )

        // List Row Standard with Icon
        ODSListRowStandard(
            scheme = scheme,
            props = ODSListRowStandardProps(
                labelText = "List Row with Icon",
                descriptionText = "This row has an icon",
                variant = ODSListRowStandardVariant.ICON,
                icon = ODSIconModel(imageVector = Icons.Default.Settings)
            )
        )

        // List Row Controls
        ODSListRowControls(
            scheme = scheme,
            props = ODSListRowControlsProps(
                labelText = "List Row with Switch",
                variant = ODSListRowControlsVariant.STANDARD,
                selected = switchState
            ),
            onSwitchClick = { switchState = !switchState }
        )

        // List Row Controls with Icon
        ODSListRowControls(
            scheme = scheme,
            props = ODSListRowControlsProps(
                labelText = "List Row with Icon & Switch",
                variant = ODSListRowControlsVariant.ICON,
                icon = ODSIconModel(imageVector = Icons.Default.Notifications),
                selected = switchState
            ),
            onSwitchClick = { switchState = !switchState }
        )

        // List Row Navigation
        ODSListRowNavigation(
            scheme = scheme,
            props = ODSListRowNavigationProps(
                labelText = "Navigation List Row",
                descriptionText = "Tap to navigate",
                variant = ODSListRowNavigationVariant.STANDARD
            ),
            onClick = {}
        )

        // List Row Navigation with Icon
        ODSListRowNavigation(
            scheme = scheme,
            props = ODSListRowNavigationProps(
                labelText = "Navigation with Icon",
                descriptionText = "Has a leading icon",
                variant = ODSListRowNavigationVariant.ICON,
                icon = ODSIconModel(imageVector = Icons.Default.ArrowForward)
            ),
            onClick = {}
        )
    }
}

@Composable
private fun CardsPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        ODSCardBasic(
            scheme = scheme,
            props = ODSCardBasicProps(),
            modifier = Modifier.fillMaxWidth(),
            contentSlot = {
                ODSColumn(
                    padding = com.telekom.odsystem.foundations.ODSPadding(all = DSVariables.spacingComponent5)
                ) {
                    ODSText(
                        text = "Card Title",
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                    ODSBox(height = DSVariables.spacingComponent2) {}
                    ODSText(
                        text = "Card content goes here",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        )
    }
}

@Composable
private fun CardQuickActionPreview(scheme: ODSTheme) {
    ODSColumn(
        gap = DSVariables.spacingComponent4,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Card Quick Action - Medium, Filled
        ODSCardQuickAction(
            scheme = scheme,
            props = ODSCardQuickActionProps(
                size = com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize.MEDIUM,
                filled = true
            ),
            onClick = {},
            contentSlot = {
                ODSColumn(
                    padding = com.telekom.odsystem.foundations.ODSPadding(all = DSVariables.spacingComponent5)
                ) {
                    ODSText(
                        text = "Quick Action Card",
                        style = DSTextStyles.subtitle,
                        color = scheme.basicText
                    )
                    ODSBox(height = DSVariables.spacingComponent2) {}
                    ODSText(
                        text = "Tap to perform quick action",
                        style = DSTextStyles.bodyMRegular,
                        color = scheme.basicTextRecessive
                    )
                }
            }
        )

        // Card Quick Action - Small, Subtle
        ODSCardQuickAction(
            scheme = scheme,
            props = ODSCardQuickActionProps(
                size = com.telekom.odsystem.organisms.cardquickaction.ODSCardQuickActionSize.SMALL,
                filled = false,
                subtle = true
            ),
            onClick = {},
            contentSlot = {
                ODSColumn(
                    padding = com.telekom.odsystem.foundations.ODSPadding(all = DSVariables.spacingComponent4)
                ) {
                    ODSText(
                        text = "Small Quick Action",
                        style = DSTextStyles.bodyMBold,
                        color = scheme.basicText
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BarChartPreview(scheme: ODSTheme = neutralScheme) {
    // Sample data for bar charts
    val verticalBarData = remember {
        listOf(
            ODSBarItemProps(xValue = 0.0, xLabel = "M", yValue = 20.0, yLabel = "20"),
            ODSBarItemProps(xValue = 1.0, xLabel = "T", yValue = 35.0, yLabel = "35"),
            ODSBarItemProps(xValue = 2.0, xLabel = "W", yValue = 15.0, yLabel = "15"),
            ODSBarItemProps(xValue = 3.0, xLabel = "T", yValue = 45.0, yLabel = "45"),
            ODSBarItemProps(xValue = 4.0, xLabel = "F", yValue = 30.0, yLabel = "30"),
            ODSBarItemProps(xValue = 5.0, xLabel = "S", yValue = 25.0, yLabel = "25"),
            ODSBarItemProps(xValue = 6.0, xLabel = "S", yValue = 40.0, yLabel = "40")
        )
    }

    val horizontalBarData = remember {
        listOf(
            ODSBarItemProps(xValue = 10.0, xLabel = "10", yValue = 0.0, yLabel = "App A"),
            ODSBarItemProps(xValue = 25.0, xLabel = "25", yValue = 1.0, yLabel = "App B"),
            ODSBarItemProps(xValue = 15.0, xLabel = "15", yValue = 2.0, yLabel = "App C"),
            ODSBarItemProps(xValue = 30.0, xLabel = "30", yValue = 3.0, yLabel = "App D"),
            ODSBarItemProps(xValue = 20.0, xLabel = "20", yValue = 4.0, yLabel = "App E")
        )
    }

    ODSColumn(
        gap = DSVariables.spacingComponent7,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Vertical Bar Chart - Pilled
        ODSColumn(
            gap = DSVariables.spacingComponent2,
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Vertical Bar Chart (Pilled)",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DSVariables.sizingComponent19), // 144.dp
                background = listOf(com.telekom.odsystem.foundations.ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSBarChart(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    props = ODSBarChartProps(
                        barItemsList = verticalBarData,
                        direction = ODSBarItemDirection.VERTICAL,
                        shape = ODSBarItemShape.PILLED,
                        showBottomLabels = true,
                        showTopLabels = false,
                        showLeftLabels = false,
                        showRightLabels = true,
                        stepCount = 4
                    ),
                    valueFormatter = { x, y -> "Value: $y" },
                    onBarSelected = {},
                    onBarDeSelected = {}
                )
            }
        }

        // Vertical Bar Chart - Squared
        ODSColumn(
            gap = DSVariables.spacingComponent2,
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Vertical Bar Chart (Squared)",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DSVariables.sizingComponent19),
                background = listOf(com.telekom.odsystem.foundations.ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSBarChart(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    props = ODSBarChartProps(
                        barItemsList = verticalBarData,
                        direction = ODSBarItemDirection.VERTICAL,
                        shape = ODSBarItemShape.SQUARED,
                        showTopLabels = true,
                        showBottomLabels = true,
                        showLeftLabels = true,
                        showRightLabels = false
                    ),
                    valueFormatter = { x, y -> "Value: $y" },
                    onBarSelected = {},
                    onBarDeSelected = {}
                )
            }
        }

        // Horizontal Bar Chart - Pilled
        ODSColumn(
            gap = DSVariables.spacingComponent2,
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Horizontal Bar Chart (Pilled)",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DSVariables.sizingComponent19),
                background = listOf(com.telekom.odsystem.foundations.ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSBarChart(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    props = ODSBarChartProps(
                        barItemsList = horizontalBarData,
                        direction = ODSBarItemDirection.HORIZONTAL,
                        shape = ODSBarItemShape.PILLED,
                        showTopLabels = false,
                        showBottomLabels = true,
                        showLeftLabels = true,
                        showRightLabels = true
                    ),
                    valueFormatter = { x, y -> "Value: $x" },
                    onBarSelected = {},
                    onBarDeSelected = {}
                )
            }
        }

        // Horizontal Bar Chart - Squared
        ODSColumn(
            gap = DSVariables.spacingComponent2,
            modifier = Modifier.fillMaxWidth()
        ) {
            ODSText(
                text = "Horizontal Bar Chart (Squared)",
                style = DSTextStyles.bodyMBold,
                color = scheme.basicText
            )
            ODSBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                background = listOf(com.telekom.odsystem.foundations.ODSColorModel(hexColor = scheme.basicBackgroundCard))
            ) {
                ODSBarChart(
                    modifier = Modifier.fillMaxSize(),
                    scheme = scheme,
                    props = ODSBarChartProps(
                        barItemsList = horizontalBarData,
                        direction = ODSBarItemDirection.HORIZONTAL,
                        shape = ODSBarItemShape.SQUARED,
                        showTopLabels = false,
                        showBottomLabels = true,
                        showLeftLabels = true,
                        showRightLabels = true
                    ),
                    valueFormatter = { x, y -> "Value: $x" },
                    onBarSelected = {},
                    onBarDeSelected = {}
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationPreview(scheme: ODSTheme) {
    var selectedIndex by remember { mutableStateOf(0) }

    ODSBottomNavigation(
        scheme = scheme,
        props = ODSBottomNavigationProps(
            items = listOf(
                ODSBottomNavigationItemProps(
                    text = "Home",
                    icon = ODSIconModel(imageVector = Icons.Default.Home),
                    iconActive = ODSIconModel(imageVector = Icons.Default.Home)
                ),
                ODSBottomNavigationItemProps(
                    text = "Settings",
                    icon = ODSIconModel(imageVector = Icons.Default.Settings),
                    iconActive = ODSIconModel(imageVector = Icons.Default.Settings)
                ),
                ODSBottomNavigationItemProps(
                    text = "Profile",
                    icon = ODSIconModel(imageVector = Icons.Default.Person),
                    iconActive = ODSIconModel(imageVector = Icons.Default.Person)
                )
            ),
            labels = true
        ),
        selectedIndex = selectedIndex,
        onIndexChanged = { selectedIndex = it }
    )
}

@Composable
private fun ODSComponentPreviewScreenPreview() {
    ODSComponentPreviewScreen(initialScheme = neutralScheme)
}
