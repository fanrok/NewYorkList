package com.example.newyorklist.data.db.di

import android.content.Context
import androidx.room.Room
import com.example.newyorklist.data.db.AppDatabase
import com.example.newyorklist.data.db.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideReviewDao(appDatabase: AppDatabase): ReviewDao {
        return appDatabase.reviewDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "NewYorkBD"
        ).build()
    }
}