package com.app.screentime.customisation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.common.util.Hex
import com.telekom.odsystem.foundations.HexColor
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.aperitifSecondaryScheme
import com.telekom.odsystem.tokens.tokens.basketballSecondaryScheme
import com.telekom.odsystem.tokens.tokens.blackScheme
import com.telekom.odsystem.tokens.tokens.cheddarSecondaryScheme
import com.telekom.odsystem.tokens.tokens.dandelionSecondaryScheme
import com.telekom.odsystem.tokens.tokens.eggSecondaryScheme
import com.telekom.odsystem.tokens.tokens.frogSecondaryScheme
import com.telekom.odsystem.tokens.tokens.guacamoleSecondaryScheme
import com.telekom.odsystem.tokens.tokens.hummingbirdSecondaryScheme
import com.telekom.odsystem.tokens.tokens.iguanaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.jacuzziSecondaryScheme
import com.telekom.odsystem.tokens.tokens.kingfisherSecondaryScheme
import com.telekom.odsystem.tokens.tokens.lagoonSecondaryScheme
import com.telekom.odsystem.tokens.tokens.macawSecondaryScheme
import com.telekom.odsystem.tokens.tokens.magentaScheme
import com.telekom.odsystem.tokens.tokens.nebulaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme
import com.telekom.odsystem.tokens.tokens.whiteScheme

/**
 * Represents a color option using an existing ODS scheme.
 * Instead of hardcoding colors, we leverage the design system's predefined schemes.
 */

@Immutable
data class ColorOption(
    val id: String,
    val scheme: ODSTheme,
) {
    companion object {

        val DEFAULT_PALETTE: List<ColorOption> = listOf(
            ColorOption(
                id = "default",
                scheme = neutralScheme,
            ),
            ColorOption(
                id = "aperitif",
                scheme = aperitifSecondaryScheme,
            ),
            ColorOption(
                id = "basketball",
                scheme = basketballSecondaryScheme,
            ),
            ColorOption(
                id = "cheddar",
                scheme = cheddarSecondaryScheme,

                ),
            ColorOption(
                id = "dandelion",
                scheme = dandelionSecondaryScheme,
            ),
            ColorOption(
                id = "egg",
                scheme = eggSecondaryScheme,
            ),
            ColorOption(
                id = "frog",
                scheme = frogSecondaryScheme,
            ),
            ColorOption(
                id = "guacamole",
                scheme = guacamoleSecondaryScheme,
            ),
            ColorOption(
                id = "hummingbird",
                scheme = hummingbirdSecondaryScheme,
            ),
            ColorOption(
                id = "iguana",
                scheme = iguanaSecondaryScheme,
            ),
            ColorOption(
                id = "jacuzzi",
                scheme = jacuzziSecondaryScheme,
            ),
            ColorOption(
                id = "kingfisher",
                scheme = kingfisherSecondaryScheme,
            ),
            ColorOption(
                id = "lagoon",
                scheme = lagoonSecondaryScheme,

                ),
            ColorOption(
                id = "macaw",
                scheme = macawSecondaryScheme,

                ),
            ColorOption(
                id = "nebula",
                scheme = nebulaSecondaryScheme,

                ),
            ColorOption(
                id = "orchid",
                scheme = orchidSecondaryScheme,
            )
        )
    }
}


