package com.example.studioweb.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.studioweb.services.repository.remote.user.UserRepositoryAPI
import com.example.studioweb.services.repository.remote.user.generics.BaseResponse
import java.io.ByteArrayOutputStream
import java.util.*


@SuppressLint("StaticFieldLeak")
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mContext = application.applicationContext
    private val mSharedPreferences = SecurityPreferences(application)
    private val mUserRepository = UserRepositoryAPI()
    private val mUserRepositoryDB = UserRepositoryDB.getInstance(mContext)

    val mLogin = MutableLiveData<Boolean>()
    var login: LiveData<Boolean> = mLogin

    private val mLoginOffline = MutableLiveData<Boolean>()
    var loginOffline: LiveData<Boolean> = mLoginOffline

    /**
     * Função responsável por fazer o Login, caso haja internet.
     */
    fun doLogin(cpf: String, senha: String) {
        mUserRepository.login(cpf, senha, object : APIListener {
            override fun onSuccess(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
                // Se o sucesso passado pela API for True, ele vai pegar as respostas e armazenar
                // em variáveis, salvar no SharedPreferences e fazer o Login.
                if (modelAPI.sucesso) {
                    // Salvando resposta da API
                    val responseNome = modelAPI.data?.nome.toString()
                    val responseCpf = modelAPI.data?.cpf.toString()
                    val responseEmail = modelAPI.data?.email.toString()
                    val responseNascimento = modelAPI.data?.nascimento.toString()
                    val responseTelefone = modelAPI.data?.telefone.toString()

                    // Armazenando respostas no SharedPreferences
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NOME, responseNome) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.CPF, responseCpf) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.EMAIL, responseEmail) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.TELEFONE, responseTelefone) }
                    modelAPI.data?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NASCIMENTO, responseNascimento) }

                    // Armazenando o CPF do usuário, caso exista no banco de dados, em uma variável.
                    val resCpf = mUserRepositoryDB.searchUser(cpf, senha)?.responseCpf
                    val isSync = 0

                    // Se não encontrar o CPF do usuário no banco, ele vai cadastrar o usuário lá.
                    if(resCpf == null) {
                        val user = UserLoginModelDB(
                            cpf, senha, responseNome, responseCpf, responseEmail, responseTelefone, responseNascimento, isSync)
                        mUserRepositoryDB.register(user)
                    }

                    setLoginTrue(modelAPI)
                } else {
                    setLoginFalse(modelAPI)
                }
            }

            override fun onFailure(str: String) {
                mLogin.value = false
            }
        })
    }

    /**
     * Função responsável por tentar fazer o Login do usuário quando ele tiver Offline.
     */
    fun tryLoginOffline(cpf: String, senha: String) {
        // Armazena a resposta do CPF do usuário nessa variável
        val resCpf = mUserRepositoryDB.searchUser(cpf, senha)?.responseCpf
        // Faz uma procura no banco de dados pelo CPF armazenado na variável anterior.
        mUserRepositoryDB.searchUser(cpf, senha)

        // Se a procura for Null, é por que o usuário ainda não fez Login nesse dispositivo
        // enquanto estava Online, e então mostra uma mensagem que o usuário não foi encontrado.
        if (resCpf == null) {
            Toast.makeText(getApplication(), "Usuário não encontrado!", Toast.LENGTH_LONG).show()
        } else {
            // Se a procura não for Null, é pq existe um usuário cadastrado.
            mLoginOffline.value = true

            // Armazena os dados do banco em variáveis.
            val responseNome = mUserRepositoryDB.searchUser(cpf, senha)?.responseNome
            val responseEmail = mUserRepositoryDB.searchUser(cpf, senha)?.responseEmail
            val responseCpf = mUserRepositoryDB.searchUser(cpf, senha)?.responseCpf
            val responseTelefone = mUserRepositoryDB.searchUser(cpf, senha)?.responseTelefone
            val responseNascimento = mUserRepositoryDB.searchUser(cpf, senha)?.responseNascimento

            // Armazena as variáveis no Shared Preferences.
            responseNome?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NOME, it) }
            responseEmail?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.EMAIL, it) }
            responseCpf?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.CPF, it) }
            responseTelefone?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.TELEFONE, it) }
            responseNascimento?.let { mSharedPreferences.store(SharedPreferencesConstants.SHARED.NASCIMENTO, it) }
        }
    }

    /**
     * Seta o Login como True, e manda a mensagem enviada pela API para o usuário.
     */
    private fun setLoginTrue(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
        mLogin.value = true
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
    }

    /**
     * Seta o Login como False, e manda a mensagem enviada pela API para o usuário.
     */
    private fun setLoginFalse(modelAPI: BaseResponse<UserModelAPI.LoginResponse>) {
        Toast.makeText(getApplication(), modelAPI.mensagem, Toast.LENGTH_SHORT).show()
        mLogin.value = false

    }

     fun updateProfilePic(cpf: String, image: ByteArray?){
        mUserRepositoryDB.updateProfilePic(cpf, image)


         mSharedPreferences.storeImage(SharedPreferencesConstants.SHARED.IMAGEM, image)
    }

    @ExperimentalUnsignedTypes
    fun getProfilePic(cpf: String) : Bitmap? {
       return mUserRepositoryDB.getProfilePic(cpf)
    }

}