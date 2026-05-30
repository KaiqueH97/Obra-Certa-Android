package com.example.obra_certa_android.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tabela_materiais",
    foreignKeys = [
        ForeignKey(
            entity = Projeto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("projetoId"),
            onDelete = ForeignKey.CASCADE // Apaga os materiais se a obra for deletada
        )
    ]
)
data class Material(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeMaterial: String,       // Ex: "Cerâmica"
    val quantidadeInfo: String,     // Ex: "27,50 m²"
    val precoTotal: Double,         // Ex: 1500.50 (Usamos Double para dinheiro/matemática)
    val projetoId: Int              // A ligação com a Obra!
)