package com.telekom.odsystem.charts.compose.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.telekom.odsystem.charts.core.common.ValueWrapper

@Composable
internal fun <T> rememberWrappedValue(value: T): ValueWrapper<T> =
    remember { ValueWrapper(value) }.also { it.value = value }
