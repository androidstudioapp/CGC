package com.senac.controlecombustivel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.senac.controlecombustivel.model.GPSTracker;

public class LocalizacaoPostos extends ActionBarActivity {

    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_postos);

        GPSTracker gps = new GPSTracker(LocalizacaoPostos.this);

        // Check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        GoogleMap mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa))
                .getMap();

        mapa.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
    }

    /**
     * M�todo que � chamado ao clicar no bot�o "Procurar".
     * @param view
     */
    public void mostrarLocalizacao(View view) {
        Intent intent = new Intent(this, LocalizacaoPostosMap.class);

        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        startActivity(intent);
    }

    /**
     * M�todo que � chamado ao clicar no bot�o de "Relat�rios".
     * @param view
     */
    public void mostrarRelatorios(View view) {
        Intent intent = new Intent(this, VisualizarRelatorio.class);
        startActivity(intent);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}