package com.example.studioweb.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studioweb.R
import com.example.studioweb.constants.SharedPreferencesConstants
import com.example.studioweb.services.repository.local.SecurityPreferences
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_home.*


class InicioActivity : AppCompatActivity() {
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_orcamento)
        mSecurityPreferences = SecurityPreferences(application)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setData()
        setListener()
        setupNavigation()
    }

    private fun setData() {
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val tvNomeNav = headerView.findViewById<View>(R.id.tv_nomeNav) as TextView
        tvNomeNav.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME)
        val tvEmailNav = headerView.findViewById<View>(R.id.tv_emailNav) as TextView
        tvEmailNav.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.EMAIL)

        tv_nomeHome.text = mSecurityPreferences.get(SharedPreferencesConstants.SHARED.NOME)

    }

    // Faz o menu abrir
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_orcamento)
        return navController.navigateUp(mAppBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_content_orcamento)

        mAppBarConfiguration =
            AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_orcamento,
                R.id.nav_meusOrcamentos,
                R.id.nav_todosUsuarios,
            )
                .setDrawerLayout(drawerLayout)
                .build()
        setupActionBarWithNavController(navController, mAppBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setListener(){
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)

        // Listener do botão de Editar Usuário
        val btnEditUser = headerView.findViewById<View>(R.id.btn_editUser) as Button
        btnEditUser.setOnClickListener{
            val intent = Intent(applicationContext, UserActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Listener do botão de Logout
        val btnLogout = headerView.findViewById<View>(R.id.btn_sairNav) as Button
        btnLogout.setOnClickListener{
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(applicationContext, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}