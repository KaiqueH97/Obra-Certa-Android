package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface MaterialDao {
    @Insert
    fun inserirMaterial(material: Material)

    @Delete
    fun deletarMaterial(material: Material)

    @Query("SELECT * FROM tabela_materiais WHERE projetoId = :idDoProjeto")
    fun buscarMateriaisPorProjeto(idDoProjeto: Int): List<Material>

    // O pulo do gato financeiro: o SQLite soma todos os preços desta obra para nós!
    @Query("SELECT SUM(precoTotal) FROM tabela_materiais WHERE projetoId = :idDoProjeto")
    fun somarCustoTotalDoProjeto(idDoProjeto: Int): Double?
}