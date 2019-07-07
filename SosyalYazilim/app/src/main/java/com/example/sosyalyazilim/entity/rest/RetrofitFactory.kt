package com.example.sosyalyazilim.entity.rest

import com.example.sosyalyazilim.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


val emrediricanService=RetrofitFactory.retrofit.create(ServiceInterface::class.java)!!

object RetrofitFactory {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BuildConfig.SOSYALYAZILIM_BASE_URL)
                .client(OkHttpProvider.instance)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }
}