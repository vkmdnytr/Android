package com.example.sosyalyazilim.entity.rest

import com.example.sosyalyazilim.entity.model.data.MockListResultItem
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ServiceInterface {

    @GET(Urls.MOCK_URL)
    fun mockListData(): Deferred<Response<MockListResultItem>>
}

