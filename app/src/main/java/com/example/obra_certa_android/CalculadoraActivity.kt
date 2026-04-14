package com.example.obra_certa_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalculadoraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculadora)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtVoltar = findViewById<android.widget.TextView>(R.id.tvVoltarCalculadora)
        txtVoltar.setOnClickListener {
            finish()
        }

        val etAltura = findViewById<EditText>(R.id.etAltura)
        val etLargura = findViewById<EditText>(R.id.etLargura)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        val llResultado = findViewById<LinearLayout>(R.id.llResultado)
        val tvAreaExata = findViewById<TextView>(R.id.tvAreaExata)
        val tvAreaComMargem = findViewById<TextView>(R.id.tvAreaComMargem)

        btnCalcular.setOnClickListener {
            val alturaDigitada = etAltura.text.toString()
            val larguraDigitada = etLargura.text.toString()

            if (alturaDigitada.isEmpty() || larguraDigitada.isEmpty()) {
                Toast.makeText(this, "Preencha a altura e a largura!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val altura = alturaDigitada.toDouble()
            val largura = larguraDigitada.toDouble()

            val areaExata = altura * largura

            val areaComMargem = areaExata * 1.10

            tvAreaExata.text = "Área Exata: ${String.format("%.2f", areaExata)} m²"
            tvAreaComMargem.text = "Com margem de 10%: ${String.format("%.2f", areaComMargem)} m²"

            llResultado.visibility = View.VISIBLE
        }
    }
}