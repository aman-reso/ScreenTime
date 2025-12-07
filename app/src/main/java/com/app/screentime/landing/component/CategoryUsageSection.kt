package com.app.screentime.landing.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.app.screentime.landing.model.CategoryUsage
import com.app.screentime.landing.util.AppCategoryUtils
import com.telekom.odsystem.DSVariables
import com.telekom.odsystem.atoms.ODSBox
import com.telekom.odsystem.atoms.ODSColumn
import com.telekom.odsystem.atoms.ODSImageModel
import com.telekom.odsystem.atoms.icon.ODSIconModel
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.molecules.accordion.ODSAccordion
import com.telekom.odsystem.molecules.accordion.ODSAccordionProps
import com.telekom.odsystem.molecules.accordion.ODSAccordionSize
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandard
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardProps
import com.telekom.odsystem.molecules.listrowstandard.ODSListRowStandardVariant
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.organisms.cardbasic.ODSCardBasic
import com.telekom.odsystem.tokens.tokens.ODSTheme

/**
 * Get icon for a category
 * Uses drawable resources from AppCategoryUtils if available, otherwise falls back to Material Icons
 * Returns ODSImageModel for proper alignment in ODSListRowStandard
 */
private fun getCategoryIcon(
    category: AppCategoryUtils.AppCategory
): ODSImageModel? {
    val drawableRes = category.drawableRes

    return if (drawableRes != 0) {
        ODSImageModel(drawableRes = drawableRes)
    } else {
        val iconVector = when (category) {
            AppCategoryUtils.AppCategory.SOCIAL_MEDIA -> Icons.Rounded.SocialDistance
            AppCategoryUtils.AppCategory.ENTERTAINMENT -> Icons.Rounded.Movie
            AppCategoryUtils.AppCategory.PRODUCTIVITY -> Icons.Rounded.Work
            AppCategoryUtils.AppCategory.GAMES -> Icons.Rounded.Games
            AppCategoryUtils.AppCategory.COMMUNICATION -> Icons.Rounded.Chat
            AppCategoryUtils.AppCategory.SHOPPING -> Icons.Rounded.ShoppingCart
            AppCategoryUtils.AppCategory.NEWS_READING -> Icons.Rounded.Newspaper
            AppCategoryUtils.AppCategory.UTILITIES -> Icons.Rounded.Build
            AppCategoryUtils.AppCategory.OTHERS -> Icons.Rounded.Category
            else -> Icons.Rounded.Category
        }
        ODSImageModel(imageVector = iconVector)
    }
}

/**
 * Category-wise usage section component using ODS Accordion
 * Displays app usage grouped by categories (Social Media, Entertainment, Productivity, etc.)
 */
@Composable
fun CategoryUsageSection(
    categoryUsage: List<CategoryUsage>,
    modifier: Modifier = Modifier,
    scheme: ODSTheme = neutralScheme
) {
    if (categoryUsage.isEmpty()) {
        return
    }

    var isExpanded by remember { mutableStateOf(false) }

    ODSColumn(
        modifier = modifier.fillMaxWidth(),
        gap = DSVariables.spacingComponent3
    ) {
        ODSAccordion(
            modifier = Modifier.fillMaxWidth(),
            scheme = scheme,
            props = ODSAccordionProps(
                headerText = "Category Usage",
                expanded = isExpanded,
                size = ODSAccordionSize.SMALL
            ),
            onClick = { expanded ->
                isExpanded = expanded
            },
            contentSlot = {
                ODSColumn(
                    modifier = Modifier.fillMaxWidth(),
                    gap = DSVariables.spacingComponent2
                ) {
                    categoryUsage.forEach { category ->
                        ODSBox(
                            modifier = Modifier.fillMaxWidth(),
                            background = listOf(ODSColorModel(scheme.basicBackgroundCard)),
                            cornerRadius = ODSCorners(all = DSVariables.radiusSmall),
                            padding = ODSPadding(horizontal = DSVariables.spacingComponent4)
                        ) {
                            ODSListRowStandard(
                                modifier = Modifier,
                                scheme = scheme,
                                props = ODSListRowStandardProps(
                                    variant = ODSListRowStandardVariant.IMAGE,
                                    labelText = category.category.displayName,
                                    descriptionTitle = category.formattedTime,
                                    descriptionText = "${category.percentage.toInt()}% of total",
                                    image = getCategoryIcon(category.category)
                                )
                            )
                        }
                    }
                }
            }
        )
    }
}

