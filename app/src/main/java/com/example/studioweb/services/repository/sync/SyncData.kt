package com.example.studioweb.services.repository.sync

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.studioweb.services.repository.local.UserRepositoryDB
import com.example.studioweb.services.repository.models.UserUpdateModel
import com.example.studioweb.services.repository.remote.UserRepositoryAPI
import com.google.gson.Gson
import org.json.JSONObject

import org.json.JSONArray

/**
 * Classe que sincroniza os dados alterados do usuário no banco com o servidor.
 */

class SyncData(application: Application) {
    private val mContext = application.applicationContext
    private val mUserRepositoryAPI = UserRepositoryAPI()
    private val mUserRepositoryDB = UserRepositoryDB.getInstance(mContext)

    fun startSync() {
            // Peguei todos os usuários do banco que não foram sincronizados e botei dentro de um Array
            val unsyncUsers = mUserRepositoryDB.getUnsyncUsers()

            // Agora faço um FOR para percorrer a lista dos usuários, e para cada usuário, mando um update para a API.
            for (user in unsyncUsers) {

                //Fazendo o update na API
                mUserRepositoryAPI.updateUnsyncUsers(user.Nome, user.CPF, user.Email, user.Telefone, user.Nascimento, user.Senha)

                //Setando o isSync como true, para este usuário não vir mais para o Array acima
                val isSync = 1
                mUserRepositoryDB.updateUnsyncUser(user.CPF, isSync)
            }
    }
}