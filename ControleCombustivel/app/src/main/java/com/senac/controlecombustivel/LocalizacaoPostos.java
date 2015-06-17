package com.senac.controlecombustivel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.senac.controlecombustivel.model.GPSTracker;

public class LocalizacaoPostos extends ActionBarActivity {

    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_postos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void mostrarLocalizacao(View view) {
        Intent intent = new Intent(this, LocalizacaoPostosMap.class);
        // Create class object
        gps = new GPSTracker(LocalizacaoPostos.this);

        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);

            startActivity(intent);

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }

    }

    public void mostrarRelatorios(View view) {
        Intent intent = new Intent(this, VisualizarRelatorio.class);

        intent.putExtra("idAndroid", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_localizacao_postos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("ACTION BAR", "SETTINGS");
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}