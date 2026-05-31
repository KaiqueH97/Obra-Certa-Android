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

            // Mapeando os botões visuais do seu layout
            val btnAbrirObra = viewDoCartao.findViewById<TextView>(R.id.btnAbrirObra)
            val btnEditar = viewDoCartao.findViewById<TextView>(R.id.btnEditarProjeto)
            val btnExcluir = viewDoCartao.findViewById<TextView>(R.id.btnExcluirProjeto)

            // 1. Ação: Abrir Obra (Passa os dados pela Intent)
            btnAbrirObra.setOnClickListener {
                val intent = Intent(this, DetalhesProjetoActivity::class.java)
                intent.putExtra("PROJETO_ID", projeto.id)
                intent.putExtra("PROJETO_NOME", projeto.nomeCliente)
                startActivity(intent)
            }

            // 2. Ação: Excluir Obra (Com aviso de segurança)
            btnExcluir.setOnClickListener {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Excluir Obra")
                    .setMessage("Apagar '${projeto.nomeCliente}'? Todas as tarefas e materiais serão excluídos!")
                    .setPositiveButton("Sim, apagar") { _, _ ->
                        db.projetoDao().deletarProjeto(projeto)
                        atualizarListaNaTela()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }

            // 3. Ação: Editar Obra (Abre caixa para digitar novo nome)
            btnEditar.setOnClickListener {
                val inputNome = EditText(this)
                inputNome.setText(projeto.nomeCliente)
                inputNome.setPadding(50, 50, 50, 50)

                android.app.AlertDialog.Builder(this)
                    .setTitle("Editar Nome do Cliente")
                    .setView(inputNome)
                    .setPositiveButton("Salvar") { _, _ ->
                        val novoNome = inputNome.text.toString()
                        if (novoNome.isNotBlank()) {
                            val projetoAtualizado = projeto.copy(nomeCliente = novoNome)
                            db.projetoDao().atualizarProjeto(projetoAtualizado)
                            atualizarListaNaTela() // Recarrega com o nome novo
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            llListaProjetos.addView(viewDoCartao)
        }
    }
}