package com.kabindra.cleanarchitecture.data.source.remote

import com.kabindra.cleanarchitecture.data.model.NewsDto
import com.kabindra.cleanarchitecture.data.source.remote.ApiList.Companion.API_TOP_HEADLINES
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApiService {
    @GET(API_TOP_HEADLINES)
    suspend fun fetchNews(): Response<NewsDto>
}
