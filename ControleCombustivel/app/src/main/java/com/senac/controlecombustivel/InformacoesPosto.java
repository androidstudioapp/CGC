package com.senac.controlecombustivel;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.senac.controlecombustivel.model.Abastecimento;
import com.senac.controlecombustivel.model.TiposCombustivel;
import com.senac.controlecombustivel.webservice.WebService;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class InformacoesPosto extends ActionBarActivity {

    private EditText et_gasolinaPreco;
    private EditText et_etanolPreco;
    private EditText et_gnvPreco;

    private Spinner spinner;

    private List<TiposCombustivel> combustiveis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_posto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int idPosto = getIntent().getIntExtra("idPosto",0);

        if (idPosto != 0) {

            String[] array_spinner = new String[3];
            array_spinner[0]="Gasolina";
            array_spinner[1]="Etanol";
            array_spinner[2]="GNV";

            spinner  = (Spinner) findViewById(R.id.spinner);

            ArrayAdapter adapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, array_spinner);

            spinner.setAdapter(adapter);

            TextView tv_nome = (TextView) findViewById(R.id.tv_posto_nome);
            TextView tv_endereco = (TextView) findViewById(R.id.tv_posto_endereco);

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

    public void atualizaPreco(View view) {
        Log.d("DEBUGANDO CLICK 'OK'", view.toString());
        Log.d("DEBUGANDO CLICK 'OK'", "" + view.getId());

        double preco;

        switch (view.getId()) {

            case R.id.bt_gasolinaOk :
                preco = Double.parseDouble(String.valueOf(et_gasolinaPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "Gasolina ok " + preco);

                combustiveis.get(0).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(0));

                Toast.makeText(this, "Preco da Gasolina atualizada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_etanolOk :
                preco = Double.parseDouble(String.valueOf(et_etanolPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "Etanol ok " + preco);

                combustiveis.get(1).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(1));

                Toast.makeText(this, "Preco do Etanol atualizado", Toast.LENGTH_LONG).show();
                break;
            case R.id.bt_gnvOk :
                preco = Double.parseDouble(String.valueOf(et_gnvPreco.getText()));
                Log.d("DEBUGANDO CLICK 'OK'", "GNV ok " + preco);

                combustiveis.get(2).setPreco(preco);
                WebService.atualizarTipoaCombustivel(combustiveis.get(2));

                Toast.makeText(this, "Preco do GNV atualizado", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    public void inserirAbastecimento(View view) {
        EditText et_valorTotal = (EditText) findViewById(R.id.et_valorTotal);

        // Se o valor total nao for nulo
        if (!et_valorTotal.getText().equals("")) {
            TiposCombustivel tiposCombustivel = combustiveis.get(spinner.getSelectedItemPosition());
            double valorTotal = Double.parseDouble(String.valueOf(et_valorTotal.getText()));
            double litros = Math.round((valorTotal / tiposCombustivel.getPreco())*100.0)/100.0;
            Date data = Calendar.getInstance().getTime();
            String idAndroid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            WebService.inserirAbastecimento(new Abastecimento(valorTotal, idAndroid, litros, tiposCombustivel, data));

            Toast.makeText(this, "Abastecimento inserido", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Valor total nulo", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_relatorio:
                startActivity(new Intent(this, VisualizarRelatorio.class));
                return true;
            // 16908332 é o id do botão de seta para voltar.
            case 16908332:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
