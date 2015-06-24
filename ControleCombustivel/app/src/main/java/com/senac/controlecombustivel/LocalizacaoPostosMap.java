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

public class LocalizacaoPostosMap extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private List<MarkerOptions> marcacoes = new ArrayList<>();

    private double latitude;
    private double longitude;

    private List<Posto> postos;

    private LatLng posicao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_postos_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        posicao = new LatLng(latitude, longitude);

        setUpMapIfNeeded();

        addMarcacoesPostos();
    }

    private void addMarcacoesPostos() {
        postos = WebService.getPostos();

        for (Posto p : postos) {
            if (calculateDistance(latitude, longitude, p.getLatitude(), p.getLongitude()) <= 1000.0) {
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
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Adicionando o círculo com raio de 1000 metros.
        mMap.addCircle(new CircleOptions()
                .center(posicao)
                .radius(1000)
                .fillColor(Color.argb(50, 196, 0, 12))
                .strokeWidth(1));

        // Dando zoom no mapa.
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posicao, 14.0f));

        MarkerOptions markerOptions = new MarkerOptions()
                .position(posicao)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_48dp));

        mMap.addMarker(markerOptions);
        marcacoes.add(markerOptions);

        mMap.setOnMarkerClickListener(new MarcadorListener());
    }

    private double calculateDistance(double fromLat, double fromLong,
                                     double toLat, double toLong) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

    private class MarcadorListener implements GoogleMap.OnMarkerClickListener {

        @Override
        public boolean onMarkerClick(Marker marker) {
            MarkerOptions markerOptions = marcacoes.get(Integer.parseInt(marker.getId().replaceAll("m", "")));

            if (markerOptions.isVisible() && !marker.getId().endsWith("0")) {
                Intent intent = new Intent(LocalizacaoPostosMap.this, InformacoesPosto.class);
                // Inserindo o id do posto pra proxima tela e substituindo o 'm' do nome da marcacao por nada
                intent.putExtra("idPosto", Integer.parseInt(marker.getId().replaceAll("m", "")));

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