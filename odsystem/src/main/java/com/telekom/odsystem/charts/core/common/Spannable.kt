package com.telekom.odsystem.charts.core.common

import android.text.SpannableStringBuilder

internal fun SpannableStringBuilder.appendCompat(
    text: CharSequence,
    what: Any,
    flags: Int,
): SpannableStringBuilder = append(text, what, flags)
