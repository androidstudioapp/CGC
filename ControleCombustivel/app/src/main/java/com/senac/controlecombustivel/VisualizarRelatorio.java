package com.senac.controlecombustivel;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.senac.controlecombustivel.model.Abastecimento;
import com.senac.controlecombustivel.model.Relatorio;
import com.senac.controlecombustivel.webservice.WebService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class VisualizarRelatorio extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_relatorio);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Pegando a list view do layout.
        ListView listView = (ListView) findViewById(R.id.lv_relatorio_lista);

        // Criando os arrays que definem onde os dados vindo da lista de Map vão ficar.
        String[] de = {"data", "valor", "litros", "combustivel", "posto"};
        int[] para = {R.id.tv_relatorio_data, R.id.tv_relatorio_valorTotal, R.id.tv_relatorio_litros, R.id.tv_relatorio_tipo, R.id.tv_relatorio_posto};

        // Recuperando o id android pra pegar os abastecimentos.
        String idAndroid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Pegando do web service o relatório
        Relatorio relatorio = WebService.getRelatorioAbastecimentos(idAndroid);

        // Pegando as views do valor total, posto e combustivel mais utilizados.
        TextView textViewValor = (TextView) findViewById(R.id.tv_relatorio_gastoTotal);
        TextView textViewPosto = (TextView) findViewById(R.id.tv_relatorio_postoMaisUsado);
        TextView textViewCombustivel = (TextView) findViewById(R.id.tv_relatorio_combustivelMaisUsado);

        Log.d("VISUALIZAR RELATORIO", relatorio.toString());

        // Definindo os valores nos text view dos atributos do relatorio, valor total, posto e combustivel mais utilizados.
        textViewValor.setText(String.valueOf(relatorio.getValorTotal()));
        textViewPosto.setText(relatorio.getPostoMaisUsado());
        textViewCombustivel.setText(relatorio.getCombustivelMaisUsado());


        // Criando o adapter, mandando este contexto, a lista de Map, o layout de cada item, e os arrays que conferem o nome do item do mapa com o id do layout.
        SimpleAdapter adapter = new SimpleAdapter(this, getListAbastecimentos(relatorio.getAbastecimentos()), R.layout.relatorio_item, de, para);

        // Definindo o adapter na lista.
        listView.setAdapter(adapter);
    }

    public List<Map<String, Object>> getListAbastecimentos(List<Abastecimento> abastecimentos) {
        List<Map<String, Object>> lista = new ArrayList<>();

        for (Abastecimento a : abastecimentos) {
            Map<String, Object> item = new HashMap<>();

            item.put("data", new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(a.getData()));
            item.put("valor", a.getValorTotal() + " R$");
            item.put("litros", a.getLitros() + " L");
            item.put("combustivel", a.getTiposCombustivel().getTipoCombustivelAbreviado());
            item.put("posto", a.getTiposCombustivel().getPosto().getNome());

            lista.add(item);

            Log.d("LISTA", item.toString());
        }

        return lista;
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
                Intent intent = getIntent();
                finish();
                startActivity(intent);

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
