package com.example.weatherapp.di

import com.example.weatherapp.data.remote.BASE_URL
import com.example.weatherapp.data.remote.RemoteDataSource
import com.example.weatherapp.data.remote.RetrofitDataSource
import com.example.weatherapp.data.remote.WeatherService
import com.example.weatherapp.domain.UseCases.SearchCityUseCase
import com.example.weatherapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofitDataSource(weatherService: WeatherService): RemoteDataSource = RetrofitDataSource(weatherService)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository = Repository(remoteDataSource)

    @Singleton
    @Provides
    fun provideSearchCityUseCase(repository: Repository) = SearchCityUseCase(repository)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .addConverterFactory(gsonConverterFactory)
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit): WeatherService = retrofit.create(WeatherService::class.java)
}