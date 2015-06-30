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

        String query = "INSERT INTO TIPOS (ID, NOME) VALUES (?,?)";

        banco.rawQuery(query, new String[] {String.valueOf(tipo.getId()), tipo.getNome()});

        banco.close();
    }

}