package com.kabindra.cleanarchitecture.domain.repository.remote

import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(): Flow<NetworkResult<News>>
}
