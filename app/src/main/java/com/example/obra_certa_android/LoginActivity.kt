package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obra_certa_android.database.AppDatabase

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

        // 1. MAPEAMENTO ÚNICO DE COMPONENTES
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etSenha = findViewById<EditText>(R.id.etSenha)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val tvCadastreSe = findViewById<TextView>(R.id.tvCadastreSe)
        val tvEsqueceuSenha = findViewById<TextView>(R.id.tvEsqueceuSenha)

        // Inicializa o banco de dados
        val db = AppDatabase.getDatabase(this)

        // 2. LÓGICA DE AUTENTICAÇÃO (Botão Entrar)
        btnEntrar.setOnClickListener {
            val emailDigitado = etEmail.text.toString()
            val senhaDigitada = etSenha.text.toString()

            if (emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // O banco de dados vai procurar o usuário
            val usuarioLogado = db.usuarioDao().autenticarUsuario(emailDigitado, senhaDigitada)

            if (usuarioLogado != null) {
                // Deu certo! Navega para a Home
                Toast.makeText(this, "Bem-vindo, ${usuarioLogado.nome}!", Toast.LENGTH_SHORT).show()
                val intentNavegacao = Intent(this, HomeActivity::class.java)
                startActivity(intentNavegacao)
                finish() // Fecha a tela de login
            } else {
                // Errou a senha ou e-mail não existe
                Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. NAVEGAÇÃO PARA CADASTRO
        tvCadastreSe.setOnClickListener {
            val intentCadastro = Intent(this, CadastroActivity::class.java)
            startActivity(intentCadastro)
        }

        // 4. NAVEGAÇÃO PARA RECUPERAR SENHA
        tvEsqueceuSenha.setOnClickListener {
            val intentRecuperar = Intent(this, RecuperarActivity::class.java)
            startActivity(intentRecuperar)
        }
    }
}