package com.app.screentime.ui.language

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.app.screentime.di.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LanguageRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private val languageKey = stringPreferencesKey("language")

    val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[languageKey] ?: "en"
        }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit {
            it[languageKey] = language
        }
    }
}
