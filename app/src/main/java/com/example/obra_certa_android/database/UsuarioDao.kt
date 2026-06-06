package com.example.obra_certa_android.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    // Para a tela de Cadastro
    @Insert
    fun inserirUsuario(usuario: Usuario)

    // Para a tela de Login (busca se existe alguém com esse email e senha)
    @Query("SELECT * FROM tabela_usuarios WHERE email = :emailDigitado AND senha = :senhaDigitada LIMIT 1")
    fun autenticarUsuario(emailDigitado: String, senhaDigitada: String): Usuario?

    // Opcional: Para evitar cadastrar dois emails iguais
    @Query("SELECT * FROM tabela_usuarios WHERE email = :emailDigitado LIMIT 1")
    fun verificarEmailExistente(emailDigitado: String): Usuario?
}