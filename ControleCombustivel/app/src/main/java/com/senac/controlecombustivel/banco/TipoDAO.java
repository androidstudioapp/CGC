package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.Tipo;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class TipoDAO extends BackupSQLiteHelper {

    private static final String TABELA_TIPO = "TIPOS";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_NOME = "NOME";

    public TipoDAO(Context context) {
        super(context);
    }

    public void inserirTipo(Tipo tipo) {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, tipo.getId());
        valores.put(CHAVE_NOME, tipo.getNome());

        banco.insert(TABELA_TIPO, null, valores);

        banco.close();
    }

}