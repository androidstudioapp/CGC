package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.Combustivel;
import com.senac.controlecombustivel.model.Tipo;

/**
 * Created by Dantieris on 26/06/2015.
 */
public class CombustivelDAO extends BackupSQLiteHelper {

    private static final String TABELA_COMBUSTIVEIS = "COMBUSTIVEIS";

    private static final String CHAVE_ID = "ID";
    private static final String CHAVE_NOME = "NOME";

    public CombustivelDAO(Context context) {
        super(context);
    }

    public void inserirCombustivel(Combustivel combustivel) {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, combustivel.getId());
        valores.put(CHAVE_NOME, combustivel.getNome());

        banco.insert(TABELA_COMBUSTIVEIS, null, valores);

        banco.close();
    }

}