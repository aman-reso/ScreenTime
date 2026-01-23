package com.app.screentime.core.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// network-module
interface UnauthorizedHandler {
    fun onUnauthorized()
}

object NetworkAuthBridge {
    var unauthorizedHandler: UnauthorizedHandler? = null
}

