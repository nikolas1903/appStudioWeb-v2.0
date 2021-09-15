package com.example.studioweb.services.repository.remote.generics

import com.google.gson.annotations.SerializedName

/**
 * Resposta genérica da API, onde Sucesso e Mensagem são os campos padrões, que tem em
 * todas as requisiões, e Data são os campos que mudam conforme as requisições.
 */
open class BaseResponse<Data : Any?>(
    @SerializedName("Sucesso")
    var sucesso: Boolean = false,

    @SerializedName("Mensagem")
    var mensagem: String = "",

    @SerializedName("Data")
    var data: Data? = null


)