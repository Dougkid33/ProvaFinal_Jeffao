package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Navigation.AppDestination
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun TelaInicialScreen(navController: NavHostController) {
    var textoBusca by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textoBusca,
            onValueChange = { textoBusca = it },
            label = { Text("Buscar por CPF/Código") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Implemente a lógica de busca aqui */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(AppDestination.CadastroCliente.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastro Cliente")
        }
        Button(
            onClick = { navController.navigate(AppDestination.CadastroCaixaCharuto.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastro Caixa de Charuto")
        }
        Button(
            onClick = { navController.navigate(AppDestination.ListaClientes.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lista de Clientes")
        }
        Button(
            onClick = { navController.navigate(AppDestination.ListaCaixasCharuto.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lista de Caixas de Charuto")
            
        }
        // Adicione mais botões conforme necessário...
    }
}

@Preview(showBackground = true)
@Composable
fun TelaInicialScreenPreview() {
    MyApplicationTheme{

        val navController = rememberNavController()
        //CadastroClienteScreen(navController = navController)
        TelaInicialScreen(navController = navController)
    }
}


