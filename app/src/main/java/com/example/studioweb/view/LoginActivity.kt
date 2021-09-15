package com.example.studioweb.view

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.studioweb.R
import com.example.studioweb.view.utils.CpfMask
import com.example.studioweb.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity() : AppCompatActivity() {

    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        mLoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Setando a máscara de CPF
        et_cpf.addTextChangedListener(CpfMask.mask("###.###.###-##", et_cpf))

        setListeners()
        observe()
    }

    /**
     * Função responsável por observar as mudanças, e executar uma ação.
     */
    private fun observe() {
        //Observa se o usuário fez o Loggin, e muda ele pra MainActivity
        mLoginViewModel.login.observe(this, Observer {
            if (it) {
                val intent = Intent(this, InicioActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        mLoginViewModel.loginOffline.observe(this, Observer{
            if (it){
                val intent = Intent(this, InicioActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Logado com sucesso!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Função responsável por escutar os cliques do usuário e fazer validação de campos.
     */
    private fun setListeners() {
        //Listener Login
        btn_login.setOnClickListener {
            // Armazenando o que o usuário digitou.
            val cpfInvalido = et_cpf.text.toString()
            val cpf = cpfInvalido.replace(".", "").replace("-", "") //Substituindo os caractéres invalidos do CPF
            val senha = et_senha.text.toString()

            //Validação do CPF e Senha.
            if (cpf.length == 11 && senha.length >= 6) {
                if(isNetworkAvailable()){
                    // Se tiver conexão, ele faz o Login, passando o CPF e a SENHA digitados.
                    mLoginViewModel.doLogin(cpf, senha)
                } else{
                    // Se não tiver conexão, ele faz tenta fazer Login Offline.
                    mLoginViewModel.tryLoginOffline(cpf, senha)
                }
            } else {
                Toast.makeText(this, "Campos inválidos!", Toast.LENGTH_SHORT).show()
            }
        }

        //Listener Registrar
        tv_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Listener Logo - levar para o Insta
        img_logo.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/studioweb.digital"))
            startActivity(browserIntent)
        }
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