package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProjetoDao {
    @Insert
    fun inserirProjeto(projeto: Projeto)

    @Update
    fun atualizarProjeto(projeto: Projeto)

    @Delete
    fun deletarProjeto(projeto: Projeto)

    @Query("SELECT * FROM tabela_projetos")
    fun buscarTodosProjetos(): List<Projeto>
}