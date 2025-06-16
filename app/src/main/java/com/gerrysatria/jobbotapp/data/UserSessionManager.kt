package com.gerrysatria.jobbotapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserSessionManager(private val context: Context) {

    companion object {
        private val KEY_USER_ID = intPreferencesKey("user_id")
    }

    val userIdFlow: Flow<Int?>
        get() = context.dataStore.data
            .map { prefs ->
                prefs[KEY_USER_ID]
            }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
        }
    }

    suspend fun clearUserId() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_USER_ID)
        }
    }
}