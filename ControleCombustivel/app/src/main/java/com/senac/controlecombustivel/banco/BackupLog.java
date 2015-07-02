package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BackupLog extends BackupSQLiteHelper {

    private static final String CHAVE_DATA = "DATA";
    private static final String TABELA_BACKUP_LOG = "BACKUP_LOG";

    public BackupLog(Context context) {
        super(context);
    }

    public void inserirBackupLog(Calendar data) {
        Log.d("INSERINDO BACKUP LOG", data.toString());
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_DATA, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(data.getTime()));

        banco.insertOrThrow(TABELA_BACKUP_LOG, null, valores);

        banco.close();
    }

    public Calendar getUltimaData() {
        String query = "SELECT " + CHAVE_DATA + " FROM " + TABELA_BACKUP_LOG + " ORDER BY " + CHAVE_DATA + " DESC LIMIT 0,1";

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery(query, new String[] {});

        if (cursor != null && cursor.moveToFirst()) {
            String dataString = cursor.getString(0);

            Calendar data = converterStringParaData(dataString);

            banco.close();
            cursor.close();

            Log.d("SELECIONANDO BACKUP LOG", data.toString());

            return data;
        } else {
            Log.d("SELECIONANDO BACKUP LOG", "nulo");
            banco.close();
            return null;
        }
    }

    public void deletarBackupLog() {
        Log.d("DELETANDO BACKUP LOG", "DELETANDO TUDO");
        SQLiteDatabase banco = this.getWritableDatabase();

        String query = "DELETE FROM " + TABELA_BACKUP_LOG;

        banco.execSQL(query);

        banco.close();
    }

    private Calendar converterStringParaData(String dataString) {
        String dataArray [] = dataString.split("-");

        int ano = Integer.parseInt(dataArray[0]);
        int mes = Integer.parseInt(dataArray[1]);
        int dia = Integer.parseInt(dataArray[2]);

        Calendar c = Calendar.getInstance();

        c.set(ano, mes - 1, dia);
        return c;
    }

}
