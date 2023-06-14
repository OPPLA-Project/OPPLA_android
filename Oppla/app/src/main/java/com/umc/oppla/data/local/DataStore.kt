package com.umc.oppla.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.umc.oppla.data.local.DataStore.PreferenceKeys.ACCESS_TOKEN
import com.umc.oppla.data.local.DataStore.PreferenceKeys.BACKGROUND_PERMISSION_STATE
import com.umc.oppla.data.local.DataStore.PreferenceKeys.DATASTORE
import com.umc.oppla.data.local.DataStore.PreferenceKeys.LOCATION_SEARCH_HISTORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStore(private val context: Context) {

    private object PreferenceKeys {
        const val DATASTORE = "datastore"

        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val BACKGROUND_PERMISSION_STATE = booleanPreferencesKey("background_permission_state")
        val LOCATION_SEARCH_HISTORY = stringSetPreferencesKey("location_search_history")
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

    suspend fun savePermissionState(permissionstate: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[BACKGROUND_PERMISSION_STATE] = permissionstate
        }
    }

    fun getPermissionState(): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[BACKGROUND_PERMISSION_STATE] ?: false
            }
    }

    suspend fun saveSearchHistory(searchword: String) {
        context.dataStore.edit { prefs ->
            if(prefs[LOCATION_SEARCH_HISTORY].isNullOrEmpty()){
                prefs[LOCATION_SEARCH_HISTORY] = linkedSetOf(searchword)
            }else{
                val current = linkedSetOf<String>()
                current.addAll(prefs[LOCATION_SEARCH_HISTORY]?.toSet()!!)
                if(current.contains(searchword)){
                    current.remove(searchword)
                    prefs[LOCATION_SEARCH_HISTORY] = current
                }
            }
        }

        context.dataStore.edit { prefs ->
            if(prefs[LOCATION_SEARCH_HISTORY].isNullOrEmpty()){
                prefs[LOCATION_SEARCH_HISTORY] = linkedSetOf(searchword)
            }else{
                val current = linkedSetOf<String>()
                current.addAll(prefs[LOCATION_SEARCH_HISTORY]?.toSet()!!)
                current.add(searchword)
                prefs[LOCATION_SEARCH_HISTORY] = current
            }

        }
    }

    suspend fun deleteSearchHistory(searchword: String) {
        context.dataStore.edit { prefs ->
            if(prefs[LOCATION_SEARCH_HISTORY].isNullOrEmpty()){
            }else{
                val current = linkedSetOf<String>()
                current.addAll(prefs[LOCATION_SEARCH_HISTORY]?.toSet()!!)
                current.remove(searchword)
                prefs[LOCATION_SEARCH_HISTORY] = current
            }

        }
    }

    fun getSearchHistory(): Flow<Set<String>> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[LOCATION_SEARCH_HISTORY] ?: setOf()
            }
    }

}