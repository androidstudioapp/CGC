package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.senac.controlecombustivel.model.Bandeira;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class BandeiraDAO extends BackupSQLiteHelper {

    private static final String TABELA_BANDEIRA = "BANDEIRAS";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_NOME = "NOME";

    public BandeiraDAO(Context context) {
        super(context);
    }

    public void inserirBandeira(Bandeira bandeira) {
        Log.d("INSERINDO BANDEIRAS", bandeira.toString());
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, bandeira.getId());
        valores.put(CHAVE_NOME, bandeira.getNome());

        banco.insert(TABELA_BANDEIRA, "", valores);

        banco.close();
    }

    public List<Bandeira> getBandeiras() {
        List<Bandeira> bandeiras = new ArrayList<>();

        SQLiteDatabase banco = this.getReadableDatabase();

        String query = "SELECT * FROM BANDEIRAS";

        Cursor cursor = banco.rawQuery(query, new String[]{});

        Bandeira bandeira = null;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                bandeira = new Bandeira(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("NOME")));

                bandeiras.add(bandeira);
            } while (cursor.moveToNext());
        }
        banco.close();

        Log.d("SELECIONANDO BANDEIRAS", bandeiras.toString());

        return bandeiras;
    }


}
