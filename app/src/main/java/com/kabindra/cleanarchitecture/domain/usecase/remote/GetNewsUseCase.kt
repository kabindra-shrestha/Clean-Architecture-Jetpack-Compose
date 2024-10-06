package com.kabindra.cleanarchitecture.domain.usecase.remote

import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.domain.repository.remote.NewsRepository
import com.kabindra.cleanarchitecture.utils.Result
import kotlinx.coroutines.flow.Flow

class GetNewsUseCase(private val repository: NewsRepository) {
    suspend fun execute(): Flow<Result<News>> {
        return repository.getNews()
    }
}
