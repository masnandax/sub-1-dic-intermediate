package com.example.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("/v1/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String): Call<SignIn>

    @FormUrlEncoded
    @POST("/v1/login")
    fun postLogin(
        @Field("email") email:String ,
        @Field("password") password:String):Call<UserResponse>

    @GET("/v1/stories")
    fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ) : Call<List<ListStoryItem>>

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>

    @GET("/v1/stories/{id}")
    fun getStoriesById(
        @Path("id") id: String
    ): Call<StoryDetailResponse>
}