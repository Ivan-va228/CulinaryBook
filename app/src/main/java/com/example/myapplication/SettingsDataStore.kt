
package com.example.myapplication

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

private val SORT_BY_TIME = booleanPreferencesKey("sort_by_time")
private val DARK_THEME = booleanPreferencesKey("dark_theme")

class SettingsDataStore(private val context: Context) {

    val sortByTime: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SORT_BY_TIME] ?: false
        }

    suspend fun saveSortByTime(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SORT_BY_TIME] = value
        }
    }

    val darkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME] ?: false
        }

    suspend fun saveDarkTheme(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME] = value
        }
    }
}
