package com.example.studioweb.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studioweb.listener.APIListenerOrcamento
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.orcamento.OrcamentoRepositoryAPI

@SuppressLint("StaticFieldLeak")
class OrcamentoViewModel(application: Application) : AndroidViewModel(application) {

    private val mOrcamentoRepositoryAPI = OrcamentoRepositoryAPI()
    private val mCreateOrcamento = MutableLiveData<Boolean>()
    var createOrcamento: LiveData<Boolean> = mCreateOrcamento

    fun enviarOrcamento(nome: String, cpf: String, telefone: String, email: String, ramo: String, nomeEmpresa: String, templates: String) {
        mOrcamentoRepositoryAPI.
        send(nome, cpf, telefone, email, ramo, nomeEmpresa, templates, object : APIListenerOrcamento {
            override fun onSuccess(modelAPI: OrcamentoModelAPI.OrcamentoResponse) {
                // Salvando resposta da API
                mCreateOrcamento.value = true
            }

            override fun onFailure(str: String) {
                mCreateOrcamento.value = false
            }


        })
    }
}