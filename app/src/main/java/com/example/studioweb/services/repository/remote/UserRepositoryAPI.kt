package com.example.studioweb.services.repository.remote

import com.example.studioweb.listener.APIListener
import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.generics.BaseResponse
import com.example.studioweb.services.repository.remote.request.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepositoryAPI {

    private val mRemote = RetrofitClient.createService(UserService::class.java)

    /**
     * Função responsável por fazer o Login e comunicar com a API.
     * Ela recebe CPF e SENHA por parâmetro, armazena no Request do Login, e faz uma comunicação
     * com a API, fazendo um mRemote.login, que é um Post no UserService, e então realizando o Login.
     */
    fun login(cpf: String, senha: String, listener: APIListener) {
        val login = User.Login()
        login.cpf = cpf
        login.senha = senha

        val call: Call<BaseResponse<UserModelAPI.LoginResponse>> = mRemote.login(login)
        call.enqueue(object : Callback<BaseResponse<UserModelAPI.LoginResponse>> {
            override fun onFailure(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, response: Response<BaseResponse<UserModelAPI.LoginResponse>>) {
                response.body()?.let { listener.onSuccess(it) }
            }
        })

    }


    /**
     * Função responsável por criar uma conta e comunicar com a API.
     * Ela recebe todos os dados necessários para a criação por parâmetro,
     * armazena no Request do Register, e faz uma comunicação com a API,
     * fazendo um mRemote.register, que é um Post no UserService,
     * e então registrando o usuário.
     */
    fun create(nome: String, cpf: String, email: String, telefone: String, nascimento: String, senha: String, listener: APIListener) {
        val register = User.Register()
        register.nome = nome
        register.email = email
        register.telefone = telefone
        register.nascimento = nascimento
        register.cpf = cpf
        register.senha = senha

        val call: Call<BaseResponse<UserModelAPI.LoginResponse>> = mRemote.register(register)
        call.enqueue(object : Callback<BaseResponse<UserModelAPI.LoginResponse>> {
            override fun onFailure(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, response: Response<BaseResponse<UserModelAPI.LoginResponse>>) {
                response.body()?.let { listener.onSuccess(it) }
            }
        })

    }


    /**
     * Função responsável por editar uma conta e comunicar com a API.
     * Ela recebe todos os dados necessários para a edição por parâmetro,
     * armazena no Request do Update, e faz uma comunicação com a API,
     * fazendo um mRemote.update, que é um Put no UserService,
     * e então editando o usuário.
     */
    fun update(nome: String, cpf: String, email: String, telefone: String, nascimento: String, senha: String, listener: APIListener){
        val update = User.Update()
        update.nome = nome
        update.email = email
        update.telefone = telefone
        update.nascimento = nascimento
        update.cpf = cpf
        update.senha = senha

        val call: Call<BaseResponse<UserModelAPI.LoginResponse>> = mRemote.update(update)
        call.enqueue(object : Callback<BaseResponse<UserModelAPI.LoginResponse>> {
            override fun onFailure(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

            override fun onResponse(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, response: Response<BaseResponse<UserModelAPI.LoginResponse>>) {
                response.body()?.let { listener.onSuccess(it) }
            }
        })
    }


    /**
     * Função responsável por editar uma conta de usuário que não está sincronizada,
     * e comunicar com a API. Ela recebe todos os dados necessários para a edição por parâmetro,
     * armazena no Request do Update, e faz uma comunicação com a API,
     * fazendo um mRemote.update, que é um Put no UserService, e então editando o usuário.
     */
    fun updateUnsyncUsers(nome: String, cpf: String, email: String, telefone: String, nascimento: String, senha: String){
        val update = User.Update()
        update.nome = nome
        update.email = email
        update.telefone = telefone
        update.nascimento = nascimento
        update.cpf = cpf
        update.senha = senha

        val call: Call<BaseResponse<UserModelAPI.LoginResponse>> = mRemote.update(update)
        call.enqueue(object : Callback<BaseResponse<UserModelAPI.LoginResponse>> {
            override fun onFailure(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, t: Throwable) {
                var s = ""
            }

            override fun onResponse(call: Call<BaseResponse<UserModelAPI.LoginResponse>>, response: Response<BaseResponse<UserModelAPI.LoginResponse>>) {
                var s = ""
            }
        })
    }
}