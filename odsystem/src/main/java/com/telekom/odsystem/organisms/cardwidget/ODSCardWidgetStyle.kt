package com.telekom.odsystem.organisms.cardwidget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.foundations.ODSActions
import com.telekom.odsystem.foundations.ODSColorModel
import com.telekom.odsystem.foundations.ODSCorners
import com.telekom.odsystem.foundations.ODSOffset
import com.telekom.odsystem.foundations.ODSPadding
import com.telekom.odsystem.tokens.tokens.ODSTheme

@Suppress("LongMethod")
class ODSCardWidgetStyle {
    var minHeight: Dp? = null
    var minWidth: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    var cornerRadius: ODSCorners? = null
    var topImageContainerZStackContentAlignment: Alignment? = null
    var topImageContainerVerticalAlignment: Alignment.Vertical? = null
    var topImageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var topImageContainerVerticalArrangement: Arrangement.Vertical? = null
    var topImageContainerContentAlignment: Alignment? = null
    var imageTopZStackClipContent: Boolean? = null // Not used in mobile
    var imageTopCornerRadius: ODSCorners? = null
    var imageTopVerticalAlignment: Alignment.Vertical? = null
    var imageTopHorizontalAlignment: Alignment.Horizontal? = null
    var imageTopVerticalArrangement: Arrangement.Vertical? = null
    var imageContentScale: ContentScale? = null
    var logoContainerAbsoluteOffset: ODSOffset? = null
    var logoContainerAbsoluteContentAlignment: Alignment? = null
    var logoContainerCornerRadius: ODSCorners? = null
    var logoContainerHeight: Dp? = null
    var logoContainerWidth: Dp? = null
    var logoContainerVerticalAlignment: Alignment.Vertical? = null
    var logoContainerHorizontalAlignment: Alignment.Horizontal? = null
    var logoContainerVerticalArrangement: Arrangement.Vertical? = null
    var contentContainerZStackMinHeight: Dp? = null
    var contentContainerZStackContentAlignment: Alignment? = null
    var contentContainerPadding: ODSPadding? = null
    var contentContainerMinHeight: Dp? = null
    var contentContainerVerticalAlignment: Alignment.Vertical? = null
    var contentContainerHorizontalAlignment: Alignment.Horizontal? = null
    var contentContainerVerticalArrangement: Arrangement.Vertical? = null
    var contentContainerContentAlignment: Alignment? = null
    var contentContainerGap: Dp? = null
    var backgroundBackground: List<ODSColorModel>? = null
    var backgroundCornerRadius: ODSCorners? = null
    var backgroundVerticalAlignment: Alignment.Vertical? = null
    var backgroundHorizontalAlignment: Alignment.Horizontal? = null
    var backgroundVerticalArrangement: Arrangement.Vertical? = null
    var bottomImageContainerZStackContentAlignment: Alignment? = null
    var bottomImageContainerVerticalAlignment: Alignment.Vertical? = null
    var bottomImageContainerHorizontalAlignment: Alignment.Horizontal? = null
    var bottomImageContainerVerticalArrangement: Arrangement.Vertical? = null
    var bottomImageContainerContentAlignment: Alignment? = null
    var imageBottomZStackClipContent: Boolean? = null // Not used in mobile
    var imageBottomCornerRadius: ODSCorners? = null
    var imageBottomVerticalAlignment: Alignment.Vertical? = null
    var imageBottomHorizontalAlignment: Alignment.Horizontal? = null
    var imageBottomVerticalArrangement: Arrangement.Vertical? = null
    var image2ContentScale: ContentScale? = null
    var logoContainer2AbsoluteOffset: ODSOffset? = null
    var logoContainer2AbsoluteContentAlignment: Alignment? = null
    var logoContainer2CornerRadius: ODSCorners? = null
    var logoContainer2Width: Dp? = null
    var logoContainer2Height: Dp? = null
    var logoContainer2VerticalAlignment: Alignment.Vertical? = null
    var logoContainer2HorizontalAlignment: Alignment.Horizontal? = null
    var logoContainer2VerticalArrangement: Arrangement.Vertical? = null
    var scaleFactor: Float? = null // Not exported from the plugin
    var imageVerticalOffset: Dp? = null // Not exported from the plugin
    var imageTopHeight: Dp? = null // Not exported from the plugin
    var imageBottomHeight: Dp? = null // Not exported from the plugin

    fun getStyle(
        scheme: ODSTheme,
        props: ODSCardWidgetProps,
        state: ODSActions,
    ): ODSCardWidgetStyle {
        val style = ODSCardWidgetStyle()
        style.minHeight = DSCardWidgetTokens.minHeight
        style.minWidth = DSCardWidgetTokens.minWidth
        style.verticalAlignment = DSCardWidgetTokens.verticalAlignment
        style.verticalArrangement = DSCardWidgetTokens.verticalArrangement
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.horizontalAlignment = DSCardWidgetTokens.horizontalAlignmentTypeNoImage
            style.cornerRadius = DSCardWidgetTokens.cornerRadiusTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.horizontalAlignment = DSCardWidgetTokens.horizontalAlignmentTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.horizontalAlignment = DSCardWidgetTokens.horizontalAlignmentTypeBottomImage
        }
        style.topImageContainerZStackContentAlignment =
            DSCardWidgetTokens.topImageContainerZStackContentAlignment
        style.topImageContainerVerticalAlignment =
            DSCardWidgetTokens.topImageContainerVerticalAlignment
        style.topImageContainerHorizontalAlignment =
            DSCardWidgetTokens.topImageContainerHorizontalAlignment
        style.topImageContainerVerticalArrangement =
            DSCardWidgetTokens.topImageContainerVerticalArrangement
        style.topImageContainerContentAlignment =
            DSCardWidgetTokens.topImageContainerContentAlignment
        style.imageTopZStackClipContent = DSCardWidgetTokens.imageTopZStackClipContent
        style.imageTopCornerRadius = DSCardWidgetTokens.imageTopCornerRadius
        style.imageTopVerticalAlignment = DSCardWidgetTokens.imageTopVerticalAlignment
        style.imageTopHorizontalAlignment = DSCardWidgetTokens.imageTopHorizontalAlignment
        style.imageTopVerticalArrangement = DSCardWidgetTokens.imageTopVerticalArrangement
        style.imageContentScale = DSCardWidgetTokens.imageContentScale
        style.logoContainerAbsoluteContentAlignment =
            DSCardWidgetTokens.logoContainerAbsoluteContentAlignment
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeBottomImage
        }
        /*if (props.type == ODSCardWidgetType.TOP_IMAGE && state == ODSActions.PRESSED) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeTopImageStatePressed
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE && state == ODSActions.PRESSED) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeBottomImageStatePressed
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE && state == ODSActions.HOVERED) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeTopImageStateHovered
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE && state == ODSActions.HOVERED) {
            style.logoContainerAbsoluteOffset =
                DSCardWidgetTokens.logoContainerAbsoluteOffsetTypeBottomImageStateHovered
        }*/
        style.logoContainerCornerRadius = DSCardWidgetTokens.logoContainerCornerRadius
        style.logoContainerHeight = DSCardWidgetTokens.logoContainerHeight
        style.logoContainerWidth = DSCardWidgetTokens.logoContainerWidth
        style.logoContainerVerticalAlignment = DSCardWidgetTokens.logoContainerVerticalAlignment
        style.logoContainerHorizontalAlignment = DSCardWidgetTokens.logoContainerHorizontalAlignment
        style.logoContainerVerticalArrangement = DSCardWidgetTokens.logoContainerVerticalArrangement
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.contentContainerZStackMinHeight =
                DSCardWidgetTokens.contentContainerZStackMinHeightTypeNoImage
            style.contentContainerZStackContentAlignment =
                DSCardWidgetTokens.contentContainerZStackContentAlignmentTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.contentContainerZStackMinHeight =
                DSCardWidgetTokens.contentContainerZStackMinHeightTypeTopImage
            style.contentContainerZStackContentAlignment =
                DSCardWidgetTokens.contentContainerZStackContentAlignmentTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.contentContainerZStackMinHeight =
                DSCardWidgetTokens.contentContainerZStackMinHeightTypeBottomImage
            style.contentContainerZStackContentAlignment =
                DSCardWidgetTokens.contentContainerZStackContentAlignmentTypeBottomImage
        }
        style.contentContainerVerticalAlignment =
            DSCardWidgetTokens.contentContainerVerticalAlignment
        style.contentContainerVerticalArrangement =
            DSCardWidgetTokens.contentContainerVerticalArrangement
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.contentContainerPadding = DSCardWidgetTokens.contentContainerPaddingTypeNoImage
            style.contentContainerMinHeight =
                DSCardWidgetTokens.contentContainerMinHeightTypeNoImage
            style.contentContainerHorizontalAlignment =
                DSCardWidgetTokens.contentContainerHorizontalAlignmentTypeNoImage
            style.contentContainerContentAlignment =
                DSCardWidgetTokens.contentContainerContentAlignmentTypeNoImage
            style.contentContainerGap = DSCardWidgetTokens.contentContainerGapTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.contentContainerPadding = DSCardWidgetTokens.contentContainerPaddingTypeTopImage
            style.contentContainerMinHeight =
                DSCardWidgetTokens.contentContainerMinHeightTypeTopImage
            style.contentContainerHorizontalAlignment =
                DSCardWidgetTokens.contentContainerHorizontalAlignmentTypeTopImage
            style.contentContainerContentAlignment =
                DSCardWidgetTokens.contentContainerContentAlignmentTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.contentContainerPadding =
                DSCardWidgetTokens.contentContainerPaddingTypeBottomImage
            style.contentContainerMinHeight =
                DSCardWidgetTokens.contentContainerMinHeightTypeBottomImage
            style.contentContainerHorizontalAlignment =
                DSCardWidgetTokens.contentContainerHorizontalAlignmentTypeBottomImage
            style.contentContainerContentAlignment =
                DSCardWidgetTokens.contentContainerContentAlignmentTypeBottomImage
        }
        if (props.subtle) {
            style.backgroundBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCardSubtle))
        }
        if (!props.subtle) {
            style.backgroundBackground =
                listOf(ODSColorModel(hexColor = scheme.basicBackgroundCard))
        }
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.backgroundCornerRadius = DSCardWidgetTokens.backgroundCornerRadiusTypeNoImage
            style.backgroundVerticalAlignment =
                DSCardWidgetTokens.backgroundVerticalAlignmentTypeNoImage
            style.backgroundHorizontalAlignment =
                DSCardWidgetTokens.backgroundHorizontalAlignmentTypeNoImage
            style.backgroundVerticalArrangement =
                DSCardWidgetTokens.backgroundVerticalArrangementTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.backgroundCornerRadius = DSCardWidgetTokens.backgroundCornerRadiusTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.backgroundCornerRadius = DSCardWidgetTokens.backgroundCornerRadiusTypeBottomImage
        }
        style.bottomImageContainerZStackContentAlignment =
            DSCardWidgetTokens.bottomImageContainerZStackContentAlignment
        style.bottomImageContainerVerticalAlignment =
            DSCardWidgetTokens.bottomImageContainerVerticalAlignment
        style.bottomImageContainerHorizontalAlignment =
            DSCardWidgetTokens.bottomImageContainerHorizontalAlignment
        style.bottomImageContainerVerticalArrangement =
            DSCardWidgetTokens.bottomImageContainerVerticalArrangement
        style.bottomImageContainerContentAlignment =
            DSCardWidgetTokens.bottomImageContainerContentAlignment
        style.imageBottomZStackClipContent = DSCardWidgetTokens.imageBottomZStackClipContent
        style.imageBottomCornerRadius = DSCardWidgetTokens.imageBottomCornerRadius
        style.imageBottomVerticalAlignment = DSCardWidgetTokens.imageBottomVerticalAlignment
        style.imageBottomHorizontalAlignment = DSCardWidgetTokens.imageBottomHorizontalAlignment
        style.imageBottomVerticalArrangement = DSCardWidgetTokens.imageBottomVerticalArrangement
        style.image2ContentScale = DSCardWidgetTokens.image2ContentScale
        style.logoContainer2AbsoluteContentAlignment =
            DSCardWidgetTokens.logoContainer2AbsoluteContentAlignment
        if (props.type == ODSCardWidgetType.NO_IMAGE) {
            style.logoContainer2AbsoluteOffset =
                DSCardWidgetTokens.logoContainer2AbsoluteOffsetTypeNoImage
        }
        if (props.type == ODSCardWidgetType.TOP_IMAGE) {
            style.logoContainer2AbsoluteOffset =
                DSCardWidgetTokens.logoContainer2AbsoluteOffsetTypeTopImage
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE) {
            style.logoContainer2AbsoluteOffset =
                DSCardWidgetTokens.logoContainer2AbsoluteOffsetTypeBottomImage
        }
        /*if (props.type == ODSCardWidgetType.BOTTOM_IMAGE && state == ODSActions.PRESSED) {
            style.logoContainer2AbsoluteOffset =
                DSCardWidgetTokens.logoContainer2AbsoluteOffsetTypeBottomImageStatePressed
        }
        if (props.type == ODSCardWidgetType.BOTTOM_IMAGE && state == ODSActions.HOVERED) {
            style.logoContainer2AbsoluteOffset =
                DSCardWidgetTokens.logoContainer2AbsoluteOffsetTypeBottomImageStateHovered
        }*/
        style.logoContainer2CornerRadius = DSCardWidgetTokens.logoContainer2CornerRadius
        style.logoContainer2Width = DSCardWidgetTokens.logoContainer2Width
        style.logoContainer2Height = DSCardWidgetTokens.logoContainer2Height
        style.logoContainer2VerticalAlignment = DSCardWidgetTokens.logoContainer2VerticalAlignment
        style.logoContainer2HorizontalAlignment =
            DSCardWidgetTokens.logoContainer2HorizontalAlignment
        style.logoContainer2VerticalArrangement =
            DSCardWidgetTokens.logoContainer2VerticalArrangement

        style.scaleFactor = DSCardWidgetTokens.scaleFactor // Not exported from the plugin
        style.imageVerticalOffset =
            DSCardWidgetTokens.verticalImageOffset // Not exported from the plugin
        style.imageTopHeight = DSCardWidgetTokens.imageTopHeight // Not exported from the plugin
        style.imageBottomHeight =
            DSCardWidgetTokens.imageBottomHeight // Not exported from the plugin
        return style
    }
}
