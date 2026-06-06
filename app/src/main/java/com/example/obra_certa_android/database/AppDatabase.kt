package com.example.obra_certa_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// ATENÇÃO: Adicionamos o Material::class e subimos a version para 3
@Database(entities = [Projeto::class, Tarefa::class, Material::class, Usuario::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projetoDao(): ProjetoDao
    abstract fun tarefaDao(): TarefaDao
    abstract fun materialDao(): MaterialDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "obra_certa_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // Vai apagar o banco v2 e recriar como v3
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}