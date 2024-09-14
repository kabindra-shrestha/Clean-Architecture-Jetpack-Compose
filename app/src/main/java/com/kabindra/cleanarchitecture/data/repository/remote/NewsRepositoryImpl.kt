package com.kabindra.cleanarchitecture.data.repository.remote

import com.kabindra.cleanarchitecture.data.model.toDomain
import com.kabindra.cleanarchitecture.data.source.remote.NewsDataSource
import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.domain.repository.remote.NewsRepository
import com.kabindra.cleanarchitecture.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(private val newsDataSource: NewsDataSource) : NewsRepository {
    override suspend fun getNews(): Flow<NetworkResult<News>> = flow {
        try {
            val userDto = newsDataSource.getNews()
            emit(NetworkResult.Success(userDto.toDomain()))
        } catch (e: Exception) {
            println("ERROR: $e")
            emit(NetworkResult.Error(e))
        }
    }
}
