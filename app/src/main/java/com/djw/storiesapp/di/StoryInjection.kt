package com.djw.storiesapp.di

import android.content.Context
import com.djw.storiesapp.data.database.StoryDatabase
import com.djw.storiesapp.data.repository.StoryRepository
import com.djw.storiesapp.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}