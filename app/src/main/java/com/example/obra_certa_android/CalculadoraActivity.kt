package com.example.obra_certa_android

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.obra_certa_android.database.AppDatabase // Import do banco adicionado!

class CalculadoraActivity : AppCompatActivity() {
    private val opcoesMateriais = mapOf(
        "Piso" to listOf("Porcelanato", "Cerâmica", "Laminado", "Cimentício", "Vinílico", "Emborrachado", "Pedras"),
        "Parede" to listOf("Bloco cerâmico", "Drywall", "Bloco de concreto", "Tijolo ecológico", "Tijolinho maciço"),
        "Revestimento" to listOf("Textura tradicional", "Textura Projetada", "Monocapa", "Massa Corrida", "Cerâmica/Azulejo"),
        "Reboco" to listOf("Tradicional (Cimento e Areia)", "Projetado", "Monocapa", "Gesso"),
        "Contrapiso" to listOf("Cimento", "Argamassa niveladora", "Concreto Usinado"),
        "Laje" to listOf("Concreto armado", "Lajota cerâmica", "Pré-moldada (treliçada)", "EPS (Isopor)"),
        "Forro" to listOf("Gesso acartonado (Drywall)", "Gesso em placas", "PVC", "Madeira", "Metálico"),
        "Telhado" to listOf("Telha Cerâmica (Barro)", "Telha de Fibrocimento", "Telha Metálica", "Shingle"),
        "Pintura" to listOf("Tinta Acrílica", "Tinta Látex (PVA)", "Tinta Epóxi", "Esmalte Sintético")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculadora)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtVoltar = findViewById<TextView>(R.id.tvVoltarCalculadora)
        txtVoltar.setOnClickListener { finish() }

        val spSuperficie = findViewById<Spinner>(R.id.spSuperficie)
        val spMaterial = findViewById<Spinner>(R.id.spMaterial)
        val etAltura = findViewById<EditText>(R.id.etAltura)
        val etLargura = findViewById<EditText>(R.id.etLargura)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        val llResultado = findViewById<LinearLayout>(R.id.llResultado)
        val tvNomeMaterialResult = findViewById<TextView>(R.id.tvNomeMaterialResult)
        val tvAreaComMargem = findViewById<TextView>(R.id.tvAreaComMargem)

        val listaSuperficies = mutableListOf("Selecione a superfície")
        listaSuperficies.addAll(opcoesMateriais.keys)

        val adapterSuperficie = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaSuperficies)
        spSuperficie.adapter = adapterSuperficie

        val tvAreaExata = findViewById<TextView>(R.id.tvAreaExata)
        val spProjetosVinculo = findViewById<Spinner>(R.id.spProjetosVinculo)
        val btnSalvarMaterial = findViewById<Button>(R.id.btnSalvarMaterial)

        // --- MUDANÇA AQUI: INTEGRAÇÃO COM O BANCO DE DADOS ---
        val db = AppDatabase.getDatabase(this)
        val projetoIdRecebido = intent.getIntExtra("PROJETO_ID", -1)

        // Busca a lista real de projetos do banco
        val projetosDoBanco = db.projetoDao().buscarTodosProjetos()

        // Extrai apenas os nomes para mostrar na tela
        val listaProjetos = mutableListOf("Selecione um projeto...")
        listaProjetos.addAll(projetosDoBanco.map { it.nomeCliente })

        val adapterProjetos = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaProjetos)
        spProjetosVinculo.adapter = adapterProjetos

        // Pré-seleciona a obra automaticamente se o usuário veio da tela de detalhes
        if (projetoIdRecebido != -1) {
            val posicaoNaLista = projetosDoBanco.indexOfFirst { it.id == projetoIdRecebido }
            if (posicaoNaLista != -1) {
                // +1 porque a primeira opção é o "Selecione um projeto..."
                spProjetosVinculo.setSelection(posicaoNaLista + 1)
            }
        }
        // --------------------------------------------------------

        spSuperficie.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val superficieSelecionada = listaSuperficies[position]
                val listaMateriais = mutableListOf<String>()

                if (superficieSelecionada == "Selecione a superfície") {
                    listaMateriais.add("Escolha a superfície primeiro")
                } else {
                    listaMateriais.add("Selecione o material")
                    opcoesMateriais[superficieSelecionada]?.let { listaMateriais.addAll(it) }
                }

                val adapterMaterial = ArrayAdapter(this@CalculadoraActivity, android.R.layout.simple_spinner_dropdown_item, listaMateriais)
                spMaterial.adapter = adapterMaterial
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnCalcular.setOnClickListener {
            val alturaText = etAltura.text.toString()
            val larguraText = etLargura.text.toString()
            val superficieEscolhida = spSuperficie.selectedItem.toString()
            val materialEscolhido = spMaterial.selectedItem.toString()

            if (alturaText.isEmpty() || larguraText.isEmpty() ||
                superficieEscolhida == "Selecione a superfície" ||
                materialEscolhido == "Selecione o material" || materialEscolhido == "Escolha a superfície primeiro") {
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- AÇÃO DE SALVAR O MATERIAL NA OBRA ---
            btnSalvarMaterial.setOnClickListener {
                val posicaoSelecionada = spProjetosVinculo.selectedItemPosition

                // A posição 0 é o texto "Selecione um projeto..."
                if (posicaoSelecionada == 0) {
                    Toast.makeText(this, "Por favor, selecione uma obra para vincular!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Pega o projeto correto da lista (subtrai 1 por causa do "Selecione...")
                val projetoSelecionado = projetosDoBanco[posicaoSelecionada - 1]

                val nomeDoMaterial = spMaterial.selectedItem.toString()
                val quantidadeCalculada = tvAreaComMargem.text.toString()

                // Prepara a entidade do banco. Preço inicial vai como 0.0 para ser editado depois.
                val novoMaterial = com.example.obra_certa_android.database.Material(
                    nomeMaterial = nomeDoMaterial,
                    quantidadeInfo = quantidadeCalculada,
                    precoTotal = 0.0,
                    projetoId = projetoSelecionado.id
                )

                // Salva no banco de dados!
                db.materialDao().inserirMaterial(novoMaterial)

                Toast.makeText(this, "Material salvo com sucesso!", Toast.LENGTH_SHORT).show()

                // Fecha a calculadora. Ao fechar, o onResume() da tela de Detalhes vai recarregar a lista automaticamente.
                finish()
            }

            val areaExata = alturaText.toDouble() * larguraText.toDouble()
            var areaFinal = areaExata
            var unidade = "m²"

            when (superficieEscolhida) {
                "Piso", "Contrapiso", "Laje", "Telhado" -> {
                    areaFinal = areaExata * 1.10
                    unidade = "m² (c/ 10% de quebra)"
                }
                "Parede", "Reboco", "Revestimento" -> {
                    areaFinal = areaExata
                    unidade = "m²"
                }
                "Forro" -> {
                    areaFinal = areaExata
                    unidade = "m² de forro"
                }
                "Pintura" -> {
                    areaFinal = areaExata
                    unidade = "m² (Consultar lata)"
                }
            }

            val formatAreaFinal = String.format(java.util.Locale("pt", "BR"), "%.2f", areaFinal)
            val formatAreaExata = String.format(java.util.Locale("pt", "BR"), "%.2f", areaExata)

            tvNomeMaterialResult.text = "Material: $materialEscolhido"
            tvAreaComMargem.text = "$formatAreaFinal $unidade"
            tvAreaExata.text = "Área total s/ quebra: $formatAreaExata m²"

            llResultado.visibility = View.VISIBLE
        }
    }
}