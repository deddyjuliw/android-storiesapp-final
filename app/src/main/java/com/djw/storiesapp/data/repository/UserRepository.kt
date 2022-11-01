package com.djw.storiesapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.data.response.LoginResponse
import com.djw.storiesapp.data.response.RegisterResponse

import com.djw.storiesapp.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class UserRepository private constructor(private val dataStore: DataStore<Preferences>, private val apiService: ApiService){
    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        } catch (throwable: HttpException) {
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            } catch (exception: java.lang.Exception){
                emit(Result.Error(exception.message.toString()))
            }
        }
    }

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        } catch (throwable: HttpException) {
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Result.Error(it.toString()))
                }
            } catch (exception: Exception) {
                emit(Result.Error(exception.message.toString()))
            }
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserRepository? = null
        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}