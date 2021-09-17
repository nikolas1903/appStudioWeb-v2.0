package com.example.studioweb.services.repository.remote.orcamento.request

import com.google.gson.annotations.SerializedName

class Orcamento {

    /**
     * Campos passados para a API para fazer o Login.
     */
    class EnviarOrcamento() {
        @SerializedName("nome")
        var nome: String = ""

        @SerializedName("cpf")
        var cpf: String = ""

        @SerializedName("telefone")
        var telefone: String = ""

        @SerializedName("email")
        var email: String = ""

        @SerializedName("ramo")
        var ramo: String = ""

        @SerializedName("empresa")
        var empresa: String = ""

        @SerializedName("templates")
        var templates: String = ""

        @SerializedName("status")
        var status: String = ""
    }
}