package com.baccaro.kmp.presentation.auth

import Notify
import NotificationDuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baccaro.kmp.domain.model.UserRole
import com.baccaro.kmp.presentation.RegisterViewModel
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onWorkerRegisterClick: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isLoadingLocation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isLoadingLocation = true
        val geoLocation = Geolocator.mobile()
        when (val result = geoLocation.current()) {
            is GeolocatorResult.Success -> {
                val coordinates = result.data.coordinates
                // Solo necesitamos llamar a onLocationChanged ya que onCoordinatesChanged espera un string
                viewModel.onLocationChanged("(${coordinates.latitude}/${coordinates.longitude})")
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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Cliente") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.phone ?: "",
                onValueChange = viewModel::onPhoneChanged,
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // Modificar el campo de ubicación
            OutlinedTextField(
                value = state.location,
                onValueChange = { viewModel.onLocationChanged(it) },
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

            Button(
                onClick = {
                    viewModel.onRegisterClick { 
                        Notify(message = "¡Registro exitoso!", duration = NotificationDuration.SHORT)
                        onRegisterSuccess()
                    } 
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }

            TextButton(onClick = onWorkerRegisterClick) {
                Text("¿Eres un trabajador? Regístrate aquí")
            }

            state.error?.let { error ->
                LaunchedEffect(error) {
                    Notify(message = "Ha ocurrido un error", duration = NotificationDuration.LONG)
                }
                Text(
                    text = "Ha ocurrido un error",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
} 