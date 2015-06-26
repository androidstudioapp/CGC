package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.Bandeira;

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
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put(CHAVE_ID, bandeira.getId());
        valores.put(CHAVE_NOME, bandeira.getNome());

        banco.insert(TABELA_BANDEIRA, null, valores);

        banco.close();
    }


}
