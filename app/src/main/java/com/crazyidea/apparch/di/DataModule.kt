package com.crazyidea.apparch.di

import com.crazyidea.apparch.api.UniversitiesApi
import com.crazyidea.apparch.data.dataSource.UniversitiesRemoteDataSource
import com.crazyidea.apparch.data.repository.UniversityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {


    @Provides
    @Singleton
    fun provideUniversitiesApi(retrofit: Retrofit) = retrofit.create(UniversitiesApi::class.java)

    @Provides
    @Singleton
    fun provideUniversitiesRemoteDataSource(universitiesApi: UniversitiesApi) =
        UniversitiesRemoteDataSource(universitiesApi )

    @Provides
    @Singleton
    fun provideUniversitiesRepository(universitiesRemoteDataSource: UniversitiesRemoteDataSource) =
        UniversityRepository(universitiesRemoteDataSource)

}