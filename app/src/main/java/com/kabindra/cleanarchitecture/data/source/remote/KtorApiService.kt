package com.kabindra.cleanarchitecture.data.source.remote

import com.kabindra.cleanarchitecture.data.model.NewsDto
import com.kabindra.cleanarchitecture.data.source.remote.ApiList.Companion.API_TOP_HEADLINES
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorApiService(private val client: HttpClient) {
    suspend fun fetchNews(): NewsDto {
        return client.get(API_TOP_HEADLINES).body<NewsDto>()
    }
}
