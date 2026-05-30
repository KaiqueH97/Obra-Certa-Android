package com.example.obra_certa_android

import android.app.AlertDialog
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
import com.example.obra_certa_android.database.Material
import com.example.obra_certa_android.database.Tarefa
import java.text.NumberFormat
import java.util.Locale

class DetalhesProjetoActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var projetoId: Int = -1

    // Variáveis Visuais
    private lateinit var llListaTarefas: LinearLayout
    private lateinit var llListaMateriais: LinearLayout
    private lateinit var tvCustoTotalProjeto: TextView

    // Formatador para deixar os números com cara de Dinheiro (R$)
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

        // --- LÓGICA DAS ABAS ---
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

        // --- MAPEAMENTO TAREFAS ---
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

        // --- MAPEAMENTO MATERIAIS / ORÇAMENTO ---
        llListaMateriais = findViewById(R.id.llListaMateriais)
        tvCustoTotalProjeto = findViewById(R.id.tvCustoTotalProjeto)
        val btnNovoCalculo = findViewById<Button>(R.id.btnNovoCalculo)

        btnNovoCalculo.setOnClickListener {
            mostrarDialogNovoMaterial()
        }

        // Carrega as listas assim que abrir a tela
        atualizarListaTarefas()
        atualizarListaMateriais()
    }

    // --- FUNÇÕES DE TAREFA ---
    private fun atualizarListaTarefas() {
        llListaTarefas.removeAllViews()
        if (projetoId == -1) return

        val lista = db.tarefaDao().buscarTarefasPorProjeto(projetoId)
        for (tarefa in lista) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_tarefa, llListaTarefas, false)

            view.findViewById<TextView>(R.id.tvNomeTarefaItem).text = tarefa.nomeTarefa
            val cbTarefa = view.findViewById<CheckBox>(R.id.cbTarefaConcluida)
            cbTarefa.isChecked = tarefa.isConcluida

            cbTarefa.setOnCheckedChangeListener { _, isChecked ->
                db.tarefaDao().atualizarTarefa(tarefa.copy(isConcluida = isChecked))
            }
            llListaTarefas.addView(view)
        }
    }

    // --- FUNÇÕES DE MATERIAL / ORÇAMENTO ---
    private fun mostrarDialogNovoMaterial() {
        // Usa o visual que criamos no Passo 1
        val viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_novo_material, null)

        val etNome = viewDialog.findViewById<EditText>(R.id.etNomeMaterialDialog)
        val etQtd = viewDialog.findViewById<EditText>(R.id.etQuantidadeDialog)
        val etPreco = viewDialog.findViewById<EditText>(R.id.etPrecoDialog)

        AlertDialog.Builder(this)
            .setView(viewDialog)
            .setPositiveButton("Salvar") { _, _ ->
                val nome = etNome.text.toString()
                val qtd = etQtd.text.toString()
                val precoString = etPreco.text.toString()

                if (nome.isNotBlank() && precoString.isNotBlank() && projetoId != -1) {
                    // Tenta converter o texto digitado num número decimal
                    val precoDecimal = precoString.toDoubleOrNull() ?: 0.0

                    val novoMaterial = Material(
                        nomeMaterial = nome,
                        quantidadeInfo = qtd,
                        precoTotal = precoDecimal,
                        projetoId = projetoId
                    )

                    db.materialDao().inserirMaterial(novoMaterial)
                    atualizarListaMateriais() // Atualiza tudo na tela
                } else {
                    Toast.makeText(this, "Preencha o nome e o preço!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun atualizarListaMateriais() {
        llListaMateriais.removeAllViews()
        if (projetoId == -1) return

        // 1. Puxa e desenha os materiais
        val lista = db.materialDao().buscarMateriaisPorProjeto(projetoId)
        for (material in lista) {
            val view = LayoutInflater.from(this).inflate(R.layout.item_material, llListaMateriais, false)

            view.findViewById<TextView>(R.id.tvNomeMaterialItem).text = material.nomeMaterial
            view.findViewById<TextView>(R.id.tvQuantidadeMaterialItem).text = material.quantidadeInfo

            // Coloca o R$ no preço
            view.findViewById<TextView>(R.id.tvPrecoMaterialItem).text = formatadorMoeda.format(material.precoTotal)

            // Ação de Excluir
            view.findViewById<TextView>(R.id.btnDeletarMaterial).setOnClickListener {
                db.materialDao().deletarMaterial(material)
                atualizarListaMateriais() // Recarrega a tela apagando o item
            }

            llListaMateriais.addView(view)
        }

        // 2. Atualiza o Card Verde com o Custo Total
        // O banco de dados faz a soma automaticamente com a query que criamos
        val somaTotal = db.materialDao().somarCustoTotalDoProjeto(projetoId) ?: 0.0
        tvCustoTotalProjeto.text = formatadorMoeda.format(somaTotal)
    }
}