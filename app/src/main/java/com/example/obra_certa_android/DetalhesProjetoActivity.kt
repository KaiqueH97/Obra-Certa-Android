package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obra_certa_android.database.AppDatabase
import com.example.obra_certa_android.database.Tarefa
import java.text.NumberFormat
import java.util.Locale
import com.example.obra_certa_android.database.Material

class DetalhesProjetoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var projetoId: Int = -1

    private lateinit var llListaTarefas: LinearLayout
    private lateinit var llListaMateriais: LinearLayout
    private lateinit var tvCustoTotalProjeto: TextView

    private val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_projeto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getDatabase(this)
        projetoId = intent.getIntExtra("PROJETO_ID", -1)
        val projetoNome = intent.getStringExtra("PROJETO_NOME") ?: "Projeto Desconhecido"

        findViewById<TextView>(R.id.tvNomeProjetoDetalhe).text = projetoNome
        findViewById<TextView>(R.id.tvVoltarDetalhes).setOnClickListener { finish() }

        val btnAbaOrcamento = findViewById<LinearLayout>(R.id.btnAbaOrcamento)
        val btnAbaTarefas = findViewById<LinearLayout>(R.id.btnAbaTarefas)
        val llAbaOrcamento = findViewById<LinearLayout>(R.id.llAbaOrcamento)
        val llAbaTarefas = findViewById<LinearLayout>(R.id.llAbaTarefas)

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

        llListaTarefas = findViewById(R.id.llListaTarefas)
        val etNovaTarefa = findViewById<EditText>(R.id.etNovaTarefa)
        val btnAdicionarTarefa = findViewById<Button>(R.id.btnAdicionarTarefa)

        btnAdicionarTarefa.setOnClickListener {
            val texto = etNovaTarefa.text.toString()
            if (texto.isNotBlank() && projetoId != -1) {
                db.tarefaDao().inserirTarefa(Tarefa(nomeTarefa = texto, projetoId = projetoId))
                etNovaTarefa.text.clear()
                atualizarListaTarefas()
            }
        }

        llListaMateriais = findViewById(R.id.llListaMateriais)
        tvCustoTotalProjeto = findViewById(R.id.tvCustoTotalProjeto)

        val btnNovoCalculo = findViewById<Button>(R.id.btnNovoCalculo)
        btnNovoCalculo.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            intent.putExtra("PROJETO_ID", projetoId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza as listas automaticamente ao retornar da Calculadora
        atualizarListaTarefas()
        atualizarListaMateriais()
    }
    private fun atualizarListaTarefas() {
        llListaTarefas.removeAllViews()
        if (projetoId == -1) return

        val lista = db.tarefaDao().buscarTarefasPorProjeto(projetoId)

        for (tarefa in lista) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_tarefa, llListaTarefas, false)

            val tvNomeTarefa = view.findViewById<TextView>(R.id.tvNomeTarefaItem)
            val cbTarefa = view.findViewById<CheckBox>(R.id.cbTarefaConcluida)
            val cardTarefa = view.findViewById<androidx.cardview.widget.CardView>(R.id.cardTarefaItem)

            tvNomeTarefa.text = tarefa.nomeTarefa
            cbTarefa.isChecked = tarefa.isConcluida

            fun atualizarVisualTarefa(concluida: Boolean) {
                if (concluida) {
                    // Risca o texto, deixa o texto verde escuro e o fundo verde claro
                    tvNomeTarefa.paintFlags = tvNomeTarefa.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    tvNomeTarefa.setTextColor(android.graphics.Color.parseColor("#528B6B"))
                    cardTarefa.setCardBackgroundColor(android.graphics.Color.parseColor("#F0FDF4"))
                } else {
                    // Tira o risco, volta o texto para preto e o fundo para branco
                    tvNomeTarefa.paintFlags = tvNomeTarefa.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvNomeTarefa.setTextColor(android.graphics.Color.parseColor("#1A202C"))
                    cardTarefa.setCardBackgroundColor(android.graphics.Color.WHITE)
                }
            }

            atualizarVisualTarefa(tarefa.isConcluida)

            cbTarefa.setOnCheckedChangeListener { _, isChecked ->
                db.tarefaDao().atualizarTarefa(tarefa.copy(isConcluida = isChecked))
                atualizarVisualTarefa(isChecked) // Atualiza a cor na hora!
            }

            view.setOnLongClickListener {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Excluir Tarefa")
                    .setMessage("Deseja apagar a tarefa '${tarefa.nomeTarefa}'?")
                    .setPositiveButton("Sim") { _, _ ->
                        db.tarefaDao().deletarTarefa(tarefa)
                        atualizarListaTarefas() // Recarrega a lista sem a tarefa
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
                true
            }

            llListaTarefas.addView(view)
        }
    }


    private fun atualizarListaMateriais() {
        llListaMateriais.removeAllViews()
        if (projetoId == -1) return

        val lista = db.materialDao().buscarMateriaisPorProjeto(projetoId)
        for (material in lista) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_material, llListaMateriais, false)

            view.findViewById<TextView>(R.id.tvNomeMaterialItem).text = material.nomeMaterial
            view.findViewById<TextView>(R.id.tvQuantidadeMaterialItem).text = material.quantidadeInfo
            view.findViewById<TextView>(R.id.tvPrecoMaterialItem).text = formatadorMoeda.format(material.precoTotal)

            val btnEditar = view.findViewById<TextView>(R.id.btnEditarMaterial)
            val btnExcluir = view.findViewById<TextView>(R.id.btnDeletarMaterial)

            btnExcluir.setOnClickListener {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Excluir Material")
                    .setMessage("Deseja remover '${material.nomeMaterial}' do orçamento?")
                    .setPositiveButton("Sim") { _, _ ->
                        db.materialDao().deletarMaterial(material)
                        atualizarListaMateriais()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            btnEditar.setOnClickListener {
                val inputPreco = EditText(this)
                inputPreco.setText(material.precoTotal.toString())
                inputPreco.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                inputPreco.setPadding(50, 50, 50, 50)

                android.app.AlertDialog.Builder(this)
                    .setTitle("Editar Preço Total")
                    .setView(inputPreco)
                    .setPositiveButton("Salvar") { _, _ ->
                        val novoPreco = inputPreco.text.toString().toDoubleOrNull() ?: material.precoTotal
                        val materialAtualizado = material.copy(precoTotal = novoPreco)
                        db.materialDao().atualizarMaterial(materialAtualizado)
                        atualizarListaMateriais()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            llListaMateriais.addView(view)
        }
        val somaTotal = db.materialDao().somarCustoTotalDoProjeto(projetoId) ?: 0.0
        tvCustoTotalProjeto.text = formatadorMoeda.format(somaTotal)
    }
}