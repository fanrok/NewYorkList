package com.example.newyorklist.domain.repositories.di

import com.example.newyorklist.domain.repositories.ReviewRepository
import com.example.newyorklist.domain.repositories.ReviewRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository
}