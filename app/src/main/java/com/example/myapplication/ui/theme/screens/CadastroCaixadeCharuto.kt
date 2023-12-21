package com.example.myapplication.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
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
fun CadastroCharutoScreen(navController: NavHostController?, isPreview: Boolean = false) {
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null

    var modelo by remember { mutableStateOf("") }
    var fabricante by remember { mutableStateOf("") }
    var qtdeNicotina by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var cpfCliente by remember { mutableStateOf("") }

    // Geração de código incremental aleatório
    val codigo = remember { mutableStateOf(Random.nextInt(1000, 9999)) } // Gera um número aleatório entre 1000 e 9999

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Cadastro de Caixa de Charutos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Código: ${codigo.value}", style = MaterialTheme.typography.bodyMedium) // Exibe o código como uma label

        OutlinedTextField(value = modelo, onValueChange = { modelo = it }, label = { Text("Modelo") })
        OutlinedTextField(value = fabricante, onValueChange = { fabricante = it }, label = { Text("Fabricante") })
        OutlinedTextField(value = qtdeNicotina, onValueChange = { qtdeNicotina = it }, label = { Text("Qtde Nicotina") })
        OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") })
        OutlinedTextField(value = cpfCliente, onValueChange = { cpfCliente = it }, label = { Text("CPF Cliente") }) // Campo CPF como um campo editável

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val charuto = CaixaDeCharutos(
                    codigo = codigo.value,
                    modelo = modelo,
                    fabricante = fabricante,
                    qtdeNicotina = qtdeNicotina.toFloat(),
                    preco = preco.toDouble(),
                    cpfCliente = cpfCliente
                )
                db?.collection("caixasDeCharutos")?.add(charuto)
                    ?.addOnSuccessListener {
                        // Limpa os campos
                        modelo = ""
                        fabricante = ""
                        qtdeNicotina = ""
                        preco = ""
                        cpfCliente = ""
                        SnackbarHostState()
                        Toast.makeText(null, "Caixa de Charutos cadastrada com sucesso!", Toast.LENGTH_SHORT).show()       }
                    ?.addOnFailureListener {
                        // Tratamento de erro
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastrar")


        }
        Button(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retornar para a Tela Inicial")
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