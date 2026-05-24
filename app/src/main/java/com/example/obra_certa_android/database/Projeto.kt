package com.example.obra_certa_android.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_projetos")
data class Projeto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeCliente: String
)