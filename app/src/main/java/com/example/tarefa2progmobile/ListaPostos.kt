package com.example.tarefa2progmobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ListaPostos(irParaAdicionarPosto: () -> Unit) {
    val context = LocalContext.current
    var lista by remember { mutableStateOf(carregarListaPosto(context)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        lista.forEachIndexed {index, posto ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("${posto.nome}")
                Button(
                    onClick = {
                        deletarPosto(context, index)
                        lista = carregarListaPosto(context)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Deletar")
                }
            }
        }
        Button(
            onClick = irParaAdicionarPosto
        ) {
            Text("Adicionar novo posto")
        }
    }
}