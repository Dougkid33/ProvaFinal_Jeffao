package com.example.myapplication.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Navigation.AppDestination
import com.example.myapplication.model.Cliente
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import androidx.compose.runtime.Composable
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun ListaClientesScreen(navController: NavHostController?, isPreview:Boolean = false) {
    val db = if (!isPreview) FirebaseFirestore.getInstance() else null
    var clientes by remember { mutableStateOf(listOf<Cliente>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db?.collection("clientes")?.get()?.addOnSuccessListener { querySnapshot ->
            clientes = querySnapshot.documents.map { document ->
                Cliente(
                    cpf = document.getString("cpf") ?: "",
                    nome = document.getString("nome") ?: "",
                    email = document.getString("email") ?: "",
                    instagram = document.getString("instagram") ?: ""
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
            items(clientes) { cliente ->
                ClienteCard(cliente, onEdit = { clienteEditado ->
                    navController?.navigate(AppDestination.CadastroEdicaoCliente.createRoute(clienteEditado.cpf))
                }, onDelete = { cpf ->
                    val db = Firebase.firestore
                    val document = db.collection("clientes").document(cpf)

                    document.get().addOnSuccessListener { querySnapshot ->
                        document.delete().addOnSuccessListener {
                            Log.d("Firestore", "DocumentSnapshot successfully deleted!")
                            // Recarregar a lista após a exclusão
                            db.collection("clientes").get().addOnSuccessListener { querySnapshot ->
                                clientes = querySnapshot.documents.map { document ->
                                    Cliente(
                                        cpf = document.getString("cpf") ?: "",
                                        nome = document.getString("nome") ?: "",
                                        email = document.getString("email") ?: "",
                                        instagram = document.getString("instagram") ?: ""
                                    )
                                }
                                isLoading = false
                            }
                        }.addOnFailureListener { e ->
                            Log.w("Firestore", "Error deleting document", e)
                            if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.NOT_FOUND) {
                                // Show a Toast message
                                //Toast.makeText(context, "Cliente com o CPF ${cpf} não existe.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener { e ->
                        Log.w("Firestore", "Error getting document", e)
                    }
                }
                )

            }

        }
        Button(
            onClick = { navController?.navigate(AppDestination.TelaInicial.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retornar para a Tela Inicial")
        }
    }
}

@Composable
fun ClienteCard(cliente: Cliente, onEdit: (Cliente) -> Unit, onDelete: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val navController = rememberNavController()
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Informações do cliente
            Text("CPF: ${cliente.cpf}", style = MaterialTheme.typography.titleSmall)
            Text("Nome: ${cliente.nome}")
            Text("Email: ${cliente.email}")
            Text("Instagram: ${cliente.instagram}")
            Spacer(modifier = Modifier.height(8.dp))
            // Botões
            Row {
                Button(onClick = { onEdit(cliente) }, modifier = Modifier.padding(8.dp)) {
                    Text("Editar")
                }
                Button(onClick = { onDelete(cliente.cpf) }, modifier = Modifier.padding(8.dp)) {
                    Text("Excluir")
                }

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ListaClientesScreenPreview() {
    MyApplicationTheme {
        val navController = rememberNavController()
        ListaClientesScreen(navController, isPreview = true)
    }
}
