package com.senac.controlecombustivel;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalizacaoPostosMap extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    // GPSTracker class
    GPSTracker gps;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao_postos_map);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        setUpMapIfNeeded();

        addMarcacoesPostos();
    }

    private void addMarcacoesPostos() {
        String [] titulos = {"Comercial Farroupilha",
                        "Abastecedora de Combustiveis Bela Vista",
                        "Posto Touring",
                        "Firense Combustíveis Ltda",
                        "Heinen Comercial de Combustiveis",
                        "Posto Luna",
                        "Psc Posto de Combustiveis",
                        "Mabrhel Combustiveis",
                        "Comércio de Comb Venâncio Aires Ltda",
                        "J F A Silveira"};

        LatLng [] positions = {new LatLng(-30.0254448,-51.2154747),
                                new LatLng(-30.0331084, -51.1953674),
                                new LatLng(-30.0373793, -51.2195615),
                                new LatLng(-30.0417054, -51.2125330),
                                new LatLng(-30.0407658, -51.2222989),
                                new LatLng(-30.0465253, -51.2293121),
                                new LatLng(-30.024792, -51.215373),
                                new LatLng(-30.027737, -51.216322),
                                new LatLng(-30.041878, -51.217499),
                                new LatLng(-30.032813, -51.212606)};


        for (int i = 0 ; i < 10 ; i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(positions[i])
                        .title(titulos[i]));
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

        // Instantiates a new CircleOptions object and defines the center and radius
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1000)
                .fillColor(new Color().argb(80, 153, 204, 255))
                .strokeWidth(1); // In meters

        // Get back the mutable Circle
        Circle circle = mMap.addCircle(circleOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Você Está Aqui!"));
    }
}
