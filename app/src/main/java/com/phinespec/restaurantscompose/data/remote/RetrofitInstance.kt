package com.phinespec.restaurantscompose.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// No longer being used in favor of dependency injection
object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://letseat-5ac46-default-rtdb.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val restaurantsApiService: RestaurantsApiService by lazy {
        retrofit.create(RestaurantsApiService::class.java)
    }
}