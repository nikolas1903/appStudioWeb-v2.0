package com.example.studioweb.viewmodel

import androidx.lifecycle.ViewModel
import com.example.studioweb.listener.APIListenerOrcamentoGet
import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.remote.orcamento.OrcamentoRepositoryAPI

class MeusOrcamentosViewModel : ViewModel() {
    private val mOrcamentoRepositoryAPI = OrcamentoRepositoryAPI()

    fun buscar(cpf: String) {
        mOrcamentoRepositoryAPI.getByCpf(cpf, object : APIListenerOrcamentoGet{
            override fun onSuccess(modelAPI: List<OrcamentoModelAPI.OrcamentoResponse>) {
                var s = ""
            }

            override fun onFailure(str: String) {
                var s = ""
            }

        })
    }
}