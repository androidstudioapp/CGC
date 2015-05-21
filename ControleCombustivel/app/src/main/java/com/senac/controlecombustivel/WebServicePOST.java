package com.senac.controlecombustivel;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by Dantieris on 21/05/2015.
 */
public class WebServicePOST extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        String tag = "DEBUGANDO WEB SERVICE POST";
        Log.d(tag, Arrays.toString(strings));
        URL url = null;
        String jsonString = strings[1];

        // Cria o objeto url
        try {
            url = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Conecta com o url pra realizar input e output de dados.
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");

            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            urlConnection.setRequestProperty("Content-Length", "" +
                    Integer.toString(jsonString.getBytes().length));
            //urlConnection.setRequestProperty("Content-Language", "en-US");

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            if (urlConnection.getDoOutput()) {
                Log.d(tag, "Do output true");
                DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());

                dos.writeBytes(jsonString);
                dos.flush();
                dos.close();
            }

            if (urlConnection.getDoInput()) {
                InputStream inputStream = null;
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("DEBUGANDO WS POST", lerStream(inputStream));
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Recebe um input stream, carrega todos dados inseridos nele em um string builder e retorna um json string.
     *
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
