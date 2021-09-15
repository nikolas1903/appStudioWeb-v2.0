package com.example.studioweb.services.repository.models

/**
 * Modelo usado quando o usuário faz o Login. É passado para a função de Criação do Usuário no
 * Banco de Dados, e registra o usuário conforme os campos armazenados nessas variáveis
 */
data class UserLoginModelDB(
    var cpf: String,
    var senha: String,
    var responseNome: String,
    var responseCpf: String,
    var responseEmail: String,
    var responseTelefone: String,
    var responseNascimento: String,
    var isSync: Int = 0
)


/**
 * Modelo usado quando o usuário edita o perfil. É passado para a função de Update do Usuário no
 * Banco de Dados, e edita o usuário conforme os campos armazenados nessas variáveis
 */
data class UserUpdateModel(
    var CPF: String,
    var Email: String,
    var Nascimento: String,
    var Nome: String,
    var Senha: String,
    var Telefone: String,
)


