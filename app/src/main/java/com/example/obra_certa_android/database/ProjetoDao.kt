package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProjetoDao {
    @Insert
    fun inserirProjeto(projeto: Projeto)

    @Query("SELECT * FROM tabela_projetos")
    fun buscarTodosProjetos(): List<Projeto>
}