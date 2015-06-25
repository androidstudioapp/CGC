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

    private static final String COMBUSTIVEL = "combustivel";
    private static final String POSTO = "posto";
    private static final String POSTOS = "postos";
    private static final String TIPO_COMBUSTIVEL = "tipoCombustivel";
    private static final String TIPO_COMBUSTIVEL_POR_POSTO = "tipoCombustivelPorPosto";
    private static final String ATUALIZAR_PRECO = "atualizarPreco";
    private static final String INSERIR_ABASTECIMENTO = "inserirAbastecimento";
    private static final String RELATORIO = "relatorio";
    private static final String SEPARADOR = "/";
    private static final String VALOR_TOTAL_ABASTECIDO = "valorTotalAbastecido";
    private static final String POSTO_MAIS_USADO = "postoMaisUsado";
    private static final String COMBUSTIVEL_MAIS_USADO = "combustivelMaisUsado";

    public WebService() {
    }

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

                idPosto = jsonObjectTiposCombustivel.getInt("ID_POSTO");
                int idTipo = jsonObjectTiposCombustivel.getInt("ID_TIPO");
                int idCombustivel = jsonObjectTiposCombustivel.getInt("ID_COMBUSTIVEL");

                @SuppressWarnings("Retirar a chamada recursiva do web service.")
                Posto posto = getPosto(idPosto);
                Tipo tipo = getTipo(idTipo);
                Combustivel combustivel = getCombustivel(idCombustivel);

                TiposCombustivel tiposCombustivel = new TiposCombustivel(id, posto, preco, tipo, combustivel);

                tiposCombustiveis.add(i, tiposCombustivel);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tiposCombustiveis;
    }

    public static TiposCombustivel getTiposCombustivel(int id) {
        TiposCombustivel tiposCombustivel = null;

        try {
            JSONObject jsonObjectTiposCombustivel = new WebServiceGET().execute(URL + TIPO_COMBUSTIVEL + SEPARADOR + id + SEPARADOR + CHAVE).get().getJSONArray(TIPO_COMBUSTIVEL).getJSONObject(0);

            id = jsonObjectTiposCombustivel.getInt("ID");
            double preco = jsonObjectTiposCombustivel.getDouble("PRECO");

            int idPosto = jsonObjectTiposCombustivel.getInt("ID_POSTO");
            int idTipo = jsonObjectTiposCombustivel.getInt("ID_TIPO");
            int idCombustivel = jsonObjectTiposCombustivel.getInt("ID_COMBUSTIVEL");

            @SuppressWarnings("Retirar a chamada recursiva do web service.")
            Posto posto = getPosto(idPosto);
            Tipo tipo = getTipo(idTipo);
            Combustivel combustivel = getCombustivel(idCombustivel);

            tiposCombustivel = new TiposCombustivel(id, posto, preco, tipo, combustivel);
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return tiposCombustivel;
    }

    public static void atualizarTipoaCombustivel(TiposCombustivel tiposCombustivel, String id_android) {
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
     * Acessa o web service, o método get combustivel que retorna um combustivel que esteja cadastrado no banco no formato json
     * o json é processado e retorna um objeto da classe Tipo.
     *
     * @return Um objeto Tipo.
     */
    public static Combustivel getCombustivel(int id) {
        Combustivel combustivel = null;

        JSONObject jsonObjectCombustivel = null;
        try {
            jsonObjectCombustivel = new WebServiceGET().execute(URL + COMBUSTIVEL + SEPARADOR + id).get().getJSONArray(COMBUSTIVEL).getJSONObject(0);

            id = jsonObjectCombustivel.getInt("ID");
            String nome = jsonObjectCombustivel.getString("NOME");

            combustivel = new Combustivel(id, nome);
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return combustivel;
    }

    @SuppressWarnings("Remover por baixa performance.")
    public static Tipo getTipo(int id) {
        Tipo tipo = null;

        JSONObject jsonObjectTipo = null;
        try {
            jsonObjectTipo = new WebServiceGET().execute(URL + "/tipo/" + id).get().getJSONArray("tipo").getJSONObject(0);

            id = jsonObjectTipo.getInt("ID");
            String nome = jsonObjectTipo.getString("NOME");

            tipo = new Tipo(id, nome);

            Log.d("TIPO", tipo.toString());
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return tipo;
    }

    /**
     * @param id
     * @return
     */
    public static Posto getPosto(int id) {
        Posto posto = null;

        try {
            JSONObject jsonObjectPosto = new WebServiceGET().execute(URL + POSTO + SEPARADOR + id + SEPARADOR + CHAVE).get().getJSONArray(POSTO).getJSONObject(0);

            id = jsonObjectPosto.getInt("ID");
            String endereco = jsonObjectPosto.getString("ENDERECO");
            String nome = jsonObjectPosto.getString("NOME");
            double latitude = jsonObjectPosto.getDouble("LATITUDE");
            double longitude = jsonObjectPosto.getDouble("LONGITUDE");

            int idBandeira = jsonObjectPosto.getInt("ID_BANDEIRA");
            String nomeBandeira = jsonObjectPosto.getString("BANDEIRA");

            posto = new Posto(id, endereco, nome, latitude, longitude, new Bandeira(idBandeira, nomeBandeira));

            Log.d("POSTO", posto.toString());
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return posto;
    }

    @SuppressWarnings("Remover por baixa performance.")
    public static Bandeira getBandeira(int id) {
        Bandeira bandeira = null;

        try {
            JSONObject jsonObjectBandeira;

            jsonObjectBandeira = new WebServiceGET().execute(URL + "/bandeira/" + id).get().getJSONArray("bandeira").getJSONObject(0);

            id = jsonObjectBandeira.getInt("ID");
            String nome = jsonObjectBandeira.getString("NOME");

            bandeira = new Bandeira(id, nome);

            Log.d("BANDEIRA", bandeira.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return bandeira;
    }

    /**
     * Acessa o web service, o método get postos que retorna todos abastecimentos de um android id cadastrados no banco no formato json
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

                int idTiposCombustivel = jsonAbastecimento.getInt("ID_TIPOS_COMBUSTIVEL");

                TiposCombustivel tiposCombustivel = getTiposCombustivel(idTiposCombustivel);

                abastecimentos.add(i, new Abastecimento(id, valorTotal, idAndroid, litros, tiposCombustivel, data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        relatorio = new Relatorio(abastecimentos, combustivelMaisUsado, postoMaisUsado, valorTotalAbastecido);

        return relatorio;
    }
}
