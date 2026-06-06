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
import com.example.obra_certa_android.database.Usuario

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

        // 1. MAPEAMENTO DOS COMPONENTES DA TELA
        val etNome = findViewById<EditText>(R.id.etNomeCadastro)
        val etEmail = findViewById<EditText>(R.id.etEmailCadastro)
        val etSenha = findViewById<EditText>(R.id.etSenhaCadastro)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)
        val tvEntrarAqui = findViewById<TextView>(R.id.tvEntrarAqui)

        // Inicializa o banco de dados
        val db = AppDatabase.getDatabase(this)

        // 2. LÓGICA DE SALVAMENTO (CREATE)
        btnCadastrar.setOnClickListener {
            val nomeDigitado = etNome.text.toString().trim()
            val emailDigitado = etEmail.text.toString().trim()
            val senhaDigitada = etSenha.text.toString().trim()

            // Validação de segurança: Não deixa salvar se tiver campo vazio
            if (nomeDigitado.isEmpty() || emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica se o e-mail já foi usado antes
            val usuarioExistente = db.usuarioDao().verificarEmailExistente(emailDigitado)

            if (usuarioExistente != null) {
                Toast.makeText(this, "Este e-mail já está cadastrado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Monta o "pacote" do novo usuário
            val novoUsuario = Usuario(
                nome = nomeDigitado,
                email = emailDigitado,
                senha = senhaDigitada
            )

            // Salva definitivamente no banco de dados local
            db.usuarioDao().inserirUsuario(novoUsuario)

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

            // Fecha a tela de cadastro e volta automaticamente para o Login
            finish()
        }

        // 3. NAVEGAÇÃO DE VOLTA PARA O LOGIN
        tvEntrarAqui.setOnClickListener {
            // O finish() simplesmente destrói a tela de cadastro, revelando a tela de login que já estava embaixo
            finish()
        }
    }
}