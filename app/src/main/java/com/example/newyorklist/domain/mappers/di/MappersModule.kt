package com.example.newyorklist.domain.mappers.di

import com.example.newyorklist.domain.mappers.SimpleMapper
import com.example.newyorklist.domain.mappers.SimpleMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MappersModule {
    @Binds
    abstract fun bindSimpleMapper(impl: SimpleMapperImpl): SimpleMapper
}