package com.example.studioweb.services.repository.remote.orcamento

import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.orcamento.request.Orcamento
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrcamentoService {
    /**
     * Função que faz a comunicação com a API para realizar o Login.
     */
    @POST("orcamento")
    fun registraOrcamento(
        @Body orcamento: Orcamento.EnviarOrcamento
    ): Call<OrcamentoModelAPI.OrcamentoResponse>


    /**
     * Função que faz a comunicação com a API para procurar um orçamento pelo CPF.
     */
    @GET("orcamento/cpf/{cpf}")
    fun getOrcamentoCpf(
        @Path(value = "cpf", encoded = true) cpf: String
    ): Call <List<OrcamentoModelAPI.OrcamentoResponse>>
}