package com.phinespec.restaurantscompose.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phinespec.restaurantscompose.data.remote.RestaurantsApiService
import com.phinespec.restaurantscompose.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "RestaurantViewModel"

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val restaurantsApiService: RestaurantsApiService
) : ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    private val _state = mutableStateOf(emptyList<Restaurant>())
    val state: State<List<Restaurant>> = _state

    init {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            getRestaurants()
        }
    }

    // get all the restaurants from the remote server
    // set the state value to the result restored from stateHandle
    private suspend fun getRestaurants() {
        val result = restaurantsApiService.getRestaurants()
        withContext(Dispatchers.Main) {
            _state.value = result.restoreFavorites()
        }
    }

    // take in a restaurant id
    // grab the state restaurants list as a mutable list
    // get the first restaurant with passed id
    // update the item in the state with toggled isFavorite value
    // save to stateHandle
    // replace state with mutable list
    fun toggleFavorite(restaurantId: Int) {
        val restaurants = _state.value.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == restaurantId }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] = item.copy(isFavorite = !item.isFavorite)
        saveFavorite(restaurants[itemIndex])
        _state.value = restaurants
    }

    // take in a restaurant
    // grab the list of favorited restaurant id's from stateHandle
    // if it's empty return mutable list
    // if passed item is favorited add it's id to the list
    // otherwise remove that item id from the list
    // update the stateHandle with the modified list of id's
    private fun saveFavorite(item: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }


    // Extension to restore the favorited list in the event of process death
    // get the list of id's for favorited restaurants
    // create a map which maps the favorited id to the restaurant with that id
    // iterate over each id in favoritedIds and set the mapped isFavorite value to true
    // return the values of the map as our new restaurant list
    private fun List<Restaurant>.restoreFavorites(): List<Restaurant> {
        stateHandle.get<List<Int>>(FAVORITES)?.let { favoritedIds ->
            val restaurantsMap = this.associateBy { it.id }
            favoritedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }

    companion object {
        const val FAVORITES = "favorites"
    }
}