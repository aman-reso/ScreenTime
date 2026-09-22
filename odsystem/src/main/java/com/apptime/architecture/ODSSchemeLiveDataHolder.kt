package com.telekom.odsystem


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.telekom.odsystem.tokens.tokens.ODSTheme
import kotlinx.coroutines.flow.MutableSharedFlow

object ODSThemeLiveDataHolder {
    private val ODSThemeLiveData: MutableSharedFlow<ODSTheme> =
        MutableSharedFlow(0)

    fun getODSThemeLiveData(): MutableSharedFlow<ODSTheme> {
        return ODSThemeLiveData
    }

    internal fun updateODSTheme(newODSTheme: ODSTheme) {
        ODSThemeLiveData.tryEmit(newODSTheme)
    }
}
