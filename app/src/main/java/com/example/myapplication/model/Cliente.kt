package com.example.myapplication.model

open class Cliente(
val cpf: String,
val nome: String,
val email: String,
val instagram: String
) {
    override fun toString(): String {
        return "Cliente(cpf='$cpf', nome='$nome', email='$email', instagram='$instagram')"
    }
}