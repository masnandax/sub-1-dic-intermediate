package com.example.storyapp.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.SignIn
import com.example.storyapp.api.UserResponse
import com.example.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel:ViewModel(){
      private val _isLoading = MutableLiveData<Boolean>()
      val isLoading: LiveData<Boolean> = _isLoading

      private val _token = MutableLiveData<String>()
      val token: LiveData<String> = _token

      private val _response = MutableLiveData<String>()
      val response: LiveData<String> = _response

      fun login(context: Context, email: String, password: String) {
            _isLoading.value = true
            val client = ApiConfig.getApiService(context).postLogin(email, password)
            client.enqueue(object: Callback<UserResponse> {
                  override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                  ) {
                        _isLoading.value = false
                        if(response.isSuccessful) {
                              val responseBody = response.body()
                              if(responseBody != null) {
                                    if(responseBody.error == false){
                                          _token.postValue(responseBody.loginResult.token.toString())
                                          _response.postValue(responseBody.message.toString())
                                    } else if(responseBody.error == true){
                                          _response.postValue(responseBody.message.toString())
                                    }
                              } else {
                                    _isLoading.value = false
                                    Toast.makeText(context, "Data Not Connected", Toast.LENGTH_SHORT).show()
                              }
                        } else {
                              Toast.makeText(context, "Failed To Login", Toast.LENGTH_SHORT).show()
                        }
                  }

                  override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.d(LoginActivity.TAG, "${t.message}")
                  }
            })
      }

      fun signIn(context: Context, name: String, email: String, password: String){
            _isLoading.value = true
            val client = ApiConfig.getApiService(context).postRegister(name, email, password)
            client.enqueue(object : Callback<SignIn> {
                  override fun onResponse(call: Call<SignIn>, response: Response<SignIn>) {
                        if(response.isSuccessful) {
                              val responseBody = response.body()
                              _isLoading.value = false
                              if(responseBody != null) {
                                    if(responseBody.error == false) {
                                          _response.value = responseBody.message.toString()
                                    } else if(responseBody.error == true) {
                                          _response.value = responseBody.message.toString()
                                    }
                              }
                        } else {
                              _isLoading.value = false
                              Toast.makeText(context, "Failed To Sign In", Toast.LENGTH_SHORT).show()
                        }
                  }

                  override fun onFailure(call: Call<SignIn>, t: Throwable) {
                        _isLoading.value = false
                        Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                  }
            })
      }
}