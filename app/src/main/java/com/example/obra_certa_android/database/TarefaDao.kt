package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TarefaDao {
    @Insert
    fun inserirTarefa(tarefa: Tarefa)

    @Update
    fun atualizarTarefa(tarefa: Tarefa)

    @Delete
    fun deletarTarefa(tarefa: Tarefa)

    @Query("SELECT * FROM tabela_tarefas WHERE projetoId = :idDoProjeto")
    fun buscarTarefasPorProjeto(idDoProjeto: Int): List<Tarefa>
}