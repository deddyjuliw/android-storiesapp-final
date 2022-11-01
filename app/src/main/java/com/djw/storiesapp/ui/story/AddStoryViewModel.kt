package com.djw.storiesapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djw.storiesapp.data.repository.StoryRepository
import com.djw.storiesapp.data.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun addNew(token: String, photo: MultipartBody.Part, desc: RequestBody, lat: RequestBody?, lon: RequestBody?) = storyRepository.addStories(token, photo, desc, lat, lon)
}