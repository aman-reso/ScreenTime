package com.app.screentime.ui.theme

import android.provider.Settings
import com.telekom.odsystem.neutralScheme
import com.telekom.odsystem.tokens.tokens.ODSTheme
import com.telekom.odsystem.tokens.tokens.aperitifSecondaryScheme
import com.telekom.odsystem.tokens.tokens.basketballSecondaryScheme
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
import com.telekom.odsystem.tokens.tokens.nebulaSecondaryScheme
import com.telekom.odsystem.tokens.tokens.orchidSecondaryScheme

sealed class ColorPalette {
    data class Mobile(val palette: MobilePalette) : ColorPalette()
    data class Home(val palette: HomePalette) : ColorPalette()
    data class TV(val palette: TVPalette) : ColorPalette()
    data class DEFAULT(val palette: DefaultPalette) : ColorPalette()

    enum class MobilePalette(val scheme: ODSTheme) {
        JACUZZI(jacuzziSecondaryScheme),
        IGUANA(iguanaSecondaryScheme),
        HUMMINGBIRD(hummingbirdSecondaryScheme),
        GUACAMOLE(guacamoleSecondaryScheme),
        FROG(frogSecondaryScheme)
    }

    enum class HomePalette(val scheme: ODSTheme) {
        MACAW(macawSecondaryScheme),
        NEBULA(nebulaSecondaryScheme),
        ORCHID(orchidSecondaryScheme),
        KINGFISHER(kingfisherSecondaryScheme),
        LAGOON(lagoonSecondaryScheme)
    }

    enum class TVPalette(val scheme: ODSTheme) {
        APERITIF(aperitifSecondaryScheme),
        BASKETBALL(basketballSecondaryScheme),
        CHEDDAR(cheddarSecondaryScheme),
        DANDELION(dandelionSecondaryScheme),
        EGG(eggSecondaryScheme)
    }

    enum class DefaultPalette(val scheme: ODSTheme) {
        DEFAULT(neutralScheme)
    }

    companion object {
        fun getScheme(currentEnum: Enum<*>): ODSTheme {
            when (currentEnum) {
                is MobilePalette -> {
                    val index = MobilePalette.entries.indexOf(currentEnum)
                    val next = MobilePalette.entries[(index + 1) % MobilePalette.entries.size]
                    return next.scheme
                }

                is HomePalette -> {
                    val index = HomePalette.entries.indexOf(currentEnum)
                    val next = HomePalette.entries[(index + 1) % HomePalette.entries.size]
                    return next.scheme
                }

                is TVPalette -> {
                    val index = TVPalette.entries.indexOf(currentEnum)
                    val next = TVPalette.entries[(index + 1) % TVPalette.entries.size]
                    return next.scheme
                }

                else -> return neutralScheme
            }
        }

        fun schemeGet(scheme: ODSTheme): ODSTheme {
            when (val currentEnum: Enum<*> = getEnumFromScheme(scheme)) {
                is MobilePalette -> {
                    val index = MobilePalette.entries.indexOf(currentEnum)
                    val next = MobilePalette.entries[(index + 1) % MobilePalette.entries.size]
                    return next.scheme
                }

                is HomePalette -> {
                    val index = HomePalette.entries.indexOf(currentEnum)
                    val next = HomePalette.entries[(index + 1) % HomePalette.entries.size]
                    return next.scheme
                }

                is TVPalette -> {
                    val index = TVPalette.entries.indexOf(currentEnum)
                    val next = TVPalette.entries[(index + 1) % TVPalette.entries.size]
                    return next.scheme
                }

                else -> return neutralScheme
            }
        }

        fun getEnumFromScheme(currentScheme: ODSTheme): Enum<*> {
            MobilePalette.entries.find { it.scheme == currentScheme }?.let { return it }
            HomePalette.entries.find { it.scheme == currentScheme }?.let { return it }
            TVPalette.entries.find { it.scheme == currentScheme }?.let { return it }
            return DefaultPalette.DEFAULT
        }
    }

    sealed class BenefitScheme(val scheme: ODSTheme) {
        data object ORCHID : BenefitScheme(orchidSecondaryScheme)
        data object NEBULA : BenefitScheme(nebulaSecondaryScheme)
        data object MACAW : BenefitScheme(macawSecondaryScheme)
        data object LAGOON : BenefitScheme(lagoonSecondaryScheme)
        data object KINGFISHER : BenefitScheme(kingfisherSecondaryScheme)
        data object JACUZZI : BenefitScheme(jacuzziSecondaryScheme)
        data object IGUANA : BenefitScheme(iguanaSecondaryScheme)
        data object HUMMINGBIRD : BenefitScheme(hummingbirdSecondaryScheme)
        data object GUACAMOLE : BenefitScheme(guacamoleSecondaryScheme)
        data object FROG : BenefitScheme(frogSecondaryScheme)
        data object EGG : BenefitScheme(eggSecondaryScheme)
        data object DANDELION : BenefitScheme(dandelionSecondaryScheme)
        data object CHEDDAR : BenefitScheme(cheddarSecondaryScheme)
        data object BASKETBALL : BenefitScheme(basketballSecondaryScheme)
        data object APERITIF : BenefitScheme(aperitifSecondaryScheme)

        companion object {
            val values = listOf(
                ORCHID, NEBULA, MACAW, LAGOON, KINGFISHER,
                JACUZZI, IGUANA, HUMMINGBIRD, GUACAMOLE, FROG,
                EGG, DANDELION, CHEDDAR, BASKETBALL, APERITIF
            )

            fun pickBenefitScheme(index: Int): ODSTheme = values[index % values.size].scheme

            fun pickSchemeRandom() =
                pickBenefitScheme((0..BenefitScheme.values.size).random())
        }
    }


}