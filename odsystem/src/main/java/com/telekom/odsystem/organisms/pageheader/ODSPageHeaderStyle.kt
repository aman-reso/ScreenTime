package com.telekom.odsystem.organisms.pageheader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
/**
 * Code generated with ODS RADD Code Generator
 * 2025-08-05 (v1.32.3) - uid: 1c27c8b1
 * Figma link: https://figma.com/design/9DAhZcVWjS2WXQjpYkC6D5/OneID Expl_Concept Topics?node-id=6699-29145
 */

class ODSPageHeaderStyle {
    var gap: Dp? = null
    var background: List<ODSColorModel>? = null
    var padding: ODSPadding? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var mainPageHeaderTopSectionPadding: ODSPadding? = null
    var mainPageHeaderTopSectionHeight: Dp? = null // Not used in mobile
    var mainPageHeaderTopSectionVerticalAlignment: Alignment.Vertical? = null
    var mainPageHeaderTopSectionHorizontalArrangement: Arrangement.Horizontal? = null
    var logoSlotContainerWidth: Dp? = null
    var logoSlotContainerHeight: Dp? = null
    var logoSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var logoSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var logoSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var topSectionMainPageTitleSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var topSectionMainPageTitleSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var topSectionMainPageTitleSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var preferredActionsSlotContainerMinWidth: Dp? = null
    var preferredActionsSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var preferredActionsSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var preferredActionsSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var mainPageTitleSlotContainerPadding: ODSPadding? = null
    var mainPageTitleSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var mainPageTitleSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var mainPageTitleSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var tabsContainerGap: Dp? = null
    var tabsContainerPadding: ODSPadding? = null
    var tabsContainerVerticalAlignment: Alignment.Vertical? = null
    var tabsContainerHorizontalAlignment: Alignment.Horizontal? = null
    var tabsContainerVerticalArrangement: Arrangement.Vertical? = null
    var dividerContainerHeight: Dp? = null
    var dividerContainerVerticalAlignment: Alignment.Vertical? = null
    var dividerContainerHorizontalAlignment: Alignment.Horizontal? = null
    var dividerContainerVerticalArrangement: Arrangement.Vertical? = null
    var subPageHeaderTopSectionPadding: ODSPadding? = null
    var subPageHeaderTopSectionHeight: Dp? = null // Not used in mobile
    var subPageHeaderTopSectionVerticalAlignment: Alignment.Vertical? = null
    var subPageHeaderTopSectionHorizontalArrangement: Arrangement.Horizontal? = null
    var subPageTitleSlotContainerGap: Dp? = null
    var subPageTitleSlotContainerVerticalAlignment: Alignment.Vertical? = null
    var subPageTitleSlotContainerHorizontalAlignment: Alignment.Horizontal? = null
    var subPageTitleSlotContainerVerticalArrangement: Arrangement.Vertical? = null
    var preferredActionsSlotContainer2MinWidth: Dp? = null
    var preferredActionsSlotContainer2VerticalAlignment: Alignment.Vertical? = null
    var preferredActionsSlotContainer2HorizontalAlignment: Alignment.Horizontal? = null
    var preferredActionsSlotContainer2VerticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        scheme: ODSTheme,
        props: ODSPageHeaderProps
    ): ODSPageHeaderStyle {
        val style = ODSPageHeaderStyle()
        style.gap = DSPageHeaderTokens.gap
        style.background = listOf(ODSColorModel(hexColor = scheme.basicBackground))
        style.padding = DSPageHeaderTokens.padding
        style.verticalAlignment = DSPageHeaderTokens.verticalAlignment
        style.horizontalAlignment = DSPageHeaderTokens.horizontalAlignment
        style.verticalArrangement = DSPageHeaderTokens.verticalArrangement
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM) {
            style.mainPageHeaderTopSectionPadding =
                DSPageHeaderTokens.mainPageHeaderTopSectionPaddingTypeMainPageHeaderSlim
            style.mainPageHeaderTopSectionHeight =
                DSPageHeaderTokens.mainPageHeaderTopSectionHeightTypeMainPageHeaderSlim
            style.mainPageHeaderTopSectionVerticalAlignment =
                DSPageHeaderTokens.mainPageHeaderTopSectionVerticalAlignmentTypeMainPageHeaderSlim
            style.mainPageHeaderTopSectionHorizontalArrangement =
                DSPageHeaderTokens.mainPageHeaderTopSectionHorizontalArrangementTypeMainPageHeaderSlim
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD) {
            style.mainPageHeaderTopSectionPadding =
                DSPageHeaderTokens.mainPageHeaderTopSectionPaddingTypeMainPageHeaderStandard
            style.mainPageHeaderTopSectionHeight =
                DSPageHeaderTokens.mainPageHeaderTopSectionHeightTypeMainPageHeaderStandard
            style.mainPageHeaderTopSectionVerticalAlignment =
                DSPageHeaderTokens.mainPageHeaderTopSectionVerticalAlignmentTypeMainPageHeaderStandard
            style.mainPageHeaderTopSectionHorizontalArrangement =
                DSPageHeaderTokens.mainPageHeaderTopSectionHorizontalArrangementTypeMainPageHeaderStandard
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM) {
            style.logoSlotContainerWidth =
                DSPageHeaderTokens.logoSlotContainerWidthTypeMainPageHeaderSlim
            style.logoSlotContainerHeight =
                DSPageHeaderTokens.logoSlotContainerHeightTypeMainPageHeaderSlim
            style.logoSlotContainerVerticalAlignment =
                DSPageHeaderTokens.logoSlotContainerVerticalAlignmentTypeMainPageHeaderSlim
            style.logoSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.logoSlotContainerHorizontalAlignmentTypeMainPageHeaderSlim
            style.logoSlotContainerVerticalArrangement =
                DSPageHeaderTokens.logoSlotContainerVerticalArrangementTypeMainPageHeaderSlim
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD) {
            style.logoSlotContainerWidth =
                DSPageHeaderTokens.logoSlotContainerWidthTypeMainPageHeaderStandard
            style.logoSlotContainerHeight =
                DSPageHeaderTokens.logoSlotContainerHeightTypeMainPageHeaderStandard
            style.logoSlotContainerVerticalAlignment =
                DSPageHeaderTokens.logoSlotContainerVerticalAlignmentTypeMainPageHeaderStandard
            style.logoSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.logoSlotContainerHorizontalAlignmentTypeMainPageHeaderStandard
            style.logoSlotContainerVerticalArrangement =
                DSPageHeaderTokens.logoSlotContainerVerticalArrangementTypeMainPageHeaderStandard
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM) {
            style.topSectionMainPageTitleSlotContainerVerticalAlignment =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerVerticalAlignmentTypeMainPageHeaderSlim
            style.topSectionMainPageTitleSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerHorizontalAlignmentTypeMainPageHeaderSlim
            style.topSectionMainPageTitleSlotContainerVerticalArrangement =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerVerticalArrangementTypeMainPageHeaderSlim
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD) {
            style.topSectionMainPageTitleSlotContainerVerticalAlignment =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerVerticalAlignmentTypeMainPageHeaderStandard
            style.topSectionMainPageTitleSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerHorizontalAlignmentTypeMainPageHeaderStandard
            style.topSectionMainPageTitleSlotContainerVerticalArrangement =
                DSPageHeaderTokens.topSectionMainPageTitleSlotContainerVerticalArrangementTypeMainPageHeaderStandard
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM) {
            style.preferredActionsSlotContainerMinWidth =
                DSPageHeaderTokens.preferredActionsSlotContainerMinWidthTypeMainPageHeaderSlim
            style.preferredActionsSlotContainerVerticalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainerVerticalAlignmentTypeMainPageHeaderSlim
            style.preferredActionsSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainerHorizontalAlignmentTypeMainPageHeaderSlim
            style.preferredActionsSlotContainerVerticalArrangement =
                DSPageHeaderTokens.preferredActionsSlotContainerVerticalArrangementTypeMainPageHeaderSlim
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD) {
            style.preferredActionsSlotContainerMinWidth =
                DSPageHeaderTokens.preferredActionsSlotContainerMinWidthTypeMainPageHeaderStandard
            style.preferredActionsSlotContainerVerticalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainerVerticalAlignmentTypeMainPageHeaderStandard
            style.preferredActionsSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainerHorizontalAlignmentTypeMainPageHeaderStandard
            style.preferredActionsSlotContainerVerticalArrangement =
                DSPageHeaderTokens.preferredActionsSlotContainerVerticalArrangementTypeMainPageHeaderStandard
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_SLIM) {
            style.mainPageTitleSlotContainerPadding =
                DSPageHeaderTokens.mainPageTitleSlotContainerPaddingTypeMainPageHeaderSlim
            style.mainPageTitleSlotContainerVerticalAlignment =
                DSPageHeaderTokens.mainPageTitleSlotContainerVerticalAlignmentTypeMainPageHeaderSlim
            style.mainPageTitleSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.mainPageTitleSlotContainerHorizontalAlignmentTypeMainPageHeaderSlim
            style.mainPageTitleSlotContainerVerticalArrangement =
                DSPageHeaderTokens.mainPageTitleSlotContainerVerticalArrangementTypeMainPageHeaderSlim
        }
        if (props.type == ODSPageHeaderType.MAIN_PAGE_HEADER_STANDARD) {
            style.mainPageTitleSlotContainerPadding =
                DSPageHeaderTokens.mainPageTitleSlotContainerPaddingTypeMainPageHeaderStandard
            style.mainPageTitleSlotContainerVerticalAlignment =
                DSPageHeaderTokens.mainPageTitleSlotContainerVerticalAlignmentTypeMainPageHeaderStandard
            style.mainPageTitleSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.mainPageTitleSlotContainerHorizontalAlignmentTypeMainPageHeaderStandard
            style.mainPageTitleSlotContainerVerticalArrangement =
                DSPageHeaderTokens.mainPageTitleSlotContainerVerticalArrangementTypeMainPageHeaderStandard
        }
        style.tabsContainerGap = DSPageHeaderTokens.tabsContainerGap
        style.tabsContainerPadding = DSPageHeaderTokens.tabsContainerPadding
        style.tabsContainerVerticalAlignment = DSPageHeaderTokens.tabsContainerVerticalAlignment
        style.tabsContainerHorizontalAlignment = DSPageHeaderTokens.tabsContainerHorizontalAlignment
        style.tabsContainerVerticalArrangement = DSPageHeaderTokens.tabsContainerVerticalArrangement
        style.dividerContainerVerticalAlignment =
            DSPageHeaderTokens.dividerContainerVerticalAlignment
        style.dividerContainerHorizontalAlignment =
            DSPageHeaderTokens.dividerContainerHorizontalAlignment
        style.dividerContainerVerticalArrangement =
            DSPageHeaderTokens.dividerContainerVerticalArrangement
//        if (!props.scrollable) {
        style.dividerContainerHeight = DSPageHeaderTokens.dividerContainerHeight
//        }
        if (props.type == ODSPageHeaderType.SUB_PAGE_HEADER) {
            style.subPageHeaderTopSectionPadding =
                DSPageHeaderTokens.subPageHeaderTopSectionPaddingTypeSubPageHeader
            style.subPageHeaderTopSectionHeight =
                DSPageHeaderTokens.subPageHeaderTopSectionHeightTypeSubPageHeader
            style.subPageHeaderTopSectionVerticalAlignment =
                DSPageHeaderTokens.subPageHeaderTopSectionVerticalAlignmentTypeSubPageHeader
            style.subPageHeaderTopSectionHorizontalArrangement =
                DSPageHeaderTokens.subPageHeaderTopSectionHorizontalArrangementTypeSubPageHeader
        }
        if (props.type == ODSPageHeaderType.SUB_PAGE_HEADER) {
            style.subPageTitleSlotContainerGap =
                DSPageHeaderTokens.subPageTitleSlotContainerGapTypeSubPageHeader
            style.subPageTitleSlotContainerVerticalAlignment =
                DSPageHeaderTokens.subPageTitleSlotContainerVerticalAlignmentTypeSubPageHeader
            style.subPageTitleSlotContainerHorizontalAlignment =
                DSPageHeaderTokens.subPageTitleSlotContainerHorizontalAlignmentTypeSubPageHeader
            style.subPageTitleSlotContainerVerticalArrangement =
                DSPageHeaderTokens.subPageTitleSlotContainerVerticalArrangementTypeSubPageHeader
        }
        if (props.type == ODSPageHeaderType.SUB_PAGE_HEADER) {
            style.preferredActionsSlotContainer2MinWidth =
                DSPageHeaderTokens.preferredActionsSlotContainer2MinWidthTypeSubPageHeader
            style.preferredActionsSlotContainer2VerticalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainer2VerticalAlignmentTypeSubPageHeader
            style.preferredActionsSlotContainer2HorizontalAlignment =
                DSPageHeaderTokens.preferredActionsSlotContainer2HorizontalAlignmentTypeSubPageHeader
            style.preferredActionsSlotContainer2VerticalArrangement =
                DSPageHeaderTokens.preferredActionsSlotContainer2VerticalArrangementTypeSubPageHeader
        }
        return style
    }
}
