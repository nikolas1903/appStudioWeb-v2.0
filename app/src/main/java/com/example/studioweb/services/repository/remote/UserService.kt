package com.example.studioweb.services.repository.remote

import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.generics.BaseResponse
import com.example.studioweb.services.repository.remote.request.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService {
    /**
     * Função que faz a comunicação com a API para realizar o Login.
     */
    @POST("api/Login")
    fun login(
        @Body login: User.Login
    ): Call<BaseResponse<UserModelAPI.LoginResponse>>


    /**
     * Função que faz a comunicação com a API para realizar o Cadastro.
     */
    @POST("/api/Cadastro")
    fun register(
        @Body register: User.Register

    ): Call<BaseResponse<UserModelAPI.LoginResponse>>


    /**
     * Função que faz a comunicação com a API para realizar a Alteração.
     */
    @PUT("/api/Alterar")
    fun update(
        @Body register: User.Update

    ): Call<BaseResponse<UserModelAPI.LoginResponse>>

}

