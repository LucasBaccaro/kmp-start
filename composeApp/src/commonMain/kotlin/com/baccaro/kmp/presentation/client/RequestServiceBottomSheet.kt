import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestServiceBottomSheet(
    worker: Worker,
    onDismiss: () -> Unit,
    onRequest: (ServiceRequest) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    // Agregar estados
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var isLoadingLocation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoadingLocation = true
        val geoLocation = Geolocator.mobile()
        when (val result = geoLocation.current()) {
            is GeolocatorResult.Success -> {
                val coordinates = result.data.coordinates
                latitude = coordinates.latitude
                longitude = coordinates.longitude

                // Formatear la ubicación como (LAT - LONG)
                location = "(${coordinates.latitude}/${coordinates.longitude})"
            }
            is GeolocatorResult.Error -> {
                Notify(
                    message = "Error al obtener la ubicación: ${result.message}",
                    duration = NotificationDuration.LONG
                )
            }
        }
        isLoadingLocation = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Solicitar Servicio",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Trabajador: ${worker.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción del servicio") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Modificar el campo de ubicación
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                leadingIcon = {
                    if (isLoadingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(Icons.Default.LocationOn, contentDescription = "Ubicación")
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    onRequest(
                        ServiceRequest(
                            workerId = worker.id,
                            description = description,
                            location = location
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = description.isNotBlank() && location.isNotBlank()
            ) {
                Text("Solicitar Servicio")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
} 