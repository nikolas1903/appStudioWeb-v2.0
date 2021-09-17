package com.example.studioweb.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.listener.APIListener
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.services.repository.local.UserRepositoryDB
import com.example.studioweb.services.repository.models.UserLoginModelDB
import com.example.studioweb.services.repository.models.UserModelAPI
import com.example.studioweb.services.repository.remote.user.generics.BaseResponse
import com.example.studioweb.services.repository.remote.user.UserRepositoryAPI

@SuppressLint("StaticFieldLeak")
class EditUserViewModel(application: Application) : AndroidViewModel(application) {

    private val mSharedPreferences = SecurityPreferences(application)
    private val mContext = application.applicationContext
    private val mUserRepositoryAPI = UserRepositoryAPI()
    private val mUserRepositoryDB = UserRepositoryDB.getInstance(mContext)

    val mUpdate = MutableLiveData<Boolean>()
    var update: LiveData<Boolean> = mUpdate

    private val mUpdateOff = MutableLiveData<Boolean>()
    var updateDb: LiveData<Boolean> = mUpdateOff

    /**
     * Função responsável por fazer o update do usuário no servidor, caso haja internet.
     */
    fun update(nome: String, cpf: String, email: String, telefone: String, nascimento: String, senha: String) {
        mUserRepositoryAPI.update(nome, cpf, email, telefone, nascimento, senha, object : APIListener{
            override fun onSuccess(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
                // Se o sucesso enviado pela API for True, ele faz a edição.
                // Caso contrário, chama a função de setEditFalse, mostrando a mensagem de erro.
                if (modelAPI.sucesso){
                    // Armazenando os dados recebidos da API nas variáveis
                    val responseNome = modelAPI.data?.nome.toString()
                    val responseCpf = modelAPI.data?.cpf.toString()
                    val responseEmail = modelAPI.data?.email.toString()
                    val responseTelefone = modelAPI.data?.telefone.toString()
                    val responseNascimento = modelAPI.data?.nascimento.toString()

                    // Salvando os dados no SharedPreferences
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NOME, responseNome) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.CPF, responseCpf) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.EMAIL, responseEmail) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.TELEFONE, responseTelefone) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NASCIMENTO, responseNascimento) }

                    // Criando o modelo User, e passando os dados recebidos pela API, armazenados nas variáveis
                    val user = UserLoginModelDB(cpf, senha, responseNome, responseCpf, responseEmail, responseTelefone, responseNascimento)
                    mUserRepositoryDB.updateUser(user)
                    setEditTrue(modelAPI)
                }else{
                    setEditFalse(modelAPI)
                }
            }

            override fun onFailure(str: String) {
                mUpdate.value = false
            }
        })
    }

    /**
     * Função responsável por mandar para o observe o valor Falso, e mostrar para
     * o usuário a mensagem de erro que foi enviada pela API.
     */
    fun setEditFalse(modelAPI: BaseResponse<UserModelAPI.LoginResponse>){
        mUpdate.value = false
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
    }

    /**
     * Função responsável por mandar para o observe o valor True, e mostrar para
     * o usuário a mensagem de erro que foi enviada pela API.
     */
    fun setEditTrue(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
        mUpdate.value = true
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
    }

    /**
     * Função responsável por fazer o Update do usuário no banco, caso não haja internet.
     */
    fun updateUserDb(cpf: String, senha: String, responseNome: String, responseCpf: String, responseEmail: String, responseTelefone: String, responseNascimento: String, isSync: Int) {
        val user = UserLoginModelDB(cpf, senha, responseNome, responseCpf, responseEmail, responseTelefone, responseNascimento)
        mUserRepositoryDB.updateUser(user)
        // Fazendo Update somente da coluna is_sync do banco.
        mUserRepositoryDB.updateUnsyncUser(userCpf = cpf, isSync = isSync)

        // Salvando os novos dados do usuário no SharedPreferences
        mSharedPreferences.store(SharedPreferencesConstants.SHARED.NOME, responseNome)
        mSharedPreferences.store(SharedPreferencesConstants.SHARED.CPF, responseCpf)
        mSharedPreferences.store(SharedPreferencesConstants.SHARED.EMAIL, responseEmail)
        mSharedPreferences.store(SharedPreferencesConstants.SHARED.TELEFONE, responseTelefone)
        mSharedPreferences.store(SharedPreferencesConstants.SHARED.NASCIMENTO, responseNascimento)

        mUpdateOff.value = true
    }

}