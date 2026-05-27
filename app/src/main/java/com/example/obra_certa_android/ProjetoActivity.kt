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

    // Variáveis globais para o Banco de Dados e a Lista visual
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

        // 1. Iniciar a conexão com o Banco de Dados
        db = AppDatabase.getDatabase(this)

        val txtVoltar = findViewById<TextView>(R.id.tvVoltarProjetos)
        txtVoltar.setOnClickListener { finish() }

        // Mapeando a tela
        llListaProjetos = findViewById(R.id.llListaProjetos)
        val etNovoProjeto = findViewById<EditText>(R.id.etNovoProjeto)
        val btnSalvarProjeto = findViewById<Button>(R.id.btnSalvarProjeto)

        // 2. Carregar os projetos salvos assim que a tela abre
        atualizarListaNaTela()

        // 3. Ação de Salvar um novo projeto
        btnSalvarProjeto.setOnClickListener {
            val nomeDigitado = etNovoProjeto.text.toString()

            if (nomeDigitado.isNotBlank()) {
                // Prepara a entidade
                val novoProjeto = Projeto(nomeCliente = nomeDigitado)

                // Salva no SQLite
                db.projetoDao().inserirProjeto(novoProjeto)

                // Limpa o campo e avisa o usuário
                etNovoProjeto.text.clear()
                Toast.makeText(this, "Projeto salvo!", Toast.LENGTH_SHORT).show()

                // Recarrega a lista para mostrar o projeto novo
                atualizarListaNaTela()
            } else {
                Toast.makeText(this, "Por favor, digite um nome.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função que busca no banco e desenha os cartões na tela
    private fun atualizarListaNaTela() {
        // Limpa tudo que já estava desenhado para não duplicar
        llListaProjetos.removeAllViews()

        // Puxa a lista direto do banco de dados SQLite
        val listaDoBanco = db.projetoDao().buscarTodosProjetos()

        // Para cada Projeto no banco, ele cria um cartão usando o 'item_projeto.xml'
        for (projeto in listaDoBanco) {
            // Inflar o molde
            val viewDoCartao = LayoutInflater.from(this).inflate(R.layout.item_projeto, llListaProjetos, false)

            // Alterar o texto do molde para o nome que veio do banco
            val tvNomeCliente = viewDoCartao.findViewById<TextView>(R.id.tvNomeClienteItem)
            tvNomeCliente.text = projeto.nomeCliente

            // Ação de clique do cartão (Ir para a tela de Detalhes)
            viewDoCartao.setOnClickListener {
                val intent = Intent(this, DetalhesProjetoActivity::class.java)
                // Mais pra frente vamos passar o ID do projeto nessa Intent!
                startActivity(intent)
            }

            // Adicionar o cartão pronto na tela
            llListaProjetos.addView(viewDoCartao)
        }
    }
}