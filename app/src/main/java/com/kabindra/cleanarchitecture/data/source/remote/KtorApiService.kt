package com.kabindra.cleanarchitecture.data.source.remote

import com.kabindra.cleanarchitecture.data.source.remote.ApiList.Companion.API_TOP_HEADLINES
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class KtorApiService(private val client: HttpClient) {
    suspend fun fetchNews(): HttpResponse {
        return client.get(API_TOP_HEADLINES)
    }
}
