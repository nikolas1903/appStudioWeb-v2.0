package com.example.studioweb.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.studioweb.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() //Removendo ActionBar
        setContentView(R.layout.activity_splash)

        changeToLogin()
    }

    private fun changeToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            intent.change()
        }, 2000)
    }

    private fun Intent.change() {
        startActivity(this)
        finish()
    }
}