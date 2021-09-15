package com.example.studioweb.services.repository.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.studioweb.constants.DatabaseConstants
import com.example.studioweb.services.repository.models.UserLoginModelDB
import com.example.studioweb.services.repository.models.UserUpdateModel

class UserRepositoryDB private constructor(context: Context) {
    private var mUserDatabaseHelper: UserDatabaseHelper = UserDatabaseHelper(context)

    /**
     * Singleton
     */
    companion object {
        private lateinit var repository: UserRepositoryDB
        fun getInstance(context: Context): UserRepositoryDB {
            if (!::repository.isInitialized) {
                repository = UserRepositoryDB(context)
            }
            return repository
        }
    }


    /**
     * Salva os dados do usuário no SQLite, recebidos pela API, ao fazer Login.
     */
    fun register(user: UserLoginModelDB): Boolean {
        return try {
            val db = mUserDatabaseHelper.writableDatabase

            val contentValues = ContentValues()
            contentValues.put(DatabaseConstants.USER.COLUMNS.CPF, user.cpf)
            contentValues.put(DatabaseConstants.USER.COLUMNS.SENHA, user.senha)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_NOME, user.responseNome)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_CPF, user.responseCpf)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL, user.responseEmail)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE, user.responseTelefone)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO, user.responseNascimento)
            contentValues.put(DatabaseConstants.USER.COLUMNS.IS_SYNC, user.isSync)

            db.insert(DatabaseConstants.USER.TABLE_NAME, null, contentValues)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Procura se existe algum usuário previamente salvo no banco.
     */
    @SuppressLint("Range")
    fun searchUser(cpf: String, senha: String): UserLoginModelDB? {
        var user: UserLoginModelDB? = null
        return try {
            val db = mUserDatabaseHelper.readableDatabase

            val projection = arrayOf(
                DatabaseConstants.USER.COLUMNS.CPF,
                DatabaseConstants.USER.COLUMNS.SENHA,
                DatabaseConstants.USER.COLUMNS.RESPONSE_NOME,
                DatabaseConstants.USER.COLUMNS.RESPONSE_CPF,
                DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL,
                DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE,
                DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO,
                DatabaseConstants.USER.COLUMNS.IS_SYNC
            )

            val selection = DatabaseConstants.USER.COLUMNS.CPF + " = ?"
            val args = arrayOf(cpf)

            val cursor = db.query(
                DatabaseConstants.USER.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()

                val cpfBanco =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.CPF))
                val senhaBanco =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.SENHA))
                val responseNome =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_NOME))
                val responseCpf =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_CPF))
                val responseEmail =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL))
                val responseTelefone =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE))
                val responseNascimento =
                    cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO))
                val isSync =
                    cursor.getInt(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.IS_SYNC))

                if (cpf == cpfBanco && senha == senhaBanco) {
                    user = UserLoginModelDB(
                        cpfBanco,
                        senhaBanco,
                        responseNome,
                        responseCpf,
                        responseEmail,
                        responseTelefone,
                        responseNascimento,
                        isSync)
                }
            }

            cursor?.close()
            user
        } catch (e: Exception) {
            user
        }
    }


    /**
     * Procura todos os usuários que ainda não foram sincronizados com a API.
     */
    @SuppressLint("Range")
    fun getUnsyncUsers() : List<UserUpdateModel> {
        val list : MutableList<UserUpdateModel> = ArrayList()
        return try {
            val db = mUserDatabaseHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM logged_users WHERE is_sync = 0", null)

            if(cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()){
                    val cpf = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.CPF))
                    val senha = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.SENHA))
                    val responseNome = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_NOME))
                    val responseEmail = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL))
                    val responseTelefone = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE))
                    val responseNascimento = cursor.getString(cursor.getColumnIndex(DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO))

                    val user = UserUpdateModel(cpf, responseEmail, responseNascimento, responseNome, senha, responseTelefone)
                    list.add(user)
                }
            }
            cursor?.close()
            list
        } catch (e: Exception) {
            list
        }
    }


    /**
     * Atualiza um usuário no banco.
     */
    fun updateUser(user: UserLoginModelDB): Boolean {
        return try {
            val db = mUserDatabaseHelper.writableDatabase

            val userCpf = user.cpf
            val cpf = userCpf.replace(".", "").replace("-", "") //Substituindo os caractéres invalidos do CPF
            val contentValues = ContentValues()
            contentValues.put(DatabaseConstants.USER.COLUMNS.CPF, cpf)
            contentValues.put(DatabaseConstants.USER.COLUMNS.SENHA, user.senha)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_NOME, user.responseNome)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_CPF, user.responseCpf)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL, user.responseEmail)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE, user.responseTelefone)
            contentValues.put(DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO, user.responseNascimento)

            val selection = DatabaseConstants.USER.COLUMNS.CPF + " = ?"
            val args = arrayOf(cpf)

            db.update(DatabaseConstants.USER.TABLE_NAME, contentValues, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Atualiza um usuário sync para unSync, e unSync para sync.
     */
    fun updateUnsyncUser(userCpf: String, isSync: Int): Boolean {
        return try {
            val db = mUserDatabaseHelper.writableDatabase

            val cpf = userCpf.replace(".", "")
                .replace("-", "") //Substituindo os caractéres invalidos do CPF
            val contentValues = ContentValues()
            contentValues.put(DatabaseConstants.USER.COLUMNS.CPF, cpf)
            contentValues.put(DatabaseConstants.USER.COLUMNS.IS_SYNC, isSync)


            val selection = DatabaseConstants.USER.COLUMNS.CPF + " = ?"
            val args = arrayOf(cpf)

            db.update(DatabaseConstants.USER.TABLE_NAME, contentValues, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }

}