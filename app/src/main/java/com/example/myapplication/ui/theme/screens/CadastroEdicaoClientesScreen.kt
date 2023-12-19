package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.Navigation.AppDestination
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CadastroEdicaoClienteScreen(navController: NavHostController?, cpf: String) {
    val db = FirebaseFirestore.getInstance()

    // Estados para os campos do formulário
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Carregar os dados do cliente
    LaunchedEffect(cpf) {
        if (cpf.isNotBlank()) {
            db.collection("clientes").document(cpf).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    nome = document.getString("nome") ?: ""
                    email = document.getString("email") ?: ""
                    instagram = document.getString("instagram") ?: ""
                    isLoading = false
                }
            }
        }
    }

    if (isLoading) {
        Text("Carregando...", style = MaterialTheme.typography.titleLarge)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(value = instagram, onValueChange = { instagram = it }, label = { Text("Instagram") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val clienteAtualizado = hashMapOf(
                        "nome" to nome,
                        "email" to email,
                        "instagram" to instagram
                    )
                    db.collection("clientes").document(cpf).set(clienteAtualizado)
                        .addOnSuccessListener {
                            // Navegar de volta
                            navController?.popBackStack()
                        }
                        .addOnFailureListener {
                            // Tratar erros
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
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