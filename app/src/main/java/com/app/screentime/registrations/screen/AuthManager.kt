package com.app.screentime.registrations.screen

import android.util.Log
import com.app.screentime.core.network.UnauthorizedHandler
import com.app.screentime.login.usecase.LoginUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// app-module
class AuthManager : UnauthorizedHandler {

    private var scope: CoroutineScope? = null
    private var useCase: LoginUseCase? = null

    private val mutex = Mutex()

    fun init(
        scope: CoroutineScope,
        useCase: LoginUseCase,
    ) {
        this.scope = scope
        this.useCase = useCase
    }

    override fun onUnauthorized() {
        try {
            scope?.launch(Dispatchers.Default) {
                mutex.withLock {
                    useCase?.registerDevice()
                }
            }
        } catch (e: Throwable) {
            Log.e("AuthManager", "Auto register failed", e)
        }
    }
}