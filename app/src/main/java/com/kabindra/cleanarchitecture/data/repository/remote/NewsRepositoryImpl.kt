package com.kabindra.cleanarchitecture.data.repository.remote

import com.kabindra.cleanarchitecture.data.model.toDomain
import com.kabindra.cleanarchitecture.data.source.remote.NewsDataSource
import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.domain.repository.remote.NewsRepository
import com.kabindra.cleanarchitecture.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(private val newsDataSource: NewsDataSource) : NewsRepository {
    override suspend fun getNews(): Flow<Result<News>> = flow {
        emit(Result.Loading)
        /*try {
            val response: HttpResponse = newsDataSource.getNews()
            if (response.status.isSuccess()) {
                val responses: NewsDto = response.body()

                emit(Result.Success(responses.toDomain()))
            } else {
                emit(Result.Error("Error: ${response.status}"))
            }
        } catch (e: Exception) {
            emit(Result.Error("Error: Exception: ${e.message}", e))
        }*/
        try {
            val newsDto = newsDataSource.getNews()
            if (newsDto.isSuccessful) {
                emit(Result.Success(newsDto.body()!!.toDomain()))
            } else {
                emit(Result.Error("Error: Response"))
            }
        } catch (e: Exception) {
            println("ERROR: $e")
            emit(Result.Error(e.message!!))
        }
    }
}
