package com.shjman.testweather.ui.locations

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.shjman.testweather.data.CoordinateLatLon
import com.shjman.testweather.ui.weather_forecast.WeatherForecastScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

object LocationsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: LocationsViewModel = koinViewModel()
        val currentState by viewModel.state.collectAsState()
        val context = LocalContext.current
        val scope: CoroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            viewModel.sideEffects.onEach {
                Log.e("aaaa", "ShowError it = $it")
                when (it) {
                    is SideEffect.ShowError -> {
                        Log.e("aaaa", "ShowError it = $it")
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }.launchIn(scope)
            viewModel.onCoordinateClick.onEach {
                Log.e("aaaa", "it = $it")
                navigator.push(WeatherForecastScreen(it))
            }.launchIn(scope)
        }
        LocationsView(
            currentState = currentState,
            onLatChanged = viewModel::onLatChanged,
            onLonChanged = viewModel::onLonChanged,
            onSaveLocationClick = viewModel::onSaveLocationClick,
            onCoordinateClick = viewModel::onCoordinateClick,
        )
    }
}

@Composable
private fun LocationsView(
    currentState: CurrentState,
    onLatChanged: (String) -> Unit,
    onLonChanged: (String) -> Unit,
    onCoordinateClick: (CoordinateLatLon) -> Unit,
    onSaveLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        InputLocationView(
            lat = currentState.enteredLat,
            lon = currentState.enteredLon,
            onLatChanged = onLatChanged,
            onLonChanged = onLonChanged,
            onSaveLocationClick = onSaveLocationClick,
        )
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            currentState.locations.onEach {
                item {
                    Button(
                        onClick = { onCoordinateClick(it) })
                    {
                        Text(" $it")
                    }
                }
            }
        }
    }
}

@Composable
private fun InputLocationView(
    lat: String,
    lon: String,
    onLatChanged: (String) -> Unit,
    onLonChanged: (String) -> Unit,
    onSaveLocationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        TextField(
            value = lat,
            onValueChange = onLatChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("enter lat") },
            modifier = Modifier.weight(1f)
        )
        TextField(
            value = lon,
            onValueChange = onLonChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text("enter lon") },
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onSaveLocationClick,
            modifier = Modifier.wrapContentWidth()
        ) {
            Text(text = "save this location", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Content3() {
    LocationsView(
        modifier = Modifier,
        onLatChanged = {},
        onLonChanged = {},
        onSaveLocationClick = {},
        onCoordinateClick = {},
        currentState = CurrentState(
            "44", "22", emptyList()
        )
    )
}

