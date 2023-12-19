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
import com.example.myapplication.Navigation.AppDestination
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun CadastroClienteScreen(navController: NavHostController?, cpfInicial: String? = null, isPreview: Boolean = false) {
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    var cpf by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }

    // Carregar os dados do cliente se um CPF for fornecido
    LaunchedEffect(cpfInicial) {
        if (cpfInicial != null && cpfInicial.isNotBlank()) {
            db?.collection("clientes")?.document(cpfInicial)?.get()?.addOnSuccessListener { document ->
                document?.let {
                    nome = it.getString("nome") ?: ""
                    email = it.getString("email") ?: ""
                    instagram = it.getString("instagram") ?: ""
                }
                cpf = cpfInicial
                isEditMode = true
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Text("Cadastro de Cliente", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = cpf, onValueChange = { cpf = it }, label = { Text("CPF") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = instagram, onValueChange = { instagram = it }, label = { Text("Instagram") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val cliente = hashMapOf(
                        "cpf" to cpf,
                        "nome" to nome,
                        "email" to email,
                        "instagram" to instagram
                    )
                    db?.collection("clientes")?.add(cliente)
                        ?.addOnSuccessListener { documentReference ->
                            // Tratamento de sucesso (ex: navegar para outra tela)
                        }
                        ?.addOnFailureListener { e ->
                            // Tratamento de erro
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }
    }
    Button(
        onClick = { navController?.navigate(AppDestination.TelaInicial.route) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Retornar para a Tela Inicial")
    }
    if (isEditMode) {
        Button(
            onClick = {
                val clienteAtualizado = hashMapOf(
                    "nome" to nome,
                    "email" to email,
                    "instagram" to instagram
                )
                db?.collection("clientes")?.document(cpf)?.set(clienteAtualizado)
                    ?.addOnSuccessListener {
                        // Navegar de volta
                        navController?.popBackStack()
                    }
                    ?.addOnFailureListener {
                        // Tratar erros
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Alterações")

}
    }
}
