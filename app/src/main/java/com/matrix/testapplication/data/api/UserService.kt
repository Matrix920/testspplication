package com.matrix.testapplication.data.api

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object UserService {

    var mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    var interceptor = Interceptor {
        val request = it.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()
        it.proceed(request)
    }

    var mOkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://testapi.alifouad91.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
//        .client(mOkHttpClient)
        .build()

    val usersApi = retrofit.create(IUsersService::class.java)
}