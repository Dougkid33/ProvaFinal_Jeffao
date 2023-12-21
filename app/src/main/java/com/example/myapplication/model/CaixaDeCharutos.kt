package com.example.myapplication.model

open class CaixaDeCharutos(
    var codigo: Int,
    val modelo: String,
    val fabricante: String,
    private val qtdeNicotina: Float,
    private val preco: Double,
    val cpfCliente: String
) {
    override fun toString(): String {
        return "CaixaDeCharutos(codigo=$codigo, modelo='$modelo', fabricante='$fabricante', qtdeNicotina=$qtdeNicotina, preco=$preco)"
    }
}