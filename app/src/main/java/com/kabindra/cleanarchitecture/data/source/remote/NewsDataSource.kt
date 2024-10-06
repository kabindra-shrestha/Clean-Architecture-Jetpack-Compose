package com.kabindra.cleanarchitecture.data.source.remote

import com.kabindra.cleanarchitecture.data.model.NewsDto
import retrofit2.Response

/*class NewsDataSource(private val apiService: KtorApiService) {
    suspend fun getNews(): HttpResponse {
        return apiService.fetchNews()
    }
}*/
class NewsDataSource(private val apiService: RetrofitApiService) {
    suspend fun getNews(): Response<NewsDto> {
        return apiService.fetchNews()
    }
}
