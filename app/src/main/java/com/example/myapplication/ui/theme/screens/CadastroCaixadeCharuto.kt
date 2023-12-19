package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.model.CaixaDeCharutos
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CadastroCharutoScreen(navController: NavHostController?, isPreview:Boolean = false) {
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    var codigo by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var fabricante by remember { mutableStateOf("") }
    var qtdeNicotina by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var cpfCliente by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro de Caixa de Charutos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = codigo, onValueChange = { codigo = it }, label = { Text("Código") })
        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
        OutlinedTextField(value = fabricante, onValueChange = { fabricante = it }, label = { Text("Fabricante") })
        OutlinedTextField(value = qtdeNicotina, onValueChange = { qtdeNicotina = it }, label = { Text("Qtde Nicotina") })
        OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })
        OutlinedTextField(value = cpfCliente, onValueChange = { cpfCliente = it }, label = { Text("CPF") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                val charuto = CaixaDeCharutos(
                    codigo.toInt(),
                    modelo,
                    fabricante,
                    qtdeNicotina.toFloat(),
                    preco.toDouble(),
                    cpfCliente
                )
                db?.collection("caixasDeCharutos")?.add(charuto)
                    ?.addOnSuccessListener {
                        // Tratamento de sucesso
                    }
                    ?.addOnFailureListener {
                        // Tratamento de erro
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CadastroCharutoScreenPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        CadastroCharutoScreen(navController, isPreview = true)
    }
}