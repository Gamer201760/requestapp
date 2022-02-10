package com.gglamer.icebox

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreTokenData(private val context: Context) {
    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("userToken")
        val USER_TOKEN = stringPreferencesKey("userToken")
    }

    //get the saved email
    val getToken: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_TOKEN] ?: "Token don't store"
        }

    //save email into datastore
    suspend fun saveToken(token: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }
}