package com.kabindra.cleanarchitecture.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabindra.cleanarchitecture.domain.entity.News
import com.kabindra.cleanarchitecture.domain.usecase.remote.GetNewsUseCase
import com.kabindra.cleanarchitecture.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val getNewsUseCase: GetNewsUseCase) : ViewModel() {
    private val _newsState = MutableStateFlow<NetworkResult<News>>(NetworkResult.Initial)
    val newsState: StateFlow<NetworkResult<News>> get() = _newsState

    fun loadNews() {
        _newsState.value = NetworkResult.Loading
        viewModelScope.launch {
            getNewsUseCase.execute().collect { result ->
                _newsState.value = result
            }
        }
    }
}