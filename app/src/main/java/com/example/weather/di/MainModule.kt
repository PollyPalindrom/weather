package com.example.weather.di

import com.example.weather.ConnectionManager
import com.example.weather.database.DatabaseDataSource
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
        networkDataSource: NetworkDataSource,
        databaseDataSource: DatabaseDataSource,
        connectionManager: ConnectionManager
    ): Repository {
        return Repository(networkDataSource, databaseDataSource, connectionManager)
    }
}