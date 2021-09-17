package com.example.studioweb.services.repository.remote.user.request

import com.google.gson.annotations.SerializedName

sealed class User {
    /**
     * Campos passados para a API para fazer o Login.
     */
    class Login() {
        @SerializedName("CPF")
        var cpf: String = ""

        @SerializedName("Senha")
        var senha: String = ""
    }


    /**
     * Campos passados para a API para registrar o usuário.
     */
    class Register() {
        @SerializedName("CPF")
        var cpf: String = ""

        @SerializedName("Email")
        var email: String = ""

        @SerializedName("Nascimento")
        var nascimento: String = ""

        @SerializedName("Nome")
        var nome: String = ""

        @SerializedName("Senha")
        var senha: String = ""

        @SerializedName("Telefone")
        var telefone: String = ""
    }


    /**
     * Campos passados para a API para editar um usuário.
     */
    class Update() {
        @SerializedName("CPF")
        var cpf: String = ""

        @SerializedName("Email")
        var email: String = ""

        @SerializedName("Nascimento")
        var nascimento: String = ""

        @SerializedName("Nome")
        var nome: String = ""

        @SerializedName("Senha")
        var senha: String = ""

        @SerializedName("Telefone")
        var telefone: String = ""
    }
}