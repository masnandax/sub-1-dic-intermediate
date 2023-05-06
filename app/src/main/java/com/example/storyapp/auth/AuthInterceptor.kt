package com.example.storyapp.auth

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val tokenPref = UserPreferences(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        tokenPref.getInput(UserPreferences.TOKEN)?.let {
            request
                .addHeader("Authorization", "Bearer $it")
                .build()
        }
        return chain.proceed(request.build())
    }
}