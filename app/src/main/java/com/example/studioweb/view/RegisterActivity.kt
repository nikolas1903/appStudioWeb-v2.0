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
import com.example.studioweb.view.utils.CpfMask
import com.example.studioweb.view.utils.DataMask
import com.example.studioweb.view.utils.TelefoneMask
import com.example.studioweb.viewmodel.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RegisterActivity : AppCompatActivity() {

    private lateinit var mRegisterViewModel: RegisterViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() //Removendo ActionBar
        setContentView(R.layout.activity_register)

        mRegisterViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Setando as máscaras
        et_registerCpf.addTextChangedListener(CpfMask.mask("###.###.###-##", et_registerCpf))
        et_registerTelefone.addTextChangedListener(TelefoneMask.insert("(##)#####-####", et_registerTelefone))
        et_registerNascimento.addTextChangedListener(DataMask.insert("##/##/####", et_registerNascimento))

        setListeners()
        observe()
    }

    /**
     * Função responsável por observar as mudanças, e executar uma ação.
     */
    private fun observe() {
        //Observa se o usuário fez o Loggin, e muda ele pra MainActivity
        mRegisterViewModel.create.observe(this, Observer {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    /**
     * Função responsável por escutar os cliques do usuário e fazer validação de campos.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        //Listener do botão de registrar!
        btn_registrar.setOnClickListener {
            if (!isNetworkAvailable()){
                Toast.makeText(this, "Você precisa estar conectado!", Toast.LENGTH_SHORT).show()
            }else {
                val nome = et_registerNome.text.toString()
                val cpf = et_registerCpf.text.toString()
                val email = et_registerEmail.text.toString()
                val nascimentoInvalido = et_registerNascimento.text.toString()
                val telefone = et_registerTelefone.text.toString()
                val senha = et_registerSenha.text.toString()
                val confirmaSenha = et_confirmaRegisterSenha.text.toString()

                if (nome == "" || cpf == "" || email == "" || telefone == "" || nascimentoInvalido == "" || senha == "" || confirmaSenha == "") {
                    Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                } else if (!validEmail()) {
                    Toast.makeText(this, "Informe um e-mail válido!", Toast.LENGTH_SHORT).show()
                } else if (senha != confirmaSenha) {
                    Toast.makeText(this, "As duas senhas devem ser iguais!", Toast.LENGTH_SHORT)
                        .show()
                } else if (senha.length < 6) {
                    Toast.makeText(
                        this,
                        "Sua senha deve ter no mínimo 6 caracteres!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val nascimento1 = nascimentoInvalido.replace("/", "-")
                    val formatoEnviado = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val formatoDesejado = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    val nascimento =
                        LocalDate.parse(nascimento1, formatoEnviado).format(formatoDesejado)
                            .toString()
                    mRegisterViewModel.create(nome, cpf, email, telefone, nascimento, senha)
                }
            }
        }

        //Listener do botão de voltar
        image_back.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Função responsável por validar o e-mail digitado pelo usuário.
     */
    private fun validEmail(): Boolean {
        val str = et_registerEmail.text.toString()
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