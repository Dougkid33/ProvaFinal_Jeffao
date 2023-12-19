package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Navigation.AppDestination
import com.example.myapplication.model.CaixaDeCharutos
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ListaCharutosScreen(navController: NavHostController?) {
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
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(charutos) { charuto ->
                CharutoCard(charuto, onEdit = { charutoEditado ->
                    // Implemente a navegação para a tela de edição
                    navController?.navigate("cadastroCaixadeCharutoScreen/${charuto.codigo}")
                }, onDelete = {
                    db.collection("charutos").document(charuto.codigo.toString()).delete()
                    // Recarregar a lista após a exclusão
                    db.collection("charutos").get().addOnSuccessListener { querySnapshot ->
                        charutos = querySnapshot.documents.map { document ->
                            CaixaDeCharutos(
                                codigo = document.hashCode() ?: 0,
                                modelo = document.getString("modelo") ?: "",
                                fabricante = document.getString("fabricante") ?: "",
                                qtdeNicotina = document.getDouble("qtdeNicotina")?.toFloat() ?: 0f,
                                preco = document.getDouble("preco") ?: 0.0,
                                cpfCliente = document.getString("cpfCliente") ?: ""
                            )
                        }
                        isLoading = false
                    }
                })
            }
        }
    }
}

@Composable
fun CharutoCard(charuto: CaixaDeCharutos, onEdit: (CaixaDeCharutos) -> Unit, onDelete: (Int) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val navController = rememberNavController()
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Código: ${charuto.codigo}", style = MaterialTheme.typography.titleSmall)
            Text("Modelo: ${charuto.modelo}")
            Text("Fabricante: ${charuto.fabricante}")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { onEdit(charuto) }, modifier = Modifier.padding(8.dp)) {
                    Text("Editar")
                }
                Button(onClick = { onDelete(charuto.codigo) }, modifier = Modifier.padding(8.dp)) {
                    Text("Excluir")
                }
                Button(
                    onClick = { navController?.navigate(AppDestination.TelaInicial.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Retornar para a Tela Inicial")
                }
            }
        }
    }
}