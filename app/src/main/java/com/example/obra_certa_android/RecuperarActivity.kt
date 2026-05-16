package com.example.obra_certa_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvVoltarLogin = findViewById<TextView>(R.id.tvVoltarLogin)
        tvVoltarLogin.setOnClickListener {
            finish()
        }

        val btnEnviarLink = findViewById<Button>(R.id.btnEnviarLink)
        val etEmailRecuperar = findViewById<EditText>(R.id.etEmailRecuperar)

        btnEnviarLink.setOnClickListener {
            val email = etEmailRecuperar.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, digite seu e-mail.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Link de recuperação enviado para $email", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}