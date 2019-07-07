package com.example.sosyalyazilim.entity.rest


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpProvider private constructor() {

    private object Holder {
        val INSTANCE = getOkHttpClient()
    }
    companion object {
        val instance: OkHttpClient by lazy { Holder.INSTANCE }

        private fun getOkHttpClient(): OkHttpClient {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE

            val builder = OkHttpClient.Builder()
                    .addNetworkInterceptor(httpLoggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)

            return builder.build()
        }
    }

}