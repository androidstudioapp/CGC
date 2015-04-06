package com.senac.controlecombustivel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LocalizacaoPostos extends Activity {

        Button btnShowLocation;

        // GPSTracker class
        GPSTracker gps;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_localizacao_postos);

            btnShowLocation = (Button) findViewById(R.id.show_location);

            final Intent intent = new Intent(this, LocalizacaoPostosMap.class);

            // Show location button click event
            btnShowLocation.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // Create class object
                    gps = new GPSTracker(LocalizacaoPostos.this);

                    // Check if GPS enabled
                    if(gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude", longitude);

                        startActivity(intent);

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }
                }
            });
        }
    }