package com.example.studioweb.listener

import com.example.studioweb.services.repository.models.OrcamentoModelAPI
import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.user.generics.BaseResponse

interface APIListener {
    fun onSuccess(modelAPI: BaseResponse<UserModelAPI.LoginResponse>)
    fun onFailure(str: String)
}

interface APIListenerOrcamento {
    fun onSuccess(modelAPI: OrcamentoModelAPI.OrcamentoResponse)
    fun onFailure(str: String)
}
interface APIListenerOrcamentoGet {
    fun onSuccess(modelAPI: List<OrcamentoModelAPI.OrcamentoResponse>)
    fun onFailure(str: String)
}

interface APIListenerOrcamentoGetId {
    fun onSuccess(modelAPI: OrcamentoModelAPI.OrcamentoResponse)
    fun onFailure(str: String)
}

