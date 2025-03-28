package com.advancedquiz.social

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SoundPreferences(private val dataStore: AppDataStore) {
    companion object {
        private val SOUNDS_ENABLED = booleanPreferencesKey("sounds_enabled")
        private val SOUND_VOLUME = floatPreferencesKey("sound_volume")
    }

    val soundsEnabled: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[SOUNDS_ENABLED] ?: true }

    val soundVolume: Flow<Float> = dataStore.data
        .map { preferences -> preferences[SOUND_VOLUME] ?: 0.5f }

    suspend fun setSoundsEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[SOUNDS_ENABLED] = enabled
        }
    }

    suspend fun setSoundVolume(volume: Float) {
        dataStore.edit { settings ->
            settings[SOUND_VOLUME] = volume.coerceIn(0f, 1f)
        }
    }
}