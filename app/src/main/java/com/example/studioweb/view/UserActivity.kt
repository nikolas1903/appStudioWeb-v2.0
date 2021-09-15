package com.example.studioweb.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.studioweb.R
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.services.repository.sync.ConnectionLiveData
import com.example.studioweb.services.repository.sync.SyncData
import com.example.studioweb.view.utils.CpfMaskTv
import com.example.studioweb.view.utils.DataMaskTv
import com.example.studioweb.view.utils.TelefoneMaskTv
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.activity_user.image_back
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserActivity : AppCompatActivity() {
    private lateinit var mConnectionLiveData: ConnectionLiveData
    private lateinit var mSecurityPreferences: SecurityPreferences
    private lateinit var mSyncData : SyncData

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Removendo ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_user)

        mConnectionLiveData = ConnectionLiveData(application)
        mSecurityPreferences = SecurityPreferences(application)
        mSyncData = SyncData(application)

        // Setando as máscaras nos TextViews.
        tv_cpfCompleto.addTextChangedListener(CpfMaskTv.mask("###.###.###-##", tv_cpfCompleto))
        tv_telefoneCompleto.addTextChangedListener(TelefoneMaskTv.insert("(##) # ####-####", tv_telefoneCompleto))
        tv_nascimentoCompleto.addTextChangedListener(DataMaskTv.insert("##/##/####", tv_nascimentoCompleto))

        observe()
        setData()
        setListeners()
    }

    /**
     * Função responsável por observar as mudanças, e executar uma ação.
     */
    private fun observe() {
        mConnectionLiveData.observe(this, {
            if (it) {
                // Se tiver internet, ele esconde o aviso.
                constraint_offline.visibility = GONE
                tv_offline.visibility = GONE
                image_loading.visibility = GONE
                // E chama a função de sincronização a cada certo intervalo de tempo.
                mSyncData.startSync()
            } else {
                // Caso não tenha internet, mostra o aviso.
                constraint_offline.visibility = VISIBLE
                tv_offline.visibility = VISIBLE
                image_loading.visibility = VISIBLE
            }
        })
    }

    /**
     * Função responsável por colocar os dados do usuário, cadastrados
     * no SharedPreferences, nos TextViews.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        // Setando dados do usuário na tela principal.
        tv_nomeCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME)
        tv_emailCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.EMAIL)
        tv_cpfCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF)
        tv_telefoneCompleto.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.TELEFONE)

        //Formatando Nascimento
        val nascimentoShared = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NASCIMENTO)
        val nascimentoT = nascimentoShared.replaceAfter("T", "")
        val nascimentoInvalido = nascimentoT.replace("T", "")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nascimento = LocalDate.parse(nascimentoInvalido, formatter2).format(formatter).toString()

        tv_nascimentoCompleto.text = nascimento
    }

    /**
     * Função responsável por escutar os cliques do usuário e fazer validação de campos.
     */
    private fun setListeners() {
        //Listener do botão de Logout
        tv_logout.setOnClickListener {
            Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Listener do botão de Alterar Dados
        tv_alterarDados.setOnClickListener {
            intent = Intent(this, EditUserActivity::class.java)
            startActivity(intent)
        }

        //Listener do botão de voltar
        image_back.setOnClickListener {
            intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
