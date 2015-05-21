package com.senac.controlecombustivel;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;


public class InformacoesPostoActivity extends ActionBarActivity {

    private EditText et_gasolinaPreco;
    private EditText et_etanolPreco;
    private EditText et_gnvPreco;

    private List<TiposCombustivel> combustiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_posto);

        int idPosto = getIntent().getIntExtra("idPosto",0);

        if (idPosto != 0) {
            TextView tv_nome = (TextView) findViewById(R.id.nome);
            TextView tv_endereco = (TextView) findViewById(R.id.endereco);

            et_gasolinaPreco = (EditText) findViewById(R.id.et_gasolinaPreco);
            et_etanolPreco = (EditText) findViewById(R.id.et_etanolPreco);
            et_gnvPreco = (EditText) findViewById(R.id.et_gnvPreco);

            combustiveis =  WebService.getCombustiveisPorPosto(idPosto);

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
            throw new InvalidParameterException("Indice do posto com valor zero");
        }

    }

    public void teste(View view) {
        Log.d("DEBUGANDO CLICK 'OK'", view.toString());
        Log.d("DEBUGANDO CLICK 'OK'", "" + view.getId());

        double preco = 0;

        switch (view.getId()) {

            case R.id.bt_gasolinaOk :
                preco = Double.parseDouble(String.valueOf(et_gasolinaPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "Gasolina ok " + preco);

                combustiveis.get(0).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(0));

                Toast.makeText(this, "Preço da Gasolina atualizada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_etanolOk :
                preco = Double.parseDouble(String.valueOf(et_etanolPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "Etanol ok " + preco);

                combustiveis.get(1).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(1));

                Toast.makeText(this, "Preço do Etanol atualizado", Toast.LENGTH_LONG).show();
                break;
            case R.id.bt_gnvOk :
                preco = Double.parseDouble(String.valueOf(et_gnvPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "GNV ok " + preco);

                combustiveis.get(2).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(2));

                Toast.makeText(this, "Preço do GNV atualizado", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }
}
