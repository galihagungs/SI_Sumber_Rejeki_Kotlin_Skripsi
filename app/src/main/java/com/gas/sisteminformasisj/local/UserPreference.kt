package com.gas.sisteminformasisj.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<UserModelDB> {
        return dataStore.data.map { preferences ->
            UserModelDB(
                preferences[ID_USER_KEY] ?: 0,
                preferences[USERNAME_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[NAMA_KEY] ?: "",
                preferences[ROLES_KEY] ?:"",
                preferences[TOKEN_KEY] ?:"",
                preferences[ISLOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: UserModelDB) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = user.id_user
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
            preferences[NAMA_KEY] = user.nama
            preferences[ROLES_KEY] = user.roles
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun simpanUser(id_user: Int,username: String, password: String, nama: String, roles: String) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = id_user
            preferences[USERNAME_KEY] = username
            preferences[PASSWORD_KEY] = password
            preferences[NAMA_KEY] = nama
            preferences[ROLES_KEY] = roles
        }
    }

    suspend fun login(token: String, id_user : Int) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = id_user
            preferences[TOKEN_KEY] = token
            preferences[ISLOGIN_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = "null"
            preferences[ISLOGIN_KEY] = false
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_USER_KEY = intPreferencesKey("id_user")
        private val USERNAME_KEY =  stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val NAMA_KEY = stringPreferencesKey("nama")
        private val ROLES_KEY = stringPreferencesKey("roles")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ISLOGIN_KEY = booleanPreferencesKey("islogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}