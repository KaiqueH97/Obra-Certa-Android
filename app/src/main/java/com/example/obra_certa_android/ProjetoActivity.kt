package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProjetoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_projeto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtVoltar = findViewById<TextView>(R.id.tvVoltarProjetos)
        txtVoltar.setOnClickListener {
            finish()
        }

        val cardGabriel = findViewById<CardView>(R.id.cardProjetoGabriel)
        val cardKaique = findViewById<CardView>(R.id.cardProjetoKaique)

        cardGabriel.setOnClickListener {
            val intent = Intent(this, DetalhesProjetoActivity::class.java)
            startActivity(intent)
        }

        cardKaique.setOnClickListener {
            val intent = Intent(this, DetalhesProjetoActivity::class.java)
            startActivity(intent)
        }
    }
}