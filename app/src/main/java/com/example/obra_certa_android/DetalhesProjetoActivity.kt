package com.example.obra_certa_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obra_certa_android.database.AppDatabase
import com.example.obra_certa_android.database.Tarefa

class DetalhesProjetoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var projetoId: Int = -1
    private lateinit var llListaTarefas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_projeto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Iniciar Banco e pegar a ID da Mochila
        db = AppDatabase.getDatabase(this)
        projetoId = intent.getIntExtra("PROJETO_ID", -1)
        val projetoNome = intent.getStringExtra("PROJETO_NOME") ?: "Projeto Desconhecido"

        val tvNomeProjetoDetalhe = findViewById<TextView>(R.id.tvNomeProjetoDetalhe)
        tvNomeProjetoDetalhe.text = projetoNome

        val tvVoltar = findViewById<TextView>(R.id.tvVoltarDetalhes)
        tvVoltar.setOnClickListener { finish() }

        // 2. Mapeamento da Tela (Abas)
        val btnAbaOrcamento = findViewById<LinearLayout>(R.id.btnAbaOrcamento)
        val btnAbaTarefas = findViewById<LinearLayout>(R.id.btnAbaTarefas)
        val llAbaOrcamento = findViewById<LinearLayout>(R.id.llAbaOrcamento)
        val llAbaTarefas = findViewById<LinearLayout>(R.id.llAbaTarefas)

        // Lógica de clique nas abas
        btnAbaOrcamento.setOnClickListener {
            llAbaOrcamento.visibility = View.VISIBLE
            llAbaTarefas.visibility = View.GONE
            btnAbaOrcamento.setBackgroundResource(R.drawable.bg_aba_ativa)
            btnAbaTarefas.setBackgroundResource(R.drawable.bg_aba_inativa)
        }

        btnAbaTarefas.setOnClickListener {
            llAbaTarefas.visibility = View.VISIBLE
            llAbaOrcamento.visibility = View.GONE
            btnAbaTarefas.setBackgroundResource(R.drawable.bg_aba_ativa)
            btnAbaOrcamento.setBackgroundResource(R.drawable.bg_aba_inativa)
        }

        // 3. Mapeamento das Tarefas
        llListaTarefas = findViewById(R.id.llListaTarefas)
        val etNovaTarefa = findViewById<EditText>(R.id.etNovaTarefa)
        val btnAdicionarTarefa = findViewById<Button>(R.id.btnAdicionarTarefa)

        // Carregar as tarefas assim que a tela abre
        atualizarListaTarefas()

        // 4. Salvar Nova Tarefa
        btnAdicionarTarefa.setOnClickListener {
            val textoTarefa = etNovaTarefa.text.toString()

            // Verifica se não está vazio e se a obra é válida (ID diferente de -1)
            if (textoTarefa.isNotBlank() && projetoId != -1) {
                // Prepara a entidade amarrando ela na ID da obra
                val novaTarefa = Tarefa(nomeTarefa = textoTarefa, projetoId = projetoId)

                // Salva no banco e atualiza a tela
                db.tarefaDao().inserirTarefa(novaTarefa)
                etNovaTarefa.text.clear()
                atualizarListaTarefas()
            } else {
                Toast.makeText(this, "Digite uma tarefa válida.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 5. Função que busca no banco as tarefas exclusivas desta obra
    private fun atualizarListaTarefas() {
        llListaTarefas.removeAllViews()

        // Se por acaso a ID quebrou na passagem, não faz nada
        if (projetoId == -1) return

        // Busca filtrada! Só puxa tarefas onde a Chave Estrangeira bate com a ID aberta
        val listaDoBanco = db.tarefaDao().buscarTarefasPorProjeto(projetoId)

        for (tarefa in listaDoBanco) {
            val viewDaTarefa = LayoutInflater.from(this).inflate(R.layout.item_tarefa, llListaTarefas, false)

            val tvNomeTarefa = viewDaTarefa.findViewById<TextView>(R.id.tvNomeTarefaItem)
            val cbTarefaConcluida = viewDaTarefa.findViewById<CheckBox>(R.id.cbTarefaConcluida)

            // Preenche os dados
            tvNomeTarefa.text = tarefa.nomeTarefa
            cbTarefaConcluida.isChecked = tarefa.isConcluida

            // Detalhe Profissional: Se o usuário marcar/desmarcar o checkbox, salva a alteração no banco!
            cbTarefaConcluida.setOnCheckedChangeListener { _, isChecked ->
                val tarefaAtualizada = tarefa.copy(isConcluida = isChecked)
                db.tarefaDao().atualizarTarefa(tarefaAtualizada)
            }

            llListaTarefas.addView(viewDaTarefa)
        }
    }
}