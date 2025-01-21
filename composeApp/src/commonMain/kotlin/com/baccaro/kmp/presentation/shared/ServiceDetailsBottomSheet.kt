import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baccaro.kmp.domain.model.Service
import com.baccaro.kmp.domain.model.ServiceStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsBottomSheet(
    service: Service,
    isWorker: Boolean,
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit = {},
    onReject: (String) -> Unit = {},
    onComplete: (String) -> Unit = {},
    onRate: (String, Int) -> Unit = { _, _ -> }
) {
    var selectedRating by remember { mutableStateOf(0) }

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
                text = "Detalles del Servicio",
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ServiceDetailItem("Descripción", service.description)
            ServiceDetailItem(
                label = if (isWorker) "Cliente" else "Trabajador",
                value = service.worker.name
            )
            ServiceDetailItem("Teléfono", service.worker.phone)
            ServiceDetailItem("Ubicación", service.location)
            ServiceDetailItem("Estado", when(service.status) {
                ServiceStatus.PENDING -> "Pendiente"
                ServiceStatus.IN_PROGRESS -> "En Progreso"
                ServiceStatus.COMPLETED -> "Completado"
                ServiceStatus.CANCELLED -> "Cancelado"
                ServiceStatus.REJECTED -> "Rechazado"
            })
            ServiceDetailItem("Fecha", service.createdAt)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Acciones según el rol y estado
            if (isWorker) {
                when (service.status) {
                    ServiceStatus.PENDING -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onReject(service.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Rechazar")
                            }
                            Button(
                                onClick = { onAccept(service.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Aceptar")
                            }
                        }
                    }
                    ServiceStatus.IN_PROGRESS -> {
                        Button(
                            onClick = { onComplete(service.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Marcar como Completado")
                        }
                    }
                    else -> { /* No mostrar botones */ }
                }
            } else {
                // Cliente solo puede calificar servicios completados
                if (service.status == ServiceStatus.COMPLETED && service.rating == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Califica el servicio",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        RatingSelector(
                            currentRating = selectedRating,
                            onRatingChange = { selectedRating = it }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { onRate(service.id, selectedRating) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = selectedRating > 0
                        ) {
                            Text("Enviar Calificación")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ServiceDetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 