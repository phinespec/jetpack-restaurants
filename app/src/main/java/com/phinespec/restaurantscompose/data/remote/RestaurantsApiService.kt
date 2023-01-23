package com.phinespec.restaurantscompose.data.remote

import com.phinespec.restaurantscompose.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantsApiService {

    @GET("restaurants.json")
    suspend fun getRestaurants(): List<Restaurant>

    @GET("restaurants.json?orderBy=\"r_id\"")
    suspend fun getRestaurantById(
        @Query("equalTo") id: Int
    ): Map<String, Restaurant>
}