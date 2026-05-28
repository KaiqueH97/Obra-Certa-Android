package com.example.obra_certa_android

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetalhesProjetoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalhes_projeto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvVoltar = findViewById<TextView>(R.id.tvVoltarDetalhes)
        tvVoltar.setOnClickListener { finish() }

        val tvNomeProjetoDetalhe = findViewById<TextView>(R.id.tvNomeProjetoDetalhe)
        val projetoId = intent.getIntExtra("PROJETO_ID", -1)
        val projetoNome = intent.getStringExtra("PROJETO_NOME") ?: "Projeto Desconhecido"
        tvNomeProjetoDetalhe.text = projetoNome

        val btnAbaOrcamento = findViewById<LinearLayout>(R.id.btnAbaOrcamento)
        val btnAbaTarefas = findViewById<LinearLayout>(R.id.btnAbaTarefas)
        val tvTextoOrcamento = findViewById<TextView>(R.id.tvTextoOrcamento)
        val tvTextoTarefas = findViewById<TextView>(R.id.tvTextoTarefas)

        val llAbaTarefas = findViewById<LinearLayout>(R.id.llAbaTarefas)
        val llAbaOrcamento = findViewById<LinearLayout>(R.id.llAbaOrcamento)

        val corLaranja = Color.parseColor("#FF6D00") // Substitua pela sua cor primária
        val corCinzaTexto = Color.parseColor("#64748B")

        btnAbaTarefas.setOnClickListener {
            llAbaTarefas.visibility = View.VISIBLE
            llAbaOrcamento.visibility = View.GONE

            btnAbaTarefas.setBackgroundResource(R.drawable.bg_aba_ativa)
            tvTextoTarefas.setTextColor(corLaranja)

            btnAbaOrcamento.setBackgroundResource(R.drawable.bg_aba_inativa)
            tvTextoOrcamento.setTextColor(corCinzaTexto)
        }

        btnAbaOrcamento.setOnClickListener {
            llAbaOrcamento.visibility = View.VISIBLE
            llAbaTarefas.visibility = View.GONE

            btnAbaOrcamento.setBackgroundResource(R.drawable.bg_aba_ativa)
            tvTextoOrcamento.setTextColor(corLaranja)

            btnAbaTarefas.setBackgroundResource(R.drawable.bg_aba_inativa)
            tvTextoTarefas.setTextColor(corCinzaTexto)
        }
    }
}