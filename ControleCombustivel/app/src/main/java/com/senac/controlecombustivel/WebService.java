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

        JSONArray jsonArrayPostos = getJSONArray(URL+"/postos");

        System.out.println(jsonArrayPostos.toString());

        for (int i = 0 ; i < jsonArrayPostos.length() ; i++) {
            try {
                JSONObject jsonPosto = jsonArrayPostos.getJSONObject(i);

                int id = jsonPosto.getInt("ID");
                String endereco = jsonPosto.getString("ENDERECO");
                String nome = jsonPosto.getString("NOME");
                double latitude = jsonPosto.getDouble("LATITUDE");
                double longitude = jsonPosto.getDouble("LONGITUDE");

                postos.add(i, new Posto(id, endereco, nome, latitude, longitude));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return postos;
    }

    /**
     * Recebe a url do web service que retorna um json, carrega o json em uma string e retorna de la um json array.
     * @param URL
     * @return
     */
    private static JSONArray getJSONArray(String URL)  {
        URL url = null;
        String jsonString = "";
        JSONArray jsonArray = null;

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
                JSONObject jsonObject = new JSONObject(jsonString);

                jsonArray = jsonObject.getJSONArray("postos");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonArray;
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
