package com.example.obra_certa_android

import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TarefasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarefas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtVoltar = findViewById<android.widget.TextView>(R.id.tvVoltarTarefas)
        txtVoltar.setOnClickListener {
            finish()
        }

        val etNovaTarefa = findViewById<EditText>(R.id.etNovaTarefa)
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionarTarefa)

        btnAdicionar.setOnClickListener {
            val nomeDaTarefa = etNovaTarefa.text.toString()

            if (nomeDaTarefa.isEmpty()) {
                Toast.makeText(this, "Digite o nome da tarefa antes de adicionar!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Tarefa '$nomeDaTarefa' salva com sucesso!", Toast.LENGTH_SHORT).show()
                etNovaTarefa.text.clear()
            }
        }

        val cbTarefa1 = findViewById<CheckBox>(R.id.cbTarefa1)
        val tvTarefa1 = findViewById<TextView>(R.id.tvTarefa1)

        cbTarefa1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvTarefa1.paintFlags = tvTarefa1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTarefa1.paintFlags = tvTarefa1.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
        val cbTarefa2 = findViewById<CheckBox>(R.id.cbTarefa2)
        val tvTarefa2 = findViewById<TextView>(R.id.tvTarefa2)

        cbTarefa2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tvTarefa2.paintFlags = tvTarefa2.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTarefa2.paintFlags = tvTarefa2.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}