package com.umc.oppla.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.umc.oppla.data.local.DataStore.PreferenceKeys.ACCESS_TOKEN
import com.umc.oppla.data.local.DataStore.PreferenceKeys.DATASTORE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStore(private val context: Context) {

    private object PreferenceKeys {
        const val DATASTORE = "datastore"
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    // At the top level of your kotlin file:
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE)

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
        }
    }

    fun getToken(): Flow<String> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[ACCESS_TOKEN] ?: ""
            }
    }

}