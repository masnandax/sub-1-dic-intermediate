package com.example.storyapp.api

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("loginResult")
     val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)

data class SignIn(
    @field:SerializedName("error")
    var error: Boolean? =null,

    @field:SerializedName("message")
    val message: String? =null,
)
