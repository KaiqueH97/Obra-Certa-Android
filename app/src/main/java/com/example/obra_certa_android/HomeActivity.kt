package com.example.obra_certa_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnMeusProjetos = findViewById<Button>(R.id.btnIrMeusProjetos)
        val btnCalculadora = findViewById<Button>(R.id.btnIrCalculadora)
        val btnMeuPerfil = findViewById<Button>(R.id.btnIrMeuPerfil)

        btnMeusProjetos.setOnClickListener {
            val intent = Intent(this, ProjetoActivity::class.java)
            startActivity(intent)
        }

        btnCalculadora.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }

        btnMeuPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}