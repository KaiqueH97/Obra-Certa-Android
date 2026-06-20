package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mapeamento dos botões e textos
        val btnSair = findViewById<Button>(R.id.btnSairSistema)
        val tvVoltar = findViewById<TextView>(R.id.tvVoltarPerfil)
        val tvNomeUsuario = findViewById<TextView>(R.id.tvNomeUsuario)
        val tvEditarNome = findViewById<TextView>(R.id.tvEditarNome)

        // Lógica para Editar o Nome usando um Pop-up (AlertDialog)
        tvEditarNome.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Editar Nome")

            // Cria um campo de texto (EditText) programaticamente para a caixinha
            val input = EditText(this)
            input.setText(tvNomeUsuario.text) // Deixa o nome atual já preenchido
            input.setSelection(input.text.length) // Coloca o cursor no final da palavra
            builder.setView(input)

            // Botão Salvar do Pop-up
            builder.setPositiveButton("Salvar") { dialog, _ ->
                val novoNome = input.text.toString().trim()
                if (novoNome.isNotEmpty()) {
                    // Atualiza o texto na tela
                    tvNomeUsuario.text = novoNome

                    /* * DICA FUTURA PARA O BANCO DE DADOS:
                     * Quando você implementar a sessão de usuário (ex: SharedPreferences salvando quem logou),
                     * é exatamente aqui dentro deste IF que você faria o db.usuarioDao().atualizarNomeUsuario(id, novoNome)
                     */
                }
                dialog.dismiss()
            }

            // Botão Cancelar do Pop-up
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }

        // Lógica de Sair
        btnSair.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Lógica de Voltar
        tvVoltar.setOnClickListener { finish() }
    }
}