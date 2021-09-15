package com.example.studioweb.constants

class DatabaseConstants private constructor() {

    /**
     * Constantes do Banco de Dados
     */
    object USER {
        const val TABLE_NAME = "logged_users"
        object COLUMNS {
            const val CPF = "cpf"
            const val SENHA = "senha"
            const val RESPONSE_NOME = "response_nome"
            const val RESPONSE_CPF = "response_cpf"
            const val RESPONSE_EMAIL = "response_email"
            const val RESPONSE_TELEFONE = "response_telefone"
            const val RESPONSE_NASCIMENTO = "response_nascimento"
            const val IS_SYNC = "is_sync"
        }
    }
}