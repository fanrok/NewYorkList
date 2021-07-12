package com.example.newyorklist.domain.di

import android.provider.SyncStateContract
import com.example.newyorklist.BuildConfig
import com.example.newyorklist.data.api.ApiHelper
import com.example.newyorklist.data.api.ApiHelperImpl
import com.example.newyorklist.data.api.ApiService
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.data.db.di.DatabaseModule_ProvideReviewDaoFactory.provideReviewDao
import com.example.newyorklist.domain.ReviewRepository
import com.example.newyorklist.domain.ReviewRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
//@InstallIn(ActivityComponent::class)
//object RepoModule {
//
//    @Provides
//    fun provideReviewRepository(apiHelper: ApiHelper, reviewDao: ReviewDao): ReviewRepository {
//        return ReviewRepositoryImpl(apiHelper = apiHelper, reviewDao = reviewDao)
//    }
//
//}
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository
}