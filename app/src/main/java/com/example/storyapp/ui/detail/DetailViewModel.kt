package com.example.storyapp.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Story
import com.example.storyapp.api.StoryDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
     private val _isLoading = MutableLiveData<Boolean>()
     val loading: LiveData<Boolean> = _isLoading

    private val _dataStory = MutableLiveData<Story>()
    val dataStory : LiveData<Story> = _dataStory

    fun showDetailUser(context: Context, userId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).getStoriesById(userId)
        client.enqueue(object: Callback<StoryDetailResponse> {
            override fun onResponse(call: Call<StoryDetailResponse>, response: Response<StoryDetailResponse>) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        if(responseBody.error == false && responseBody.story != null) {
                            _dataStory.postValue(responseBody.story!!)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                Log.d(TAG, "message : ${t.message}")
            }

        })
    }

    companion object {
        const val TAG = "DetailActivity"
    }
}