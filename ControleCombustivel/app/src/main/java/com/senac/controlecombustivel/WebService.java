package com.senac.controlecombustivel;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dantieris on 26/04/2015.
 */
public class WebService {

    private static final String URL = "http://controledecombustivel.16mb.com/WS";

    public WebService() {

    }

    /**
     * Acessa o web service, o método get postos que retorna todos postos cadastrados no banco no formato json
     * o json é processado e getPostos() retorna uma lista com os objetos postos.
     * @return
     */
    public static List<Posto> getPostos() {
        List<Posto> postos = new ArrayList<Posto>();

        JSONObject jsonObjectPostos = null;
        try {
            jsonObjectPostos = new BuscarJSONObject().execute(URL + "/postos").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        JSONArray jsonArrayPostos = null;
        try {
            jsonArrayPostos = jsonObjectPostos.getJSONArray("postos");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0 ; i < jsonArrayPostos.length() ; i++) {
            try {
                JSONObject jsonPosto = jsonArrayPostos.getJSONObject(i);

                int id = jsonPosto.getInt("ID");
                String endereco = jsonPosto.getString("ENDERECO");
                String nome = jsonPosto.getString("NOME");
                double latitude = jsonPosto.getDouble("LATITUDE");
                double longitude = jsonPosto.getDouble("LONGITUDE");

                int idBandeira = jsonPosto.getInt("ID_BANDEIRA");

                Bandeira bandeira = getBandeira(idBandeira);

                postos.add(i, new Posto(id, endereco, nome, latitude, longitude, bandeira));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return postos;
    }

    /**
     *
     * @param idPosto
     * @return
     */
    public static List<TiposCombustivel> getCombustiveisPorPosto(int idPosto) {
        List<TiposCombustivel> tiposCombustiveis = new ArrayList<TiposCombustivel>();

        JSONObject jsonObjecTiposCombustivel = null;
        try {
            jsonObjecTiposCombustivel = new BuscarJSONObject().execute(URL + "/tipoCombustivelPorPosto/" + idPosto).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONArray jsonArrayTiposCombustivel = null;
        try {
            jsonArrayTiposCombustivel = jsonObjecTiposCombustivel.getJSONArray("tipoCombustivel");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0 ; i < jsonArrayTiposCombustivel.length() ; i++) {
            try {

                JSONObject jsonObjectTiposCombustivel = jsonArrayTiposCombustivel.getJSONObject(i);

                int id = jsonObjectTiposCombustivel.getInt("ID");
                double preco = jsonObjectTiposCombustivel.getDouble("PRECO");

                idPosto = jsonObjectTiposCombustivel.getInt("ID_POSTO");
                int idTipo = jsonObjectTiposCombustivel.getInt("ID_TIPO");
                int idCombustivel = jsonObjectTiposCombustivel.getInt("ID_COMBUSTIVEL");

                Posto posto = getPosto(idPosto);
                Tipo tipo = getTipo(idTipo);
                Combustivel combustivel = getCombustivel(idCombustivel);

                Log.d("Teste WebService ", posto.toString());
                Log.d("Teste WebService ", tipo.toString());
                Log.d("Teste WebService ", combustivel.toString());

                TiposCombustivel tiposCombustivel = new TiposCombustivel(id, posto, preco, tipo, combustivel);

                tiposCombustiveis.add(i, tiposCombustivel);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return tiposCombustiveis;
    }

    /**
     *
     * @param id
     * @return
     */
    public static TiposCombustivel getTiposCombustivel(int id) {
        TiposCombustivel tiposCombustivel = null;

        try {
            JSONObject jsonObjectTiposCombustivel = new BuscarJSONObject().execute(URL + "/tipoCombustivel/" + id).get().getJSONArray("tipoCombustivel").getJSONObject(0);

            id = jsonObjectTiposCombustivel.getInt("ID");
            double preco = jsonObjectTiposCombustivel.getDouble("PRECO");

            int idPosto = jsonObjectTiposCombustivel.getInt("ID_POSTO");
            int idTipo = jsonObjectTiposCombustivel.getInt("ID_TIPO");
            int idCombustivel = jsonObjectTiposCombustivel.getInt("ID_COMBUSTIVEL");

            Posto posto = getPosto(idPosto);
            Tipo tipo = getTipo(idTipo);
            Combustivel combustivel = getCombustivel(idCombustivel);

            tiposCombustivel = new TiposCombustivel(id, posto, preco, tipo, combustivel);

            Log.d("TIPOS COMBUSTIVEL", tiposCombustivel.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return tiposCombustivel;
    }

    /**
     * Acessa o web service, o método get combustivel que retorna um combustivel que esteja cadastrado no banco no formato json
     * o json é processado e retorna um objeto da classe Tipo.
     * @return Um objeto Tipo.
     */
    public static Combustivel getCombustivel(int id) {
        Combustivel combustivel = null;

        JSONObject jsonObjectCombustivel = null;
        try {
            jsonObjectCombustivel = new BuscarJSONObject().execute(URL + "/combustivel/" + id).get().getJSONArray("combustivel").getJSONObject(0);

            id = jsonObjectCombustivel.getInt("ID");
            String nome = jsonObjectCombustivel.getString("NOME");

            combustivel = new Combustivel(id, nome);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return combustivel;
    }

    /**
     * Acessa o web service, o método get tipo que retorna um tipo que esteja cadastrado no banco no formato json
     * o json é processado e retorna um objeto da classe Tipo.
     * @return Um objeto Tipo.
     */
    public static Tipo getTipo(int id) {
        Tipo tipo = null;

        JSONObject jsonObjectTipo = null;
        try {
            jsonObjectTipo = new BuscarJSONObject().execute(URL + "/tipo/" + id).get().getJSONArray("tipo").getJSONObject(0);

            id = jsonObjectTipo.getInt("ID");
            String nome = jsonObjectTipo.getString("NOME");

            tipo = new Tipo(id, nome);

            Log.d("TIPO", tipo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return tipo;
    }

    /**
     *
     * @param id
     * @return
     */
    public static Posto getPosto(int id) {
        Posto posto = null;

        try {
            JSONObject jsonObjectPosto = new BuscarJSONObject().execute(URL + "/posto/" + id).get().getJSONArray("posto").getJSONObject(0);

            id = jsonObjectPosto.getInt("ID");
            String endereco = jsonObjectPosto.getString("ENDERECO");
            String nome = jsonObjectPosto.getString("NOME");
            double latitude = jsonObjectPosto.getDouble("LATITUDE");
            double longitude = jsonObjectPosto.getDouble("LONGITUDE");

            int idBandeira = jsonObjectPosto.getInt("ID_BANDEIRA");

            Bandeira bandeira = getBandeira(idBandeira);

            posto =  new Posto(id, endereco, nome, latitude, longitude, bandeira);

            Log.d("POSTO", posto.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return posto;
    }

    /**
     * Acessa o web service, o método get tipo que retorna um posto que esteja cadastrado no banco no formato json
     * o json é processado e retorna um objeto da classe Tipo.
     * @return Um objeto Tipo.
     */
    public static Bandeira getBandeira(int id) {
        Bandeira bandeira = null;

        try {
            JSONObject jsonObjectBandeira;

            jsonObjectBandeira = new BuscarJSONObject().execute(URL + "/bandeira/" + id).get().getJSONArray("bandeira").getJSONObject(0);

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
}
