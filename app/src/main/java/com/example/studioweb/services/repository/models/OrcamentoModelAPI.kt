package com.example.studioweb.services.repository.models

import com.google.gson.annotations.SerializedName

class OrcamentoModelAPI {
    /**
     * Modelo de resposta da API.
     */
     class OrcamentoResponse {
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

        @SerializedName("status")
        var status: String = ""

        @SerializedName("templates")
        var templates: String = ""

        @SerializedName("_id")
        var id: String = ""

        @SerializedName("__v")
        var v: Int = 0
    }

}

