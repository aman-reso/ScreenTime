package com.telekom.odsystem.foundations

/**
 * Created by dmarinopoulos on 3/7/24
 */

@Suppress("MagicNumber")
enum class ODSAspectRatio(val value: Float) {
    VALUE_1_1(1f),
    VALUE_5_4(5f / 4f),
    VALUE_4_5(4f / 5f),
    VALUE_4_3(4f / 3f),
    VALUE_3_4(3f / 4f),
    VALUE_3_2(3f / 2f),
    VALUE_2_3(2f / 3f),
    VALUE_16_9(16f / 9f),
    VALUE_9_16(9f / 16f),
    VALUE_2_1(2f / 1f),
    VALUE_1_2(1f / 2f),
    VALUE_3_1(3f / 1f),
}
