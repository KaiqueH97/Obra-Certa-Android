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

            tvNomeMaterialResult.text = "Material: $materialEscolhido"
            tvAreaComMargem.text = "${String.format("%.2f", areaFinal)} $unidade"

            llResultado.visibility = View.VISIBLE
        }
    }
}