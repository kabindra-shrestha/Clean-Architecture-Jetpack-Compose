package com.kabindra.cleanarchitecture.data.source.remote

import com.kabindra.cleanarchitecture.data.model.NewsDto

class NewsDataSource(private val apiService: KtorApiService) {
    suspend fun getNews(): NewsDto {
        return apiService.fetchNews()
    }
}
