package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackupSQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO_DADOS = 1;
    private static final String BANCO_DADOS_NOME = "BACKUP";

    private static final String TABELA_BACKUP_LOG = "BACKUP_LOG";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_DATA = "DATA";

    public BackupSQLiteHelper(Context context) {
        super(context, BANCO_DADOS_NOME, null, VERSAO_BANCO_DADOS);
    }

    @Override
    public void onCreate(SQLiteDatabase banco) {
        String CREATE_TABLE_BACKUP_LOG = "CREATE TABLE BACKUP_LOG (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                "DATA TEXT)"; // Trabalhar o atributo DATA com o formato  YYYY-MM-DD

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

        String CREATE_TABLE_TIPOS_COMBUSTIVEL = "CREATE TABLE TIPOS_COMBUSTIVEL (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                                "PRECO DOUBLE," +
                                                                                "ID_POSTO INTEGER," +
                                                                                "ID_TIPO INTEGER," +
                                                                                "ID_COMBUSTIVEL)";

        banco.execSQL(CREATE_TABLE_BACKUP_LOG);
        banco.execSQL(CREATE_TABLE_POSTOS);
        banco.execSQL(CREATE_TABLE_BANDEIRAS);
        banco.execSQL(CREATE_TABLE_TIPOS);
        banco.execSQL(CREATE_TABLE_COMBUSTIVEIS);
        banco.execSQL(CREATE_TABLE_TIPOS_COMBUSTIVEL);

        banco.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase banco, int antigaVersao, int novaVersao) {
        banco.execSQL("DROP TABLE IF EXISTS BACKUP_LOG");
        banco.execSQL("DROP TABLE IF EXISTS POSTOS");
        banco.execSQL("DROP TABLE IF EXISTS BANDEIRAS");
        banco.execSQL("DROP TABLE IF EXISTS TIPOS");
        banco.execSQL("DROP TABLE IF EXISTS COMBUSTIVEIS");
        banco.execSQL("DROP TABLE IF EXISTS TIPOS_COMBUSTIVEL");

        this.onCreate(banco);
    }

    public void inserirBackupLog(Date data) {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_DATA, new SimpleDateFormat("yyyy-MM-dd").format(data));

        banco.insert(TABELA_BACKUP_LOG, null, valores);

        banco.close();
    }

    public Date getUltimaData() {
        String query = "SELECT " + CHAVE_DATA + " FROM " +TABELA_BACKUP_LOG + "" +
                        "ORDER BY " + CHAVE_DATA + " DESC" +
                        "LIMIT, 0, 1";

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        banco.close();

        String dataArray[] = cursor.getString(0).split("-");

        int ano = Integer.parseInt(dataArray[0]);
        int mes = Integer.parseInt(dataArray[1]);
        int dia = Integer.parseInt(dataArray[2]);

        Calendar c = Calendar.getInstance();

        c.set(ano, mes, dia);

        return c.getTime();
    }

    public void deletarBackupLog() {
        SQLiteDatabase banco = this.getWritableDatabase();

        banco.delete(TABELA_BACKUP_LOG, "1=1", null);

        Log.d("INFO BANCO", "Deletando a tabela de log.");

        banco.close();
    }
}
