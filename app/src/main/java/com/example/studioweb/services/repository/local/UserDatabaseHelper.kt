package com.example.studioweb.services.repository.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studioweb.constants.DatabaseConstants

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Função de criação do banco de dados. É executada toda vez que o aplicativo é instalado.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USER)
    }


    /**
     * Função de update do banco de dados. É executada toda vez que muda a versão. (ainda não implementada)
     */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }


    /**
     * Objeto que contém o nome do banco, e que tem a Query de criação dele, executado pela função onCreate.
     */
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "StudioWeb.db"
        private const val CREATE_TABLE_USER =
            ("create table " + DatabaseConstants.USER.TABLE_NAME + " ("
                    + DatabaseConstants.USER.COLUMNS.CPF + " text unique, "
                    + DatabaseConstants.USER.COLUMNS.SENHA + " text, "
                    + DatabaseConstants.USER.COLUMNS.RESPONSE_NOME + " text, "
                    + DatabaseConstants.USER.COLUMNS.RESPONSE_CPF + " text, "
                    + DatabaseConstants.USER.COLUMNS.RESPONSE_EMAIL + " text, "
                    + DatabaseConstants.USER.COLUMNS.RESPONSE_TELEFONE + " text, "
                    + DatabaseConstants.USER.COLUMNS.RESPONSE_NASCIMENTO + " text, "
                    + DatabaseConstants.USER.COLUMNS.IS_SYNC + " text);")
    }
}