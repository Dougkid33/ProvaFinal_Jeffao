package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.model.CaixaDeCharutos

@Composable
fun ListaCharutosScreen() {
    val db = FirebaseFirestore.getInstance()
    var charutos by remember { mutableStateOf(listOf<CaixaDeCharutos>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.collection("charutos").get().addOnSuccessListener { querySnapshot ->
            charutos = querySnapshot.documents.map { document ->
                CaixaDeCharutos(
                    codigo = document.getString("codigo")?.toInt() ?: 0,
                    modelo = document.getString("modelo") ?: "",
                    fabricante = document.getString("fabricante") ?: "",
                    qtdeNicotina = document.getDouble("qtdeNicotina")?.toFloat() ?: 0f,
                    preco = document.getDouble("preco") ?: 0.0,
                    cpfCliente = document.getString("cpfCliente") ?: ""
                )
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Text("Carregando...", style = MaterialTheme.typography.titleSmall)
    } else {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(charutos) { charuto ->
                CharutoCard(charuto)
            }
        }
    }
}

@Composable
fun CharutoCard(charuto: CaixaDeCharutos) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Código: ${charuto.codigo}", style = MaterialTheme.typography.titleSmall)
            Text("Modelo: ${charuto.modelo}")
            Text("Fabricante: ${charuto.fabricante}")
        }
    }
}