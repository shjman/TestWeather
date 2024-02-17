package com.shjman.testweather.ui.weather_forecast

import android.util.Log
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import com.shjman.testweather.data.Celsius
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.data.ForecastPerDay
import com.shjman.testweather.ui.weather_details.WeatherDetailsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class WeatherForecastScreen(
    val coordinate: CoordinateLatLon,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel: WeatherViewModel = koinViewModel { parametersOf(coordinate) }
        val screenState by viewModel.screenState.collectAsState()
        val scope: CoroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            viewModel.onDayClick.onEach {
                Log.e("aaaa", "it = $it")
                navigator.push(WeatherDetailsScreen(coordinate, it))
            }.launchIn(scope)
        }
        ForecastsPerDayView(
            screenState = screenState,
            onSaveLocationClick = viewModel::onDayClick,
        )
    }
}

@Composable
private fun ForecastsPerDayView(
    screenState: ScreenState,
    onSaveLocationClick: (String) -> Unit,
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
            Text(" Load forecast failed error == ${screenState.error}")
        }
        is ScreenState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                screenState.forecastsPerDay.onEach {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = it.iconUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp),
                            )
                            Button(
                                onClick = { onSaveLocationClick(it.date) })
                            {
                                Text("${it.date} temp == ${it.temp} \u00B0") // todo prepare in advance
                            }
                        }
                    }
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun ContentAny() {
    ForecastsPerDayView(
        screenState = ScreenState.Success(
            forecastsPerDay = listOf(
                ForecastPerDay(
                    date = "2024-02-16",
                    temp = Celsius(10),
                    iconUrl = "10d",
                ),
                ForecastPerDay(
                    date = "2024-02-20",
                    temp = Celsius(15),
                    iconUrl = "02d",
                ),
            ),
        ),
        onSaveLocationClick = {},
    )
}
