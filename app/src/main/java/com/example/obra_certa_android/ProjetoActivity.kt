package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obra_certa_android.database.AppDatabase
import com.example.obra_certa_android.database.Projeto

class ProjetoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var llListaProjetos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_projeto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)

        val txtVoltar = findViewById<TextView>(R.id.tvVoltarProjetos)
        txtVoltar.setOnClickListener { finish() }

        llListaProjetos = findViewById(R.id.llListaProjetos)
        val etNovoProjeto = findViewById<EditText>(R.id.etNovoProjeto)
        val btnSalvarProjeto = findViewById<Button>(R.id.btnSalvarProjeto)

        atualizarListaNaTela()

        btnSalvarProjeto.setOnClickListener {
            val nomeDigitado = etNovoProjeto.text.toString()

            if (nomeDigitado.isNotBlank()) {
                val novoProjeto = Projeto(nomeCliente = nomeDigitado)

                db.projetoDao().inserirProjeto(novoProjeto)

                etNovoProjeto.text.clear()
                Toast.makeText(this, "Projeto salvo!", Toast.LENGTH_SHORT).show()

                atualizarListaNaTela()
            } else {
                Toast.makeText(this, "Por favor, digite um nome.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun atualizarListaNaTela() {
        llListaProjetos.removeAllViews()

        val listaDoBanco = db.projetoDao().buscarTodosProjetos()

        for (projeto in listaDoBanco) {
            val viewDoCartao = LayoutInflater.from(this).inflate(R.layout.item_projeto, llListaProjetos, false)
            val tvNomeCliente = viewDoCartao.findViewById<TextView>(R.id.tvNomeClienteItem)
            tvNomeCliente.text = projeto.nomeCliente

            viewDoCartao.setOnClickListener {
                val intent = Intent(this, DetalhesProjetoActivity::class.java)
                intent.putExtra("PROJETO_ID", projeto.id)
                intent.putExtra("PROJETO_NOME", projeto.nomeCliente)
                startActivity(intent)
            }
            llListaProjetos.addView(viewDoCartao)
        }
    }
}