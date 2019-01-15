package com.example.schooluser.messenger

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class Client {
    //базовая(неизменная ссылка)
    private val baseUrl = "https://api.vk.com"
    //okHttpClient
    private val okHttpClient by lazy { OkHttpClient.Builder().addInterceptor(logging).build() }
    // закоментить
    private val logging by lazy {
        HttpLoggingInterceptor(
            HttpLoggingInterceptor.Logger {
                Timber.tag("OkHttp").d(it)
            }
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    // закоментить
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            //конвертирует json
            .addConverterFactory(GsonConverterFactory.create())
            //
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
    // закоментить
    val vkApi by lazy {
        retrofit.create(VkApi::class.java)
    }

}