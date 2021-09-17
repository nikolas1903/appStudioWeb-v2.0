package com.example.studioweb.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studioweb.listener.APIListener
import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.user.UserRepositoryAPI
import com.example.studioweb.services.repository.remote.user.generics.BaseResponse

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val mUserRepositoryAPI = UserRepositoryAPI()
    val mCreate = MutableLiveData<Boolean>()
    var create: LiveData<Boolean> = mCreate

    /**
     * Função responsável por Registrar um usuário e mandar para o Servidor.
     */
    fun create(
        nome: String, cpf: String, email: String, telefone: String, nascimento: String, senha: String) {
        mUserRepositoryAPI.create(nome, cpf, email, telefone, nascimento, senha, object : APIListener {
                override fun onSuccess(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
                    if (modelAPI.sucesso) {
                        setRegisterTrue(modelAPI)
                    } else {
                        setRegisterFalse(modelAPI)
                    }
                }

                override fun onFailure(str: String) {
                    mCreate.value = false
                }
            })
    }

    /**
     * Seta o Create como True, e manda a mensagem enviada pela API para o usuário.
     */
    private fun setRegisterTrue(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
        mCreate.value = true
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
    }

    /**
     * Seta o Create como False, e manda a mensagem enviada pela API para o usuário.
     */
    private fun setRegisterFalse(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
        mCreate.value = false
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
    }

}