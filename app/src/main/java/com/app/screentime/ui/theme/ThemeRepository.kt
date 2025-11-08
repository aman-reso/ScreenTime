package com.app.screentime.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.app.screentime.di.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val themeKey = stringPreferencesKey("theme")

    val theme: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[themeKey] ?: "System"
        }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit {
            it[themeKey] = theme
        }
    }
}
