package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.senac.controlecombustivel.model.Bandeira;
import com.senac.controlecombustivel.model.Posto;

import java.util.Arrays;
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

        String query = "INSERT INTO POSTOS (ID, NOME, ENDERECO, ID_BANDEIRA, LATITUDE, LONGITUDE) " +
                        "VALUES (?,?,?,?,?,?)";

        banco.rawQuery(query, new String[] {String.valueOf(posto.getId()),
                                            posto.getNome(),
                                            posto.getEndereco(),
                                            String.valueOf(posto.getBandeira().getId()),
                                            String.valueOf(posto.getLatitude()),
                                            String.valueOf(posto.getLongitude())});

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

        Posto posto = new Posto(
                cursor.getInt(cursor.getColumnIndex(CHAVE_ID)),
                cursor.getString(cursor.getColumnIndex(CHAVE_ENDERECO)),
                cursor.getString(cursor.getColumnIndex(CHAVE_NOME)),
                cursor.getDouble(cursor.getColumnIndex(CHAVE_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(CHAVE_LONGITUDE)),
                new Bandeira(cursor.getInt(cursor.getColumnIndex(CHAVE_ID_BANDEIRA)),
                        cursor.getString(cursor.getColumnIndex(CHAVE_BANDEIRA_NOME)))
        );

        banco.close();

        return posto;
    }

    public List<Posto> getPostos() {
        List<Posto> postos = new LinkedList<>();

        String query = "SELECT POSTOS.*,BANDEIRAS.NOME AS BANDEIRA FROM POSTOS\n" +
                        "INNER JOIN BANDEIRAS\n" +
                        "ON BANDEIRAS.ID = POSTOS.ID_BANDEIRA";

        SQLiteDatabase banco = this.getWritableDatabase();
        Cursor cursor = banco.rawQuery(query, null);

        Posto posto = null;

        if (cursor != null && cursor.moveToFirst()) {
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

        return postos;
    }

}
