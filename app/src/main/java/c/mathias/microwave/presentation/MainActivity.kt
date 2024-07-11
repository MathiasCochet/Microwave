package c.mathias.microwave.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import c.mathias.microwave.R
import c.mathias.microwave.presentation.theme.MicrowaveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MicrowaveTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Body(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Body(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val event = viewModel::handleEvent

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .height(200.dp)
                .width(200.dp),
            painter = painterResource(id = if (uiState.doorOpen) R.drawable.microwave_open else R.drawable.microwave_closed),
            contentDescription = ""
        )
        Button(
            content = {
                if (uiState.doorOpen)
                    Text("Close Door")
                else
                    Text("Open Door")
            },
            onClick = {
                if (uiState.doorOpen)
                    event(MainEvent.CloseDoor)
                else
                    event(MainEvent.OpenDoor)
            },
        )
        Button(
            content = {
                Text("Start")
            },
            onClick = {
                event(MainEvent.StartMicroWave)
            }
        )
        Spacer(modifier = Modifier.height(64.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Card(
                modifier = Modifier.size(150.dp),
                colors = CardDefaults.cardColors()
                    .copy(containerColor = if (uiState.heaterOn) Color(0xFFFFA500) else Color.LightGray),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = R.drawable.heat),
                            tint = Color.Black,
                            contentDescription = "",
                        )
                        Text(
                            "Heater",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            Card(
                modifier = Modifier.size(150.dp),
                colors = CardDefaults.cardColors()
                    .copy(containerColor = if (uiState.doorOpen) Color.Yellow else Color.LightGray),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            painter = painterResource(id = R.drawable.light_bulb),
                            tint = Color.Black,
                            contentDescription = "",
                        )
                        Text("Light", modifier = Modifier.padding(8.dp))
                    }
                }
            }
        }
    }
}