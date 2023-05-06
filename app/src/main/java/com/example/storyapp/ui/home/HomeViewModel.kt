package com.example.storyapp.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ListStoryItem
import com.example.storyapp.api.StoriesResponse
import com.example.storyapp.di.Injection
import com.example.storyapp.repository.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun listStory(context:Context) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getAllStories(1,5)
        client.enqueue(object: Callback<List<ListStoryItem>> {
            override fun onResponse(
                call: Call<List<ListStoryItem>>,
                response: Response<List<ListStoryItem>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    _listStory.postValue(response?.body())
                } else {
                     Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListStoryItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}
