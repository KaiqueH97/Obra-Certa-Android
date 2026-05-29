package com.example.obra_certa_android.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tabela_tarefas",
    foreignKeys = [
        ForeignKey(
            entity = Projeto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("projetoId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeTarefa: String,
    val isConcluida: Boolean = false,
    val projetoId: Int
)