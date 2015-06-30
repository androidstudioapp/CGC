package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dantieris on 29/06/2015.
 */
public class BackupLog extends BackupSQLiteHelper {

    private static final String CHAVE_DATA = "DATA";
    private static final String TABELA_BACKUP_LOG = "BACKUP_LOG";

    public BackupLog(Context context) {
        super(context);
    }

    public void inserirBackupLog(Date data) {
        Log.d("INSERINDO BACKUP LOG", data.toString());
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_DATA, new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(data));

        banco.insertOrThrow(TABELA_BACKUP_LOG, null, valores);

        banco.close();
    }

    public Date getUltimaData() {
        String query = "SELECT " + CHAVE_DATA + " FROM " + TABELA_BACKUP_LOG + " ORDER BY " + CHAVE_DATA + " DESC LIMIT 0,1";

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery(query, new String[] {});

        if (cursor != null && cursor.moveToFirst()) {
            String dataString = cursor.getString(0);

            Date data = converterStringParaData(dataString);

            banco.close();
            cursor.close();

            Log.d("SELECIONANDO BACKUP LOG", data.toString());

            return data;
        } else {
            Log.d("SELECIONANDO BACKUP LOG", "nulo");
            banco.close();
            cursor.close();
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

    private Date converterStringParaData(String dataString) {
        String dataArray [] = dataString.split("-");

        int ano = Integer.parseInt(dataArray[0]);
        int dia = Integer.parseInt(dataArray[1]);
        int mes = Integer.parseInt(dataArray[2]);
        int hora = Integer.parseInt(dataArray[3]);
        int minuto = Integer.parseInt(dataArray[4]);
        int segundo = Integer.parseInt(dataArray[5]);

        Calendar c = Calendar.getInstance();

        c.set(ano, mes, dia, hora, minuto, segundo);

        return c.getTime();
    }

}
