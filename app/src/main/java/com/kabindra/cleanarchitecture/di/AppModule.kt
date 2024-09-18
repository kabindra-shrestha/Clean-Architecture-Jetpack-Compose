package com.kabindra.cleanarchitecture.di

import androidx.compose.material3.SnackbarHostState
import com.kabindra.cleanarchitecture.BuildConfig
import com.kabindra.cleanarchitecture.R
import com.kabindra.cleanarchitecture.data.repository.remote.NewsRepositoryImpl
import com.kabindra.cleanarchitecture.data.source.remote.KtorApiService
import com.kabindra.cleanarchitecture.data.source.remote.NewsDataSource
import com.kabindra.cleanarchitecture.data.source.remote.RetrofitApiService
import com.kabindra.cleanarchitecture.domain.repository.remote.NewsRepository
import com.kabindra.cleanarchitecture.domain.usecase.remote.GetNewsUseCase
import com.kabindra.cleanarchitecture.presentation.viewmodel.NewsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val provideAppModule = module {
    single { SnackbarHostState() }
}

val provideHttpClientModule = module {
    single {
        HttpClient {
            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    },
                    contentType = ContentType.Application.Json
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            defaultRequest {
                url(androidContext().getString(R.string.BASE_URL))
                contentType(ContentType.Application.Json)
            }
        }
    }

//    val connectTimeout: Long = 60 // 20s
//    val readTimeout: Long = 60 // 20s
//    val writeTimeout: Long = 180 // 20s

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
//            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
//            .readTimeout(readTimeout, TimeUnit.SECONDS)
//            .writeTimeout(writeTimeout, TimeUnit.SECONDS)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return okHttpClientBuilder.addInterceptor(httpLoggingInterceptor).build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { provideHttpClient() }
    single {
        val baseUrl = androidContext().getString(R.string.BASE_URL)
        provideRetrofit(get(), baseUrl)
    }
}

val provideApiServiceModule = module {
    singleOf(::KtorApiService)

    fun provideApiService(retrofit: Retrofit): RetrofitApiService {
        return retrofit.create(RetrofitApiService::class.java)
    }
    single { provideApiService(get()) }
}

val provideDataSourceModule = module {
    singleOf(::NewsDataSource)
}

val provideRepositoryModule = module {
    singleOf(::NewsRepositoryImpl).bind<NewsRepository>()
}

val provideUseCaseModule = module {
    singleOf(::GetNewsUseCase)
}

val provideViewModelModule = module {
    viewModelOf(::NewsViewModel)
}

val appModule = listOf(
    provideAppModule,
    provideHttpClientModule,
    provideApiServiceModule,
    provideDataSourceModule,
    provideRepositoryModule,
    provideUseCaseModule,
    provideViewModelModule
)