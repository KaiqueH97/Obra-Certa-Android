package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intentNavegacao = Intent(this, HomeActivity::class.java)
            startActivity(intentNavegacao)
            finish()
        }
        val tvCadastreSe = findViewById<TextView>(R.id.tvCadastreSe)
        tvCadastreSe.setOnClickListener {
            val intentCadastro = Intent(this, CadastroActivity::class.java)
            startActivity(intentCadastro)
        }
    }
}