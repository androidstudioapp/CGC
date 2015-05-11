package com.senac.controlecombustivel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;


public class InformacoesPostoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_posto);

        int idPosto = getIntent().getIntExtra("idPosto",0);

        if (idPosto != 0) {
            TextView tv_nome = (TextView) findViewById(R.id.nome);
            TextView tv_endereco = (TextView) findViewById(R.id.endereco);

            EditText et_gasolinaPreco = (EditText) findViewById(R.id.et_gasolinaPreco);
            EditText et_etanolPreco = (EditText) findViewById(R.id.et_etanolPreco);
            EditText et_gnvPreco = (EditText) findViewById(R.id.et_gnvPreco);

            List<TiposCombustivel> combustiveis =  WebService.getCombustiveisPorPosto(idPosto);

            for(TiposCombustivel t : combustiveis) {
                System.out.println(t.toString());
            }

            String nome = combustiveis.get(0).getPosto().getNome();
            String endereco = combustiveis.get(0).getPosto().getEndereco();

            double gasolinaPreco = combustiveis.get(0).getPreco();
            double etanolPreco = combustiveis.get(1).getPreco();
            double gnvPreco = combustiveis.get(2).getPreco();

            tv_nome.setText(nome);
            tv_endereco.setText(endereco);

            et_gasolinaPreco.setText(String.valueOf(gasolinaPreco));
            et_etanolPreco.setText(String.valueOf(etanolPreco));
            et_gnvPreco.setText(String.valueOf(gnvPreco));
        } else {
            throw new InvalidParameterException("Índice do posto com valor zero");
        }

    }

    public void teste(View view) {
        Log.d("ON CLICK", "TESTE " + view.toString());
    }
}
