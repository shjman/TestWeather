package com.shjman.testweather.ui.weather_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImage
import com.shjman.testweather.data.CoordinateLatLon
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class WeatherDetailsScreen(
    val coordinate: CoordinateLatLon,
    val date: String,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: WeatherDetailsViewModel = koinViewModel { parametersOf(coordinate, date) }
        val screenState by viewModel.screenState.collectAsState()
        WeatherDetailsView(
            screenState = screenState,
        )
    }
}

@Composable
private fun WeatherDetailsView(
    screenState: ScreenState,
    modifier: Modifier = Modifier,
) {
    when (screenState) {
        ScreenState.Loading -> {
            Box(
                modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(36.dp)
                        .width(36.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        is ScreenState.Error -> {
            Text(" Load details failed error == ${screenState.error}")
        }
        is ScreenState.Success -> {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = screenState.detailedDayForecastPresentModel.toString(),
                    modifier = Modifier.fillMaxWidth(),
                )
                AsyncImage(
                    model = screenState.detailedDayForecastPresentModel.iconUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentAny() {
//    ForecastsPerDayView(
//        screenState = CurrentState(
//            state = State.SUCCESS,
//            forecastsPerDay = listOf(
//                ForecastPerDay(
//                    date = "2024-02-16",
//                    temp = 10,
//                    icon = "10d",
//                ),
//                ForecastPerDay(
//                    date = "2024-02-20",
//                    temp = 15,
//                    icon = "02d",
//                ),
//            ),
//        ),
//        onSaveLocationClick = {},
//
//        )
}

