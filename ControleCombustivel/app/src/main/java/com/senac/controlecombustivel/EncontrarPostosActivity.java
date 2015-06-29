package com.senac.controlecombustivel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.senac.controlecombustivel.banco.BackupSQLiteHelper;
import com.senac.controlecombustivel.banco.BandeiraDAO;
import com.senac.controlecombustivel.banco.CombustivelDAO;
import com.senac.controlecombustivel.banco.PostoDAO;
import com.senac.controlecombustivel.banco.TipoDAO;
import com.senac.controlecombustivel.model.Bandeira;
import com.senac.controlecombustivel.model.Combustivel;
import com.senac.controlecombustivel.model.GPSTracker;
import com.senac.controlecombustivel.model.Posto;
import com.senac.controlecombustivel.model.Tipo;
import com.senac.controlecombustivel.webservice.WebService;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class EncontrarPostosActivity extends ActionBarActivity {

    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrar_postos);

        fazBackupSeNecessario();

        GPSTracker gps = new GPSTracker(EncontrarPostosActivity.this);

        latitude = gps.getLatitude();
        // Check if GPS enabled
        if (gps.canGetLocation()) {
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

    private void fazBackupSeNecessario() {
        BackupSQLiteHelper banco = new BackupSQLiteHelper(this);

        Date ultimoBackup = banco.getUltimaData();
        Date agora = Calendar.getInstance().getTime();

        if (ultimoBackup.before(agora)) {
            Log.d("INFO BACKUP", "Realizar backup local.");
            // Realiza backup das tabelas
            backupBandeiras(WebService.getBackupBandeiras());
            backupTipos(WebService.getBackupTipos());
            backupCombustiveis(WebService.getBackupCombustiveis());
            backupPostos(WebService.getBackupPostos());
            // Insere na tabela BACKUP LOG a ultima data de backup.
            banco.inserirBackupLog(agora);
        } else {
            Log.d("INFO BACKUP", "Não realizar o backup.");
        }
    }

    private void backupPostos(List<Posto> backupPostos) {
        PostoDAO banco = new PostoDAO(this);

        for (Posto p : backupPostos) {
            banco.inserirPosto(p);
        }
    }

    private void backupCombustiveis(List<Combustivel> backupCombustiveis) {
        CombustivelDAO banco = new CombustivelDAO(this);

        for (Combustivel c : backupCombustiveis) {
            banco.inserirCombustivel(c);
        }
    }

    private void backupTipos(List<Tipo> backupTipos) {
        TipoDAO banco = new TipoDAO(this);

        for (Tipo t : backupTipos) {
            banco.inserirTipo(t);
        }
    }

    private void backupBandeiras(List<Bandeira> backupBandeiras) {
        BandeiraDAO banco = new BandeiraDAO(this);

        for (Bandeira b : backupBandeiras) {
            banco.inserirBandeira(b);
        }
    }

    public void mostrarLocalizacao(View view) {
        Intent intent = new Intent(this, EncontrarPostosMapaActivity.class);

        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);

        startActivity(intent);
    }

    public void mostrarRelatorios(View view) {
        Intent intent = new Intent(this, VisualizarRelatorioActivity.class);
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
                startActivity(new Intent(this, VisualizarRelatorioActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}