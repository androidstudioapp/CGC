package com.senac.controlecombustivel.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class BackupSQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO_DADOS = 1;
    private static final String BANCO_DADOS_NOME = "BACKUP";

    public BackupSQLiteHelper(Context context) {
        super(context, BANCO_DADOS_NOME, null, VERSAO_BANCO_DADOS);
    }

    @Override
    public void onCreate(SQLiteDatabase banco) {
        String CREATE_TABLE_POSTOS = "CREATE TABLE POSTOS ( ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                            " NOME TEXT, LATITUDE DOUBLE, " +
                                                            "LONGITUDE DOUBLE, " +
                                                            "ENDERECO TEXT, " +
                                                            "ID_BANDEIRA INTEGER)";

        String CREATE_TABLE_BANDEIRAS = "CREATE TABLE BANDEIRAS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                "NOME TEXT)";

        String CREATE_TABLE_TIPOS = "CREATE TABLE TIPOS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                         "NOME TEXT)";

        String CREATE_TABLE_COMBUSTIVEIS = "CREATE TABLE COMBUSTIVEIS (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                        "NOME TEXT)";

        banco.execSQL(CREATE_TABLE_POSTOS);
        banco.execSQL(CREATE_TABLE_BANDEIRAS);
        banco.execSQL(CREATE_TABLE_TIPOS);
        banco.execSQL(CREATE_TABLE_COMBUSTIVEIS);

        banco.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase banco, int antigaVersao, int novaVersao) {
        banco.execSQL("DROP TABLE IF EXISTS POSTOS");
        banco.execSQL("DROP TABLE IF EXISTS BANDEIRAS");
        banco.execSQL("DROP TABLE IF EXISTS TIPOS");
        banco.execSQL("DROP TABLE IF EXISTS COMBUSTIVEIS");

        this.onCreate(banco);
    }
}
