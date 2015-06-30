package com.senac.controlecombustivel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.senac.controlecombustivel.banco.BackupLog;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
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

        //Log.d("BT - 1", "Chamando o metodo fazBackupSeNecessario()");
        //fazBackupSeNecessario();

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
        BackupLog backupLog = new BackupLog(this);
        Log.d("BT - 2", "Dentro do fazBackupSeNecessario, criando o banco e pegando a ultima data e a data de agora.");

        Date dataUltimoBackup = backupLog.getUltimaData();

        Date agora = Calendar.getInstance().getTime();

        int dataAtual = Integer.parseInt(agora.getYear() + "" + agora.getMonth() + "" + agora.getDay());

        Log.d("BT - 3", "Data agora " +agora);

        // Se a data do ultimo backup NÃO for nula
        // verifica se ela é mais é anterior a data atual,
        // se for, realiza o backup.
        if (dataUltimoBackup != null) {
            Log.d("BT - 4", "UltimoBackup diferente de nulo.");

            if (dataUltimoBackup.before(agora)) {
                Log.d("BT - 5", "UltimoBackup é antes do agora. Entao realizaBackup()");
                realizaBackup();
                // Insere na tabela BACKUP LOG a ultima data de backup.
                backupLog.inserirBackupLog(agora);
            } else {
                Log.d("BT - 6", "Não realizar o back. DELETANDO TABELA BACKUP LOG.");
                backupLog.deletarBackupLog();
            }
        // Se a data do ultimo backup retornar nula,
        // realiza o backup, e insere a data de ultimo backup.
        } else {
            Log.d("BT - 8", "Ultimo Backup é nulo ");
            Log.d("BT - 9", "Realizando o primeiro backup do aplicativo. Data de hoje " + agora.toString());
            realizaBackup();
            // Insere na tabela BACKUP LOG a ultima data de backup.
            backupLog.inserirBackupLog(agora);
            backupLog.getUltimaData();
        }
    }

    private void realizaBackup() {
        Log.d("BT - 10", "Realizar backup local.");
        // Realiza backup das tabelas
        backupBandeiras(WebService.getBackupBandeiras());
        backupTipos(WebService.getBackupTipos());
        backupCombustiveis(WebService.getBackupCombustiveis());
        backupPostos(WebService.getBackupPostos());
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

        banco.getBandeiras();
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