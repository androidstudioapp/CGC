package com.senac.controlecombustivel.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.senac.controlecombustivel.model.TiposCombustivel;

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
}
