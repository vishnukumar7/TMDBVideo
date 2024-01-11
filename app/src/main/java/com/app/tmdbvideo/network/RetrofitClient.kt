package com.app.tmdbvideo.network

import com.app.tmdbvideo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient())
        .build()

    private fun getLogging(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return logging
    }

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.apply {
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
            connectTimeout(1, TimeUnit.MINUTES)
            addInterceptor(getLogging())
            addInterceptor { chain ->
                try {
                    val newRequest: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0NDMyNDIwZWEwYWUwNzA3N2E2YTZkOWM4MzhkY2MyNCIsInN1YiI6IjY1OTY5MmI5YTZjMTA0MTBkZGZhNmM0YiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.lcsBXtYukRyFo78sBi9m0ZB1WTbGRCQgfAEOI65S4tk")
                        .build()
                    chain.proceed(newRequest)
                }catch (e:Exception){
                    throw e
                }
            }
        }
        return client.build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

}