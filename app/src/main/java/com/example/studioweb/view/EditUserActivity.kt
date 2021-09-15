package com.example.studioweb.view

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.R
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.example.studioweb.view.utils.CpfMask
import com.example.studioweb.view.utils.DataMask
import com.example.studioweb.view.utils.TelefoneMask
import com.example.studioweb.viewmodel.EditUserViewModel
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.activity_register.image_back
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditUserActivity : AppCompatActivity() {
    private lateinit var mEditViewModel: EditUserViewModel
    private lateinit var mSecurityPreferences: SecurityPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() //Removendo ActionBar
        setContentView(R.layout.activity_edit_user)

        mEditViewModel = ViewModelProvider(this).get(EditUserViewModel::class.java)
        mSecurityPreferences = SecurityPreferences(this)

        // Setando as Máscaras
        et_editCpf.addTextChangedListener(CpfMask.mask("###.###.###-##", et_editCpf))
        et_editTelefone.addTextChangedListener(TelefoneMask.insert("(##)#####-####", et_editTelefone));
        et_editNascimento.addTextChangedListener(DataMask.insert("##/##/####", et_editNascimento));

        setListeners()
        observe()
        setData()
    }

    /**
     * Função responsável por observar as mudanças, e executar uma ação.
     */
    private fun observe() {
        mEditViewModel.update.observe(this, Observer {
            if (it) {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        mEditViewModel.updateDb.observe(this, Observer {
            if (it) {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Usuário alterado localmente. Fique online para sincronizar.", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Função responsável por colocar os dados do usuário, cadastrados
     * no SharedPreferences, nos EditTexts responsáveis pela edição dos dados.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        et_editNome.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME))
        et_editCpf.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.CPF))
        et_editEmail.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.EMAIL))
        et_editTelefone.setText(mSecurityPreferences.get(SharedPreferencesConstants.SHARED.TELEFONE))
        //Formatando Nascimento
        val nascimentoShared =
            mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NASCIMENTO)
        val nascimentoT = nascimentoShared.replaceAfter("T", "")
        val nascimentoInvalido = nascimentoT.replace("T", "")
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nascimento1 =
            LocalDate.parse(nascimentoInvalido, formatter2).format(formatter).toString()
        val nascimento = nascimento1.replace("-", "/")

        et_editNascimento.setText(nascimento)
    }

    /**
     * Função responsável por escutar os cliques do usuário e fazer validação de campos.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        // Botão Alterar Dados
        btn_editar.setOnClickListener {
            val nome = et_editNome.text.toString()
            val cpf = et_editCpf.text.toString()
            val email = et_editEmail.text.toString()
            val telefone = et_editTelefone.text.toString()
            val nascimentoInvalido = et_editNascimento.text.toString()
            val senha = et_editSenha.text.toString()
            val confirmaSenha = et_editConfirmaSenha.text.toString()

            val nascimento1 = nascimentoInvalido.replace("/", "-")
            val formatoEnviado = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formatoDesejado = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val nascimento = LocalDate.parse(nascimento1, formatoEnviado).format(formatoDesejado).toString()

            if (nome == "" || cpf == "" || email == "" || telefone == "" || nascimento == "") {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (!validEmail()) {
                Toast.makeText(this, "Informe um e-mail válido!", Toast.LENGTH_SHORT).show()
            } else if (senha != confirmaSenha) {
                Toast.makeText(this, "As duas senhas devem ser iguais!", Toast.LENGTH_SHORT).show()
                if (senha.length < 6) {
                    Toast.makeText(this, "Sua senha deve ter no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (isNetworkAvailable()) {
                    mEditViewModel.update(nome, cpf, email, telefone, nascimento, senha)
                } else {
                    val isSync = 0
                    mEditViewModel.updateUserDb(cpf, senha, nome, cpf, email, telefone, nascimento, isSync)
                }
            }
        }

        //Listener do botão de voltar
        image_back.setOnClickListener {
            intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Função responsável por validar o e-mail digitado pelo usuário.
     */
    private fun validEmail(): Boolean {
        val str = et_editEmail.text.toString()
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    /**
     * Função responsável por verificar se há conexão de internet (não é em tempo real).
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}