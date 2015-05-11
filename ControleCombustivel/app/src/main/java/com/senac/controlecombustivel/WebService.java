package com.senac.controlecombustivel;

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

        JSONObject jsonObjectPostos = getJSONObject(URL + "/postos");

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
     * @param id
     * @return
     */
    public static TiposCombustivel getTiposCombustivel(int id) {
        TiposCombustivel tiposCombustivel = null;

        JSONObject jsonObjectTiposCombustivel = getJSONObject(URL + "/tiposcombustivel/" + id);

        try {
            id = jsonObjectTiposCombustivel.getInt("ID");
            double preco = jsonObjectTiposCombustivel.getDouble("PRECO");

            int idPosto = jsonObjectTiposCombustivel.getInt("ID_POSTO");
            int idTipo = jsonObjectTiposCombustivel.getInt("ID_TIPO");
            int idCombustivel = jsonObjectTiposCombustivel.getInt("ID_COMBUSTIVEL");

            Posto posto = getPosto(idPosto);
            Tipo tipo = getTipo(idTipo);
            Combustivel combustivel = getCombustivel(idCombustivel);

            tiposCombustivel = new TiposCombustivel(id, posto, preco, tipo, combustivel);
        } catch (JSONException e) {
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

        JSONObject jsonObjectCombustivel = getJSONObject(URL + "/combustivel/" + id);

        try {
            id = jsonObjectCombustivel.getInt("ID");
            String nome = jsonObjectCombustivel.getString("NOME");

            combustivel = new Combustivel(id, nome);
        } catch (JSONException e) {
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

        JSONObject jsonObjectTipo = getJSONObject(URL + "/tipo/" + id);

        try {
            id = jsonObjectTipo.getInt("ID");
            String nome = jsonObjectTipo.getString("NOME");

            tipo = new Tipo(id, nome);
        } catch (JSONException e) {
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
            JSONObject jsonObjectPosto = getJSONObject(URL+"/posto/"+id);

            id = jsonObjectPosto.getInt("ID");
            String endereco = jsonObjectPosto.getString("ENDERECO");
            String nome = jsonObjectPosto.getString("NOME");
            double latitude = jsonObjectPosto.getDouble("LATITUDE");
            double longitude = jsonObjectPosto.getDouble("LONGITUDE");

            int idBandeira = jsonObjectPosto.getInt("ID_BANDEIRA");

            Bandeira bandeira = getBandeira(idBandeira);

            posto =  new Posto(id, endereco, nome, latitude, longitude, bandeira);
        } catch (JSONException e) {
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
            JSONObject jsonObjectBandeira = getJSONObject(URL + "/bandeira/" + id);

            id = jsonObjectBandeira.getInt("ID");
            String nome = jsonObjectBandeira.getString("NOME");

            bandeira = new Bandeira(id, nome);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bandeira;
    }

    /**
     *
     * @param URL
     * @return
     */
    private static JSONObject getJSONObject(String URL) {
        URL url = null;
        String jsonString = "";
        JSONObject jsonObject = null;

        // Cria o objeto url
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Conecta com o url pra realizar input e output de dados.
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pega o input stream de dados da url, e passa para uma string.
        if(urlConnection.getDoInput()) {
            InputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                jsonString = lerStream(inputStream);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Transforma a string da url para um json array "postos".
        try {
            if(!jsonString.equals("")) {
                jsonObject = new JSONObject(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * Recebe um input stream, carrega todos dados inseridos nele em um string builder e retorna um json string.
     * @param inputStream
     * @return
     */
    private static String lerStream(InputStream inputStream) {
        String linha;
        BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();

        try {
            while ((linha = bfr.readLine()) != null) {
                total.append(linha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total.toString();
    }
}
