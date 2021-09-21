package com.example.studioweb.services.repository.remote.orcamento

import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.orcamento.request.Orcamento
import retrofit2.Call
import retrofit2.http.*

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


    /**
     * Função que faz a comunicação com a API para procurar um orçamento pelo ID.
     */
    @GET("orcamento/{id}")
    fun getOrcamentoId(
        @Path(value = "id", encoded = true) id: String
    ): Call <OrcamentoModelAPI.OrcamentoResponse>


    /**
     * Função que faz a comunicação com a API para procurar um orçamento pelo ID.
     */
    @PUT("orcamento/{id}")
    fun updateOrcamentoId(
        @Body orcamento: Orcamento.EnviarOrcamento,
        @Path(value = "id", encoded = true) id: String,
    ): Call <OrcamentoModelAPI.OrcamentoResponse>
}