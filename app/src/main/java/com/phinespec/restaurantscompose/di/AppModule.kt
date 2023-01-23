package com.phinespec.restaurantscompose.di

import com.phinespec.restaurantscompose.data.remote.RestaurantsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRestaurantApi(): RestaurantsApiService {
        return Retrofit.Builder()
            .baseUrl("https://letseat-5ac46-default-rtdb.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestaurantsApiService::class.java)
    }

}