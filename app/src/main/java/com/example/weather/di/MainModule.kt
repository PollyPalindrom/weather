package com.example.weather.di

import com.example.weather.network.NetworkDataSource
import com.example.weather.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Singleton

@Module
@InstallIn(FragmentComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideRepo(
        networkDataSource: NetworkDataSource
    ): Repository {
        return Repository(networkDataSource)
    }
}