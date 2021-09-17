package com.example.studioweb.services.repository.remote.orcamento

import com.example.studioweb.listener.APIListenerOrcamento
import com.example.studioweb.listener.APIListenerOrcamentoGet
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.RetrofitClientOrcamento
import com.example.studioweb.services.repository.remote.orcamento.request.Orcamento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val mRemote = RetrofitClientOrcamento.createService(OrcamentoService::class.java)

class OrcamentoRepositoryAPI {

    fun send(nome: String, cpf: String, telefone: String, email: String, ramo: String, nomeEmpresa: String, templates: String, listener: APIListenerOrcamento){
        val orcamento = Orcamento.EnviarOrcamento()
        orcamento.nome = nome
        orcamento.cpf = cpf
        orcamento.telefone = telefone
        orcamento.email = email
        orcamento.ramo = ramo
        orcamento.empresa = nomeEmpresa
        orcamento.status = "Recebido"
        orcamento.templates = templates

        val call: Call<OrcamentoModelAPI.OrcamentoResponse> = mRemote.registraOrcamento(orcamento)
        call.enqueue(object : Callback<OrcamentoModelAPI.OrcamentoResponse>{
            override fun onResponse(
                call: Call<OrcamentoModelAPI.OrcamentoResponse>, response: Response<OrcamentoModelAPI.OrcamentoResponse>) {
                response.body()?.let { listener.onSuccess(it) }
            }

            override fun onFailure(
                call: Call<OrcamentoModelAPI.OrcamentoResponse>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }
        })
    }

    fun getByCpf(cpf: String, listener: APIListenerOrcamentoGet) {
        val call: Call<List<OrcamentoModelAPI.OrcamentoResponse>> = mRemote.getOrcamentoCpf(cpf)
        call.enqueue(object : Callback<List<OrcamentoModelAPI.OrcamentoResponse>> {
            override fun onResponse(
                call: Call<List<OrcamentoModelAPI.OrcamentoResponse>>, response: Response<List<OrcamentoModelAPI.OrcamentoResponse>>) {
                response.body()?.let { listener.onSuccess(it) }
            }

            override fun onFailure(call: Call<List<OrcamentoModelAPI.OrcamentoResponse>>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }
        })
    }

}