package com.pys.booksearchapp.data.api

import com.pys.booksearchapp.util.Constants.BASE_URL
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val okHttpClient : OkHttpClient by lazy {
        //서버와 api 사이에서 데이터를 intercept
        val httpLoggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    val api : BookSearchApi by lazy {
        retrofit.create(BookSearchApi::class.java)
    }
}