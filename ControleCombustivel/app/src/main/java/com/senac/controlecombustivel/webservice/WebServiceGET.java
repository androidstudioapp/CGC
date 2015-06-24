package com.senac.controlecombustivel.webservice;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebServiceGET extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... URL) {
        URL url = null;
        String jsonString = "";
        JSONObject jsonObject = null;

        // Cria o objeto url
        try {
            url = new URL(URL[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Conecta com o url pra realizar input e output de dados.
        URLConnection urlConnection = null;
        try {

            urlConnection = url.openConnection();

            // Pega o input stream de dados da url, e passa para uma string.
            if (urlConnection.getDoInput()) {
                InputStream inputStream;
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                jsonString = lerStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transforma a string da url para um json array.
        try {
            if (!jsonString.equals("")) {
                jsonObject = new JSONObject(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * Recebe um input stream, carrega todos dados inseridos nele em um string builder e retorna um json string.
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