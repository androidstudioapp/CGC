package com.senac.controlecombustivel.webservice;

import android.util.Log;

import com.senac.controlecombustivel.model.Abastecimento;
import com.senac.controlecombustivel.model.Bandeira;
import com.senac.controlecombustivel.model.Combustivel;
import com.senac.controlecombustivel.model.Posto;
import com.senac.controlecombustivel.model.Relatorio;
import com.senac.controlecombustivel.model.Tipo;
import com.senac.controlecombustivel.model.TiposCombustivel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WebService {
    private static final String URL = "http://controledecombustivel.16mb.com/WS/";

    private static final String CHAVE = "WSd55069b2050e1559c7846b75b6544104";

    private static final String POSTOS = "postos";
    private static final String TIPO_COMBUSTIVEL = "tipoCombustivel";
    private static final String TIPO_COMBUSTIVEL_POR_POSTO = "tipoCombustivelPorPosto";
    private static final String ATUALIZAR_PRECO = "atualizarPreco";
    private static final String INSERIR_ABASTECIMENTO = "inserirAbastecimento";
    private static final String RELATORIO = "relatorio";
    private static final String VALOR_TOTAL_ABASTECIDO = "valorTotalAbastecido";
    private static final String POSTO_MAIS_USADO = "postoMaisUsado";
    private static final String COMBUSTIVEL_MAIS_USADO = "combustivelMaisUsado";

    private static final String SEPARADOR = "/";

    private static final String BACKUP_BANDEIRAS = "backupBandeiras";
    private static final String BACKUP_TIPOS = "backupTipos";
    private static final String BACKUP_COMBUSTIVEIS = "backupCombustiveis";
    private static final String BACKUP_POSTOS = "backupPostos";

    /**
     * Acessa o web service, o método get postos que retorna todos postos cadastrados no banco no formato json
     * o json é processado e getPostos() retorna uma lista com os objetos postos.
     *
     * @return
     */
    public static List<Posto> getPostos() {
        List<Posto> postos = new ArrayList<>();

        JSONObject jsonObjectPostos = null;
        try {
            jsonObjectPostos = new WebServiceGET().execute(URL + POSTOS + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayPostos = null;
        try {
            jsonArrayPostos = jsonObjectPostos.getJSONArray(POSTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayPostos.length(); i++) {
            try {
                JSONObject jsonPosto = jsonArrayPostos.getJSONObject(i);

                int id = jsonPosto.getInt("ID");
                String endereco = jsonPosto.getString("ENDERECO");
                String nome = jsonPosto.getString("NOME");
                double latitude = jsonPosto.getDouble("LATITUDE");
                double longitude = jsonPosto.getDouble("LONGITUDE");

                int idBandeira = jsonPosto.getInt("ID_BANDEIRA");
                String nomeBandeira = jsonPosto.getString("BANDEIRA");

                postos.add(i, new Posto(id, endereco, nome, latitude, longitude, new Bandeira(idBandeira, nomeBandeira)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return postos;
    }

    public static List<TiposCombustivel> getCombustiveisPorPosto(int idPosto) {
        List<TiposCombustivel> tiposCombustiveis = new ArrayList<>();

        JSONObject jsonObjecTiposCombustivel = null;
        try {
            jsonObjecTiposCombustivel = new WebServiceGET().execute(URL + TIPO_COMBUSTIVEL_POR_POSTO + SEPARADOR + idPosto + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JSONArray jsonArrayTiposCombustivel = null;
        try {
            jsonArrayTiposCombustivel = jsonObjecTiposCombustivel.getJSONArray(TIPO_COMBUSTIVEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayTiposCombustivel.length(); i++) {
            try {

                JSONObject jsonObjectTiposCombustivel = jsonArrayTiposCombustivel.getJSONObject(i);

                int id = jsonObjectTiposCombustivel.getInt("ID");
                double preco = jsonObjectTiposCombustivel.getDouble("PRECO");

                String nomeTipo = jsonObjectTiposCombustivel.getString("TIPO");
                String nomeCombustivel = jsonObjectTiposCombustivel.getString("COMBUSTIVEL");

                // Os objetos Tipo e Combustivel estão com id zerado, para melhorar a performance
                // e nao buscar atributos que nao serao utilizados
                TiposCombustivel tiposCombustivel = new TiposCombustivel(id, null, preco,
                                                                        new Tipo(0, nomeTipo),
                                                                        new Combustivel(0, nomeCombustivel));

                tiposCombustiveis.add(i, tiposCombustivel);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tiposCombustiveis;
    }

    public static void atualizarTiposCombustivel(TiposCombustivel tiposCombustivel, String id_android) {
        String jsonObjectString = "{\"ID\":\"" + tiposCombustivel.getId() + "\"," +
                "\"PRECO\":\"" + tiposCombustivel.getPreco() + "\"," +
                "\"ID_ANDROID\":\"" + id_android + "\"}";

        Log.d("WS ATUALIZAR", jsonObjectString);

        new WebServicePOST().execute(URL + ATUALIZAR_PRECO + SEPARADOR + CHAVE, jsonObjectString);
    }

    public static void inserirAbastecimento(Abastecimento abastecimento) {
        String jsonObjectString = "{\"" + TIPO_COMBUSTIVEL + "\":{\"id\":\"" + abastecimento.getTiposCombustivel().getId() + "\"}," +
                "\"valor_total\":\"" + abastecimento.getValorTotal() + "\"," +
                "\"litros\":\"" + abastecimento.getLitros() + "\"," +
                "\"data\":\"" + new SimpleDateFormat("yyyy-MM-dd").format(abastecimento.getData()) + "\"," +
                "\"id_android\":\"" + abastecimento.getIdAndroid() + "\"}";

        new WebServicePOST().execute(URL + INSERIR_ABASTECIMENTO + SEPARADOR + CHAVE, jsonObjectString);
    }
    /**
     * Acessa o web service, o método get postos que retorna todos abastecimentos de um android
     * id cadastrados no banco no formato json
     * o json é processado e getRelatorioAbastecimentos() retorna uma lista com os objetos abastecimento.
     *
     * @return
     */
    public static Relatorio getRelatorioAbastecimentos(String idAndroid) {
        Relatorio relatorio = null;

        List<Abastecimento> abastecimentos = new ArrayList<>();

        JSONObject jsonObjectRelatorio = null;
        try {
            jsonObjectRelatorio = new WebServiceGET().execute(URL + RELATORIO + SEPARADOR + idAndroid + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonRelatorio = null;

        JSONObject jsonObjectValorTotalAbastecido = null;
        JSONObject jsonObjectPostoMaisUsado = null;
        JSONObject jsonObjectCombustivelMaisUsado = null;
        try {
            jsonRelatorio = jsonObjectRelatorio.getJSONObject(RELATORIO);

            jsonObjectValorTotalAbastecido = jsonRelatorio.getJSONObject(VALOR_TOTAL_ABASTECIDO);
            jsonObjectPostoMaisUsado = jsonRelatorio.getJSONObject(POSTO_MAIS_USADO);
            jsonObjectCombustivelMaisUsado = jsonRelatorio.getJSONObject(COMBUSTIVEL_MAIS_USADO);

            jsonRelatorio.remove(VALOR_TOTAL_ABASTECIDO);
            jsonRelatorio.remove(POSTO_MAIS_USADO);
            jsonRelatorio.remove(COMBUSTIVEL_MAIS_USADO);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        double valorTotalAbastecido = 0;
        String postoMaisUsado = "";
        String combustivelMaisUsado = "";

        try {
            valorTotalAbastecido = jsonObjectValorTotalAbastecido.getDouble("VALOR_TOTAL_ABASTECIDO");
            postoMaisUsado = jsonObjectPostoMaisUsado.getString("NOME");
            combustivelMaisUsado = jsonObjectCombustivelMaisUsado.getString("NOME");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonRelatorio.length(); i++) {
            try {
                JSONObject jsonAbastecimento = jsonRelatorio.getJSONObject(String.valueOf(i));

                int id = jsonAbastecimento.getInt("ID");
                double valorTotal = jsonAbastecimento.getDouble("VALOR_TOTAL");
                double litros = jsonAbastecimento.getDouble("LITROS");

                int mes = jsonAbastecimento.getInt("MES");
                int ano = jsonAbastecimento.getInt("ANO");
                int dia = jsonAbastecimento.getInt("DIA");

                Calendar c = Calendar.getInstance();
                c.set(ano, mes, dia);
                Date data = c.getTime();

                String nomeCombustivel = jsonAbastecimento.getString("COMBUSTIVEL");
                String nomeTipo = jsonAbastecimento.getString("TIPO");

                String nomePosto = jsonAbastecimento.getString("POSTO");

                // Os objetos Posto, Tipo e Combustivel estão com os atributos nulos e zerados,
                // para melhorar a performance e nao recuperar do web service, dados que nao serao utilizados
                abastecimentos.add(i, new Abastecimento(id, valorTotal, idAndroid, litros,
                        new TiposCombustivel(0,
                                new Posto(0, null, nomePosto, 0, 0, null), 0,
                                new Tipo(0, nomeTipo),
                                new Combustivel(0, nomeCombustivel)), data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        relatorio = new Relatorio(abastecimentos, combustivelMaisUsado, postoMaisUsado, valorTotalAbastecido);

        return relatorio;
    }

    public static List<Posto> getBackupPostos() {
        List<Posto> backupPostos = new ArrayList<>();

        JSONObject jsonObjectPostos = null;
        try {
            jsonObjectPostos = new WebServiceGET().execute(URL + BACKUP_POSTOS + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayPostos = null;
        try {
            jsonArrayPostos = jsonObjectPostos.getJSONArray(BACKUP_POSTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayPostos.length(); i++) {
            try {
                JSONObject jsonPosto = jsonArrayPostos.getJSONObject(i);

                int id = jsonPosto.getInt("ID");
                String endereco = jsonPosto.getString("ENDERECO");
                String nome = jsonPosto.getString("NOME");
                double latitude = jsonPosto.getDouble("LATITUDE");
                double longitude = jsonPosto.getDouble("LONGITUDE");
                int idBandeira = jsonPosto.getInt("ID_BANDEIRA");

                backupPostos.add(i, new Posto(id, endereco, nome, latitude, longitude, new Bandeira(idBandeira, null)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return backupPostos;
    }

    public static List<Bandeira> getBackupBandeiras() {
        List<Bandeira> backupBandeiras = new ArrayList<>();

        JSONObject jsonObjectBandeiras = null;
        try {
            jsonObjectBandeiras = new WebServiceGET().execute(URL + BACKUP_BANDEIRAS + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayBandeiras = null;
        try {
            jsonArrayBandeiras = jsonObjectBandeiras.getJSONArray(BACKUP_BANDEIRAS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayBandeiras.length(); i++) {
            try {
                JSONObject jsonBandeira = jsonArrayBandeiras.getJSONObject(i);

                int id = jsonBandeira.getInt("ID");
                String nome = jsonBandeira.getString("NOME");

                backupBandeiras.add(new Bandeira(id, nome));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return backupBandeiras;
    }

    public static List<Tipo> getBackupTipos() {
        List<Tipo> backupTipos = new ArrayList<>();

        JSONObject jsonObjectTipos = null;
        try {
            jsonObjectTipos = new WebServiceGET().execute(URL + BACKUP_TIPOS + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayTipos = null;
        try {
            jsonArrayTipos = jsonObjectTipos.getJSONArray(BACKUP_TIPOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayTipos.length(); i++) {
            try {
                JSONObject jsonTipo = jsonArrayTipos.getJSONObject(i);

                int id = jsonTipo.getInt("ID");
                String nome = jsonTipo.getString("NOME");

                backupTipos.add(new Tipo(id, nome));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return backupTipos;
    }

    public static List<Combustivel> getBackupCombustiveis() {
        List<Combustivel> backupCombustiveis = new ArrayList<>();

        JSONObject jsonObjectCombustiveis = null;
        try {
            jsonObjectCombustiveis = new WebServiceGET().execute(URL + BACKUP_COMBUSTIVEIS + SEPARADOR + CHAVE).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayCombustiveis = null;
        try {
            jsonArrayCombustiveis = jsonObjectCombustiveis.getJSONArray(BACKUP_COMBUSTIVEIS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < jsonArrayCombustiveis.length(); i++) {
            try {
                JSONObject jsonCombustivel = jsonArrayCombustiveis.getJSONObject(i);

                int id = jsonCombustivel.getInt("ID");
                String nome = jsonCombustivel.getString("NOME");

                backupCombustiveis.add(new Combustivel(id, nome));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return backupCombustiveis;
    }
}