package com.example.quickorderapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session_prefs")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val USER_ROLE = stringPreferencesKey("user_role")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_EMAIL = stringPreferencesKey("user_email")

    val userRole: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[USER_ROLE] ?: "COMENSAL"
    }

    val userName: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[USER_NAME] ?: "Usuario QuickOrder"
    }

    val userEmail: Flow<String> = context.sessionDataStore.data.map { prefs ->
        prefs[USER_EMAIL] ?: ""
    }

    suspend fun saveSession(name: String, email: String, role: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
            prefs[USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
