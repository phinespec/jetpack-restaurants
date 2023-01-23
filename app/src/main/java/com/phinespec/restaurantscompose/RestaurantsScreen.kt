package com.phinespec.restaurantscompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phinespec.restaurantscompose.model.Restaurant
import com.phinespec.restaurantscompose.presentation.RestaurantsViewModel
import com.phinespec.restaurantscompose.ui.theme.RestaurantsComposeTheme


@Composable
fun RestaurantsScreen(
    onItemClick: (Int) -> Unit = {}
) {

    val viewModel: RestaurantsViewModel = hiltViewModel()
    val state = viewModel.state.value

    LazyColumn {
        items(state) { restaurant ->
            RestaurantItem(
                restaurant,
                onFavoriteClick = { id ->
                    viewModel.toggleFavorite(id)
                },
                onItemClick = { id ->
                    onItemClick(id)
                }
            )
        }
    }
}

@Composable
fun RestaurantItem(
    item: Restaurant,
    onFavoriteClick: (Int) -> Unit,
    onItemClick: (Int) -> Unit
) {

    val icon = if (item.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder

    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(8.dp)
            .clickable { onItemClick(item.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)) {

            RestaurantIcon(
                Icons.Filled.Place,
                Modifier.weight(0.15f)
            ) {}
            RestaurantDetails(item.title, item.description, Modifier.weight(0.70f))
            RestaurantIcon(icon, Modifier.weight(0.15f)) { onFavoriteClick(item.id) }
        }
    }
}

@Composable
fun RestaurantIcon(
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
     Image(
         imageVector = icon,
         contentDescription = "restaurant icon",
         modifier = modifier
             .padding(8.dp)
             .clickable { onClick() }
     )
}

@Composable
fun RestaurantDetails(
    title: String,
    description: String,
    modifier: Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(text = title,
            style = MaterialTheme.typography.h6)
        CompositionLocalProvider(
            LocalContentAlpha provides
                    ContentAlpha.medium) {
            Text(text = description,
                style = MaterialTheme.typography.body2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RestaurantsComposeTheme {
        RestaurantsScreen()
    }
}