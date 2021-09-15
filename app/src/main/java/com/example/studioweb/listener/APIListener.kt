package com.example.studioweb.listener

import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.generics.BaseResponse

interface APIListener {
    fun onSuccess(modelAPI: BaseResponse<UserModelAPI.LoginResponse>)
    fun onFailure(str: String)
}