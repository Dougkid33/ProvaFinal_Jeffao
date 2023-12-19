package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Navigation.AppDestination
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "telaInicial") {
        composable("telaInicial") { TelaInicialScreen(navController) }
        composable("cadastroCliente") { CadastroClienteScreen(navController) }
        composable("listaClientes") { ListaClientesScreen(navController) }
        composable("cadastroCaixaCharuto") { CadastroCharutoScreen(navController) }
        composable("listaCaixasCharuto") { ListaCharutosScreen(navController) }
        composable(AppDestination.CadastroEdicaoCliente.route) { backStackEntry ->
            val cpf = backStackEntry.arguments?.getString("cpf") ?: ""
            CadastroEdicaoClienteScreen(navController, cpf)
        }
        // Adicione mais rotas conforme necessário
    }
}