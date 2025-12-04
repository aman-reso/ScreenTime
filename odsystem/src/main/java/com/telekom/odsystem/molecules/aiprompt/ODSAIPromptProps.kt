package com.telekom.odsystem.molecules.aiprompt

import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptCustomWidth.FILL
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptCustomWidth.HUG
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptType.LEFT_ICON_TEXT
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptType.TEXT_ONLY
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptType.TOP_ICON_TEXT
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptVariant.FILLED
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptVariant.GHOST
import com.telekom.odsystem.molecules.aiprompt.ODSAIPromptVariant.OUTLINE

/**
 * Represents the different layout types for the AI Prompt component.
 *
 * - [TOP_ICON_TEXT]: Displays an icon above the text.
 * - [LEFT_ICON_TEXT]: Displays an icon to the left of the text.
 * - [TEXT_ONLY]: Displays only text, without any icon.
 */
enum class ODSAIPromptType {
    TOP_ICON_TEXT,
    LEFT_ICON_TEXT,
    TEXT_ONLY,
}

/**
 * Defines the visual style of the AI Prompt.
 * - [FILLED]: The AI Prompt has a solid background color.
 * - [OUTLINE]: The AI Prompt has a border and a transparent background.
 * - [GHOST]: The AI Prompt has no visible border or background, appearing as plain text and icons.
 */
enum class ODSAIPromptVariant {
    FILLED,
    OUTLINE,
    GHOST,
}

/**
 * Specifies how the AI Prompt component should adjust its width.
 *
 * - [FILL]: The component will expand to fill the available width of its parent container.
 * - [HUG]: The component's width will be determined by the size of its content (text and icons).
 */
enum class ODSAIPromptCustomWidth {
    FILL,
    HUG,
}

/**
 * Code generated with ODS RADD Code Generator
 * 2025-07-22 (v1.32.2) - uid: 281fbef5
 * Figma link: https://figma.com/design/RTdgj2EBwu8TwoaWWVEovL/ODS_OneID_Production_Library?node-id=16634-2973
 */

/**
 * Data class for the properties of an AI Prompt component.
 *
 * @param customWidth The custom width configuration for the AI Prompt.
 * @param description Optional description text to display below the title.
 * @param icon Optional icon to display. The position is determined by the `type` property.
 * @param rightIcon Optional icon to display on the right side of the prompt.
 * @param title Optional title text to display.
 * @param type The type of the AI Prompt, which determines the layout of the icon and text.
 * @param variant The visual style of the AI Prompt.
 */
data class ODSAIPromptProps(
    var customWidth: ODSAIPromptCustomWidth = ODSAIPromptCustomWidth.FILL,
    var description: String? = null,
    var icon: ODSIconModel? = null,
    var rightIcon: ODSIconModel? = null,
    var title: String? = null,
    var type: ODSAIPromptType = ODSAIPromptType.TOP_ICON_TEXT,
    var variant: ODSAIPromptVariant = ODSAIPromptVariant.FILLED,
)
