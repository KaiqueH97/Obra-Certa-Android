package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MaterialDao {
    @Insert
    fun inserirMaterial(material: Material)

    @Update
    fun atualizarMaterial(material: Material)

    @Delete
    fun deletarMaterial(material: Material)

    @Query("SELECT * FROM tabela_materiais WHERE projetoId = :idDoProjeto")
    fun buscarMateriaisPorProjeto(idDoProjeto: Int): List<Material>

    @Query("SELECT SUM(precoTotal) FROM tabela_materiais WHERE projetoId = :idDoProjeto")
    fun somarCustoTotalDoProjeto(idDoProjeto: Int): Double?
}