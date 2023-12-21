package com.example.myapplication.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.myapplication.model.Cliente
import kotlin.random.Random

@Composable
fun CadastroCharutoScreen(navController: NavHostController?, isPreview:Boolean = false) {
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    //var codigo by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var fabricante by remember { mutableStateOf("") }
    var qtdeNicotina by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    //var cpfCliente by remember { mutableStateOf("") }

    // Geração de código incremental aleatório
    val codigo= remember { mutableStateOf(0) }
    codigo.value = Random.nextInt(1000, 9999) // Gera um número aleatório entre 1000 e 9999

    // Recuperação do CPF do cliente
    var selectedCliente by remember { mutableStateOf<Cliente?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro de Caixa de Charutos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        //OutlinedTextField(value = codigo.value.toString(), onValueChange = { codigo.value = it.toInt() }, label = { Text("Código") })
        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
        OutlinedTextField(value = fabricante, onValueChange = { fabricante = it }, label = { Text("Fabricante") })
        OutlinedTextField(value = qtdeNicotina, onValueChange = { qtdeNicotina = it }, label = { Text("Qtde Nicotina") })
        OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })
           Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = selectedCliente?.nome ?: "",
            onValueChange = {},
            label = { Text("Cliente") },
            readOnly = true,
            trailingIcon = {
                DropdownMenu(
                    expanded = selectedCliente != null,
                    onDismissRequest = { selectedCliente = null }
                ) {
                    db?.collection("clientes")
                        ?.get()
                        ?.addOnSuccessListener { documents ->
                            documents.mapNotNull { it.toObject(Cliente::class.java) }.forEach { cliente ->
                                DropdownMenuItem(onClick = { selectedCliente = cliente }) {
                                    (cliente.nome)
                                }
                            }
                        }
                        ?.addOnFailureListener { exception ->
                            // Tratamento de erro
                        }
                }
            }
        )

        Button(
            onClick = {

                val charuto = CaixaDeCharutos(
                    codigo.value,
                    modelo,
                    fabricante,
                    qtdeNicotina.toFloat(),
                    preco.toDouble(),
                    selectedCliente?.cpf ?: ""
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

fun DropdownMenuItem(onClick: () -> Unit, interactionSource: () -> Unit) {

}

@Preview(showBackground = true)
@Composable
fun CadastroCharutoScreenPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        CadastroCharutoScreen(navController, isPreview = true)
    }
}
