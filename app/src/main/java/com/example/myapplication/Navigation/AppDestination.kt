package com.example.myapplication.Navigation

sealed class AppDestination(val route: String) {
    object CadastroCliente : AppDestination("cadastroCliente")
    object ListaClientes : AppDestination("listaClientes")
    object CadastroEdicaoCliente : AppDestination("cadastroEdicaoCliente/{cpf}") {
        fun createRoute(cpf: String) = "cadastroEdicaoCliente/$cpf"
    }
    object CadastroCaixaCharuto : AppDestination("cadastroCaixaCharuto")
    object ListaCaixasCharuto : AppDestination("listaCaixasCharuto")
    object TelaInicial : AppDestination("telaInicial")
}