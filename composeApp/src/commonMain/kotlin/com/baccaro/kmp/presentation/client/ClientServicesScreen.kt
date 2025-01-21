import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baccaro.kmp.domain.model.Service
import org.koin.compose.viewmodel.koinViewModel
import Notify
import NotificationDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientServicesScreen(
    viewModel: ClientServicesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedService by remember { mutableStateOf<Service?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Servicios") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null -> {
                    LaunchedEffect(state.error) {
                        Notify(message = "Ha ocurrido un error", duration = NotificationDuration.LONG)
                    }
                    Text(
                        text = "Ha ocurrido un error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                state.services.isEmpty() -> {
                    Text(
                        text = "No tienes servicios activos",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.services) { service ->
                            ServiceCard(
                                service = service,
                                onClick = { selectedService = service }
                            )
                        }
                    }
                }
            }
        }
    }

    selectedService?.let { service ->
        ServiceDetailsBottomSheet(
            service = service,
            isWorker = false,
            onDismiss = { selectedService = null },
            onRate = { serviceId, rating ->
                viewModel.rateService(serviceId, rating)
                Notify(message = "¡Gracias por tu calificación!", duration = NotificationDuration.SHORT)
                selectedService = null
            }
        )
    }
} 