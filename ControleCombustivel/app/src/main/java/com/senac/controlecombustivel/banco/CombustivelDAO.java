package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

        String query = "INSERT INTO COMBUSTIVEIS (ID, NOME) VALUES (?,?)";

        banco.rawQuery(query, new String[] {String.valueOf(combustivel.getId()), combustivel.getNome()});

        banco.close();
    }

    public void deletarCombustiveis() {
        SQLiteDatabase banco = this.getWritableDatabase();

        banco.delete(TABELA_COMBUSTIVEIS, "", new String[]{});

        Log.d("DELETANDO", "Deletando COMBUSTIVEIS.");

        banco.close();
    }

}