
package com.example.myapplication.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "settings"
)

private val DARK_THEME = booleanPreferencesKey("dark_theme")
private val DAILY_REMINDER = booleanPreferencesKey("daily_reminder")

class SettingsDataStore(private val context: Context) {

    val darkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME] ?: false
        }

    val dailyReminder: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DAILY_REMINDER] ?: false
        }

    suspend fun saveDarkTheme(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME] = value
        }
    }

    suspend fun saveDailyReminder(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_REMINDER] = value
        }
    }
}
