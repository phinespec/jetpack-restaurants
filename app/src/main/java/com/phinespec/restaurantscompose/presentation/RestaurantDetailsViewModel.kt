package com.phinespec.restaurantscompose.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phinespec.restaurantscompose.data.remote.RestaurantsApiService
import com.phinespec.restaurantscompose.model.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val restaurantsApiService: RestaurantsApiService
) : ViewModel() {

    private val _state = mutableStateOf<Restaurant?>(null)
    val state: State<Restaurant?> = _state

    init {
        val id = savedStateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch {
            _state.value = getRestaurantById(id)
        }
    }
    private suspend fun getRestaurantById(id: Int): Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restaurantsApiService.getRestaurantById(id)
            return@withContext responseMap.values.first()
        }
    }
}