package com.senac.controlecombustivel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.senac.controlecombustivel.model.Posto;
import com.senac.controlecombustivel.webservice.WebService;

import java.util.ArrayList;
import java.util.List;

public class EncontrarPostosMapaActivity extends ActionBarActivity {

    private GoogleMap mMap; // Pode ser nulo, se o Google Play services APK naoo esta disponivel

    private List<MarkerOptions> marcacoes = new ArrayList<>();

    private double latitude;
    private double longitude;

    private List<Posto> postos;

    private LatLng posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_postos_mapa);

        // Coloca o botao de voltar na barra de acao.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recuperando atributos do Intent extra.
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        posicao = new LatLng(latitude, longitude);

        criarMapaSeNecessario();

        // Cria as marcacoes com os dados dos postos, e adiciona no mapa.
        addMarcacoesPostos();
    }

    private void addMarcacoesPostos() {
        postos = WebService.getPostos();

        for (Posto p : postos) {
            if (calcularDistancia(latitude, longitude, p.getLatitude(), p.getLongitude()) <= 1000) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(p.getLatitude(), p.getLongitude()))
                        .title(p.getNome())
                        .visible(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_gas_station_black_48dp));
                mMap.addMarker(markerOptions);

                marcacoes.add(markerOptions);
            } else {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(p.getLatitude(), p.getLongitude()))
                        .title(p.getNome())
                        .visible(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_gas_station_black_48dp));
                mMap.addMarker(markerOptions);

                marcacoes.add(markerOptions);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        criarMapaSeNecessario();
    }

    private void criarMapaSeNecessario() {
        // Verifica se o mapa, ainda não foi instanciado.
        if (mMap == null) {
            // Tenta obter o mapa do SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Verifica se a obtenção do mapa, foi bem sucedida.
            if (mMap != null) {
                criarMapa();
            }
        }
    }


    private void criarMapa() {
        // Adicionando o circulo com raio de 1000 metros.
        mMap.addCircle(new CircleOptions()
                .center(posicao)
                .radius(1000)
                .fillColor(Color.argb(100, 255, 169, 31))
                .strokeWidth(1));

        // Movendo a camera para a localizacao, atual, do usuario.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicao));

        // Criando a marcacao da localizacao, atual, do usuario.
        MarkerOptions markerOptions = new MarkerOptions()
                                    .position(posicao)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_48dp));

        // Adicionando a marcacao no mapa e na lista de marcacoes.
        mMap.addMarker(markerOptions);
        marcacoes.add(markerOptions);

        // Definindo o tratador de clicks nas marcacoes
        mMap.setOnMarkerClickListener(new MarcadorListener());
    }

    /**
     * Calcula a distancia, em metros, de um ponto geografico, para outro ponto geografico,
     * recebendo a latitude e longitude, de cada ponto.
     *
     * @param deLat Latitude do ponte inicial.
     * @param deLong Longitude do ponto inicial.
     * @param paraLat Latitude do ponto final.
     * @param paraLong Longitude do ponto final.
     *
     * @return Distancia, em metros, entre os dois pontos.
     */
    private double calcularDistancia(double deLat, double deLong,
                                     double paraLat, double paraLong) {
        double d2r = Math.PI / 180;
        double dLong = (paraLong - deLong) * d2r;
        double dLat = (paraLat - deLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(deLat * d2r)
                * Math.cos(paraLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }


    private class MarcadorListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            MarkerOptions markerOptions = marcacoes.get(Integer.parseInt(marker.getId().replaceAll("m", "")));

            if (markerOptions.isVisible() && !marker.getId().endsWith("0")) {
                Intent intent = new Intent(EncontrarPostosMapaActivity.this, InformacoesPostoActivity.class);
                // Inserindo o id do posto pra proxima tela e substituindo o 'm' do nome da marcacao por nada
                intent.putExtra("posto", postos.get(Integer.parseInt(marker.getId().replaceAll("m", "")) - 1));

                startActivity(intent);
            } else {
                Log.e("EVENTO MARCAÇÃO", "TENTATIVA DE VISUALIZAR A MARCACAO ZERO");
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // "Infla" os itens do menu, para usar na barra de acao
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Trata os click nos itens da barra de acao.
        switch (item.getItemId()) {
            case R.id.menu_relatorio:
                startActivity(new Intent(this, VisualizarRelatorioActivity.class));
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