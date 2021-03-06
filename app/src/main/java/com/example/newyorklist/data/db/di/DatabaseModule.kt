package com.example.newyorklist.data.db.di

import android.content.Context
import androidx.room.Room
import com.example.newyorklist.data.db.AppDatabase
import com.example.newyorklist.data.db.dao.ReviewDao
import com.example.newyorklist.data.db.migrations.Migration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Dmitriy Larin
 * Database module
 *
 * @constructor Create empty Database module
 */
@InstallIn(SingletonComponent::class)
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
        )
            .addMigrations(Migration.MIGRATION_1_2)
            .build()
    }
}
