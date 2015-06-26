package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.Bandeira;
import com.senac.controlecombustivel.model.Posto;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class PostoDAO extends BackupSQLiteHelper {

    private static final String TABELA_POSTOS = "POSTOS";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_NOME = "NOME";
    private static final String CHAVE_ENDERECO = "ENDERECO";
    private static final String CHAVE_LATITUDE = "LATITUDE";
    private static final String CHAVE_LONGITUDE = "LONGITUDE";

    private static final String CHAVE_ID_BANDEIRA = "ID_BANDEIRA";
    private static final String CHAVE_BANDEIRA_NOME = "BANDEIRA"; // Atributo BANDEIRAS.nome do banco, no select volta como BANDEIRA

    public PostoDAO(Context context) {
        super(context);
    }

    public void inserirPosto(Posto posto) {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, posto.getId());
        valores.put(CHAVE_NOME, posto.getNome());
        valores.put(CHAVE_ENDERECO, posto.getEndereco());
        valores.put(CHAVE_ID_BANDEIRA, posto.getBandeira().getId());
        valores.put(CHAVE_LATITUDE, posto.getLatitude());
        valores.put(CHAVE_LONGITUDE, posto.getLongitude());

        banco.insert(TABELA_POSTOS, null, valores);

        banco.close();
    }

    public Posto getPosto(int id) {
        String query = "SELECT POSTOS.*,BANDEIRAS.NOME AS BANDEIRA FROM POSTOS" +
                        "INNER JOIN BANDEIRAS" +
                        "ON BANDEIRAS.ID = POSTOS.ID_BANDEIRA" +
                        "WHERE POSTOS.ID = ?";

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null) {
            cursor.moveToFirst();
        }

        cursor.close();
        banco.close();

        return new Posto(
                cursor.getInt(cursor.getColumnIndex(CHAVE_ID)),
                cursor.getString(cursor.getColumnIndex(CHAVE_ENDERECO)),
                cursor.getString(cursor.getColumnIndex(CHAVE_NOME)),
                cursor.getDouble(cursor.getColumnIndex(CHAVE_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(CHAVE_LONGITUDE)),
                new Bandeira(cursor.getInt(cursor.getColumnIndex(CHAVE_ID_BANDEIRA)),
                        cursor.getString(cursor.getColumnIndex(CHAVE_BANDEIRA_NOME)))
        );
    }

    public List<Posto> getPostos() {
        List<Posto> postos = new LinkedList<>();

        String query = "SELECT * FROM " + TABELA_POSTOS;

        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(query, null);

        Posto posto = null;
        if (cursor != null) {
            do {
                posto = new Posto(
                        cursor.getInt(cursor.getColumnIndex(CHAVE_ID)),
                        cursor.getString(cursor.getColumnIndex(CHAVE_ENDERECO)),
                        cursor.getString(cursor.getColumnIndex(CHAVE_NOME)),
                        cursor.getDouble(cursor.getColumnIndex(CHAVE_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(CHAVE_LONGITUDE)),
                        new Bandeira(cursor.getInt(cursor.getColumnIndex(CHAVE_ID_BANDEIRA)),
                                cursor.getString(cursor.getColumnIndex(CHAVE_BANDEIRA_NOME)))
                );

                postos.add(posto);
            } while (cursor.moveToNext());
        }

        banco.close();
        cursor.close();

        return postos;
    }

}
