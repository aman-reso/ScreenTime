package com.telekom.odsystem.slots.dialogbottomsheetpreferredactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import com.telekom.odsystem.componenttokens.DSDialogBottomSheetPreferredActionsTokens

class ODSDialogBottomSheetPreferredActionsStyle {
    var gap: Dp? = null
    var verticalAlignment: Alignment.Vertical? = null
    var horizontalAlignment: Alignment.Horizontal? = null
    var horizontalArrangement: Arrangement.Horizontal? = null
    var verticalArrangement: Arrangement.Vertical? = null
    fun getStyle(
        props: ODSDialogBottomSheetPreferredActionsProps
    ): ODSDialogBottomSheetPreferredActionsStyle {
        var style = ODSDialogBottomSheetPreferredActionsStyle()
        style.gap = DSDialogBottomSheetPreferredActionsTokens.gap
        style.verticalAlignment = DSDialogBottomSheetPreferredActionsTokens.verticalAlignment
        style.horizontalAlignment = DSDialogBottomSheetPreferredActionsTokens.horizontalAlignment
        if (props.variant == ODSDialogBottomSheetPreferredActionsVariant.STACKED) {
            style.verticalArrangement =
                DSDialogBottomSheetPreferredActionsTokens.verticalArrangementVariantStacked
        }
        if (props.variant == ODSDialogBottomSheetPreferredActionsVariant.SIDE_BY_SIDE) {
            style.horizontalArrangement =
                DSDialogBottomSheetPreferredActionsTokens.horizontalArrangementVariantSideBySide
        }
        if (props.variant == ODSDialogBottomSheetPreferredActionsVariant.SIDE_BY_SIDE_FILL) {
            style.horizontalArrangement =
                DSDialogBottomSheetPreferredActionsTokens.horizontalArrangementVariantSideBySideFill
        }
        return style
    }
}
