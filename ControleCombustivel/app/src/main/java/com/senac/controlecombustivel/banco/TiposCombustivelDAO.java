package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.Combustivel;
import com.senac.controlecombustivel.model.Tipo;
import com.senac.controlecombustivel.model.TiposCombustivel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class TiposCombustivelDAO extends BackupSQLiteHelper {

    private static final String TABELA_TIPOS_COMBUSTIVEL = "TIPOS_COMBUSTIVEL";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_PRECO = "PRECO";

    private static final String CHAVE_ID_TIPO = "ID_TIPO";
    private static final String CHAVE_TIPO = "TIPO"; // Atributo TIPOS.nome do banco, no select volta como TIPO

    private static final String CHAVE_ID_COMBUSTIVEL = "ID_COMBUSTIVEL";
    private static final String CHAVE_COMBUSTIVEL = "COMBUSTIVEL"; // Atributo COMBUSTIVEIS.nome do banco, no select volta como COMBUSTIVEL

    private static final String CHAVE_ID_POSTO = "ID_POSTO";
    private static final String CHAVE_POSTO = "POSTO"; // Atributo POSTOS.nome do banco, no select volta como POSTO

    public TiposCombustivelDAO(Context context) {
        super(context);
    }

    public void inserirTiposCombustivel(TiposCombustivel tiposCombustivel) {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, tiposCombustivel.getId());
        valores.put(CHAVE_PRECO, tiposCombustivel.getPreco());
        valores.put(CHAVE_ID_TIPO, tiposCombustivel.getTipo().getId());
        valores.put(CHAVE_ID_COMBUSTIVEL, tiposCombustivel.getCombustivel().getId());
        valores.put(CHAVE_ID_POSTO, tiposCombustivel.getPosto().getId());

        banco.insert(TABELA_TIPOS_COMBUSTIVEL, null, valores);

        banco.close();
    }

    public List<TiposCombustivel> getTiposCombustivelPorPosto(int idPosto) {
        List<TiposCombustivel> tiposCombustivels = new LinkedList<>();

        String query = "SELECT TIPOS_COMBUSTIVEL.ID, TIPOS.NOME AS TIPO, COMBUSTIVEIS.NOME AS COMBUSTIVEL, PRECO FROM TIPOS_COMBUSTIVEL" +
                        "INNER JOIN TIPOS" +
                        "ON TIPOS.ID = TIPOS_COMBUSTIVEL.ID_TIPO" +
                        "INNER JOIN COMBUSTIVEIS" +
                        "ON COMBUSTIVEIS.ID = TIPOS_COMBUSTIVEL.ID_COMBUSTIVEL" +
                        "WHERE TIPOS_COMBUSTIVEL.ID_POSTO = ?";

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idPosto)});

        TiposCombustivel tiposCombustivel = null;
        if (cursor != null) {
            do {
                tiposCombustivel = new TiposCombustivel(
                        cursor.getInt(cursor.getColumnIndex(CHAVE_ID)),
                        null,
                        cursor.getDouble(cursor.getColumnIndex(CHAVE_PRECO)),
                        new Tipo(0, cursor.getString(cursor.getColumnIndex(CHAVE_TIPO))),
                        new Combustivel(0, cursor.getString(cursor.getColumnIndex(CHAVE_COMBUSTIVEL)))
                );

                tiposCombustivels.add(tiposCombustivel);
            } while (cursor.moveToNext());
        }

        return tiposCombustivels;
    }
}
