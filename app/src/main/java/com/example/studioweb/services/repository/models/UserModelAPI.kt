package com.example.studioweb.services.repository.models

import com.google.gson.annotations.SerializedName

class UserModelAPI {
    /**
     * Modelo de resposta da API.
     */
    class LoginResponse {
        @SerializedName("CPF")
        var cpf: String = ""

        @SerializedName("Nascimento")
        var nascimento: String = ""

        @SerializedName("Nome")
        var nome: String = ""

        @SerializedName("Email")
        var email: String = ""

        @SerializedName("Telefone")
        var telefone: String = ""

        @SerializedName("Senha")
        var senha: String = ""
    }
}

