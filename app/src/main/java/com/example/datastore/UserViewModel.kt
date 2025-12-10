package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserViewModel: ViewModel() {

    // Mini Base de datos
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("BBDD")

    val userKey = stringPreferencesKey("userName")
    val isDarkTheme = booleanPreferencesKey("darkTheme")
    val counter = intPreferencesKey("counter")
    // Función genérica --> T --> Lectura del dataStore
    fun <T> getData(context: Context, key: Preferences.Key<T>): Flow<T> {
        return context.dataStore.data.map { preferences -> (preferences[key] ?: "") as T }
    }

    // Función genérica --> T --> Escritura/editar dataStore
    // Función "suspendible" que funciona como un hilo (función asíncrona)
    suspend fun <T> setData(context: Context, key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences -> preferences[key] = value }
    }
}