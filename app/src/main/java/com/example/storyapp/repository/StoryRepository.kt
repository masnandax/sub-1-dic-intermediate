package com.example.storyapp.repository

import com.example.storyapp.api.ApiService
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.api.StoriesResponse
import com.example.storyapp.api.StoryResponseItem
import com.example.storyapp.database.StoryDatabase
import retrofit2.Call

class StoryRepository(private val apiService: ApiService) {
    fun getStory(): Call<List<ListStoryItem>> {
        return apiService.getAllStories(1,5)
    }
}