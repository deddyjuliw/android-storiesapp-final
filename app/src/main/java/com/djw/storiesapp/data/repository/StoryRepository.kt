package com.djw.storiesapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.djw.storiesapp.data.Result
import com.djw.storiesapp.data.StoryRemoteMediator
import com.djw.storiesapp.data.database.StoryDatabase
import com.djw.storiesapp.data.response.AddResponse
import com.djw.storiesapp.data.response.GetStoryResponse
import com.djw.storiesapp.data.response.ListStoryItem
import com.djw.storiesapp.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase) {
    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

    fun addStories(token: String, photo: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?) : LiveData<Result<AddResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.addNew("Bearer $token", photo, desc, lat, lon)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getMaps(token: String) : LiveData<Result<GetStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getStories("Bearer $token", location = 1)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}