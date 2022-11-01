package com.djw.storiesapp.ui.maps

import androidx.lifecycle.ViewModel
import com.djw.storiesapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoryMaps(token: String) = storyRepository.getMaps(token)
}