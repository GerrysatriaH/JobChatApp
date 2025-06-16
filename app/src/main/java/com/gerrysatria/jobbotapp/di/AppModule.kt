package com.gerrysatria.jobbotapp.di

import com.gerrysatria.jobbotapp.BuildConfig
import com.gerrysatria.jobbotapp.activity.auth.AuthViewModel
import com.gerrysatria.jobbotapp.activity.chat.MainViewModel
import com.gerrysatria.jobbotapp.activity.home.HomeViewModel
import com.gerrysatria.jobbotapp.activity.profile.ProfileViewModel
import com.gerrysatria.jobbotapp.data.UserSessionManager
import com.gerrysatria.jobbotapp.data.repository.AuthRepository
import com.gerrysatria.jobbotapp.data.repository.ChatRepository
import com.gerrysatria.jobbotapp.data.repository.ModelRepository
import com.gerrysatria.jobbotapp.data.service.ApiService
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }

    single { AuthRepository(get(), get()) }
    single { ChatRepository(get(), get()) }
    single { ModelRepository(get(), get()) }
    single { UserSessionManager(get()) }

    viewModel { AuthViewModel(get(),get()) }
    viewModel { MainViewModel(get(),get()) }
    viewModel { HomeViewModel(get(),get()) }
    viewModel { ProfileViewModel(get(),get()) }
}


