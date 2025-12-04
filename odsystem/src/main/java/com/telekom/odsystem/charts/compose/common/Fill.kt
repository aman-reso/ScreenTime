package com.telekom.odsystem.charts.compose.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.telekom.odsystem.charts.core.common.Fill
import com.telekom.odsystem.charts.core.common.shader.ShaderProvider

/** Creates a [Fill]. */
public fun fill(color: Color): Fill = Fill(color.toArgb())

/** Creates a [Fill]. */
public fun fill(shaderProvider: ShaderProvider): Fill = Fill(shaderProvider)
