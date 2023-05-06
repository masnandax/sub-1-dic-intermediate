package com.example.storyapp.ui.addStory

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCameraViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _responseMsg = MutableLiveData<String>()
    val responseMsg : LiveData<String> = _responseMsg

    fun uploadImageData(context: Context, imageMultipart : MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val service = ApiConfig.getApiService(context).uploadImage(imageMultipart, description)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _responseMsg.postValue(responseBody.message.toString())
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Toast.makeText(context, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }
        })
    }
}