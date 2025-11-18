package com.example.tarefa2progmobile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tarefa2progmobile.ui.theme.Tarefa2ProgMobileTheme
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Tarefa2ProgMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlcoolOuGasolinaApp()
                }
            }
        }
    }
}


@Composable
fun AlcoolOuGasolinaApp() {

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var precoAlcool by rememberSaveable { mutableStateOf("") }
    var precoGasolina by rememberSaveable { mutableStateOf("") }
    var posto by rememberSaveable { mutableStateOf("") }
    var usarSetentaECinco by rememberSaveable { mutableStateOf(false) }
    var resultado by remember { mutableStateOf("") }

    var userLatitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var userLongitude by rememberSaveable { mutableStateOf<Double?>(null) }

    val percentual = if (usarSetentaECinco) 0.75 else 0.70

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = precoAlcool,
            onValueChange = { precoAlcool = it },
            label = { Text(stringResource(R.string.preco_alcool)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precoGasolina,
            onValueChange = { precoGasolina = it },
            label = { Text(stringResource(R.string.preco_gasolina)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = posto,
            onValueChange = { posto = it },
            label = { Text(stringResource(R.string.nome_posto)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (usarSetentaECinco)
                    stringResource(R.string.porcentagem_75)
                else
                    stringResource(R.string.porcentagem_70)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = usarSetentaECinco,
                onCheckedChange = { usarSetentaECinco = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.obter_localizacao))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val alcool = precoAlcool.toDoubleOrNull()
                val gasolina = precoGasolina.toDoubleOrNull()

                if (alcool != null && gasolina != null && gasolina > 0) {
                    val limite = gasolina * percentual
                    resultado = if (alcool <= limite) {
                        context.getString(R.string.resultado_alcool)
                    } else {
                        context.getString(R.string.resultado_gasolina)
                    }
                } else {
                    resultado = context.getString(R.string.erro_preenchimento)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.calcular))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = resultado,
            style = MaterialTheme.typography.titleMedium
        )

        if (resultado.isNotEmpty() && posto.isNotEmpty() && userLatitude != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val gmmIntentUri =
                        Uri.parse("geo:${userLatitude},${userLongitude}?q=$posto")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                }
            ) {
                Text(stringResource(R.string.abrir_mapa))
            }
        }
    }
}


@Preview(showBackground = true, locale = "en")
@Composable
fun PreviewEN() {
    Tarefa2ProgMobileTheme {
        AlcoolOuGasolinaApp()
    }
}