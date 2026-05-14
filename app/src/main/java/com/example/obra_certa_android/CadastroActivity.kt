package com.example.obra_certa_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvEntrarAqui = findViewById<TextView>(R.id.tvEntrarAqui)
        tvEntrarAqui.setOnClickListener {
            finish()
        }

        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)
        val etNome = findViewById<EditText>(R.id.etNomeCadastro)
        val etEmail = findViewById<EditText>(R.id.etEmailCadastro)
        val etSenha = findViewById<EditText>(R.id.etSenhaCadastro)

        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Conta criada com sucesso para $nome!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}