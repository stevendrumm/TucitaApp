package com.andret199377hotmail.learning.com.tucitaapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HeadlinesFragment.OnHeadlineSelectedListener {

    private SQLiteDatabase db;
    DatePicker fecha;
    Spinner centroproduccion;
    private View mProgressView;
    private View mCitaFormView;
    ToggleButton buscar;
    HeadlinesFragment firstFragment;
    TextView nombre1;
    TextView nombre2;
    TextView apellido1;
    TextView apellido2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fecha = (DatePicker) findViewById(R.id.datePickerFechaCita);
        centroproduccion = (Spinner) findViewById(R.id.spinnerCentroProduccion);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.TipoDeCita,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centroproduccion.setAdapter(adapter);

        buscar = (ToggleButton) findViewById(R.id.toggleButtonBuscar);
        mCitaFormView = findViewById(R.id.cita_form);
        nombre1 = (TextView) findViewById(R.id.Nombre1);
        nombre2 = (TextView) findViewById(R.id.Nombre2);
        apellido1 = (TextView) findViewById(R.id.Apellido1);
        apellido2 = (TextView) findViewById(R.id.Apellido2);

        perfilUsuario();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("perfil");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_perm_identity_white_24dp));
        tabs.addTab(spec);


        spec=tabs.newTabSpec("cita");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_list_white_24dp));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("configuracion");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_settings_white_24dp));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                Intent actividad = new Intent(Principal.this,SettingsActivity.class);
                startActivity(actividad);
                break;
            case R.id.cerrarsesion:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle(R.string.Important);
                dialogo1.setMessage(R.string.CloseMessage);
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cerrarSesion();
                        Toast t = Toast.makeText(Principal.this, R.string.MessageExitProgram, Toast.LENGTH_SHORT);
                        t.show();
                        Intent actividad = new Intent(Principal.this, LoginActivity.class);
                        startActivity(actividad);
                    }
                });
                /*dialogo1.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1,int id){


                    }

                });*/
                dialogo1.show();
                break;
            case R.id.salir:
                AlertDialog.Builder dialogo2 = new AlertDialog.Builder(this);
                dialogo2.setTitle(R.string.Important);
                dialogo2.setMessage(R.string.ExitMessage);
                dialogo2.setCancelable(false);
                dialogo2.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo2, int id){
                        System.exit(0);
                    }

                });/*
                dialogo2.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1,int id){


                    }

                });*/
                dialogo2.show();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.perfil) {
            // Handle the camera action
        } else if (id == R.id.citas) {

        } else if (id == R.id.reservas) {

        } else if (id == R.id.action_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cerrarSesion() {
        LoginSQLiteHelper Ldbh = new LoginSQLiteHelper(this);
        db = Ldbh.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN, 0);
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN + " = 1";
        //String[] selectionArgs = {1};
        db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, null);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Principal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.andret199377hotmail.learning.com.tucitaapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Principal Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.andret199377hotmail.learning.com.tucitaapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void onArticleSelected(int position, final String itemAtPosition) {
        AlertDialog.Builder dialogo3 = new AlertDialog.Builder(this);
        dialogo3.setTitle(R.string.Important);
        dialogo3.setMessage(R.string.CitaMessage+" "+itemAtPosition+"?");
        dialogo3.setCancelable(false);
        dialogo3.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogo2, int id){
                Toast.makeText(getApplicationContext(),itemAtPosition,Toast.LENGTH_LONG).show();

            }

        });
        dialogo3.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogo1,int id){


            }

        });
        dialogo3.show();



    }

    private void cargarHorasCitas() {
        if (findViewById(R.id.firts_fragment_container) != null) {
            firstFragment = new HeadlinesFragment();
            Bundle args = new Bundle();
            args.putString(HeadlinesFragment.NOMBREFECHA, convertir(fecha.getYear())+"-"+convertir(fecha.getMonth()+1)+"-"+convertir(fecha.getDayOfMonth()));
            Log.i("fecha", convertir(fecha.getYear())+"-"+convertir(fecha.getMonth()+1)+"-"+convertir(fecha.getDayOfMonth()));
            switch (centroproduccion.getSelectedItemPosition()) {
                case 0:
                    args.putString(HeadlinesFragment.CENTROPRODUCCION, "1110");
                    Log.i("error", "1110");
                    break;
                case 1:
                    args.putString(HeadlinesFragment.CENTROPRODUCCION, "1301");
                    Log.i("error", "1301");
                    break;
            }
            firstFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.firts_fragment_container, firstFragment).commit();

        }

    }

    public void mostrar(View v){
        FrameLayout layout = (FrameLayout) findViewById(R.id.firts_fragment_container);
        if(buscar.isChecked()){
            mCitaFormView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);

            cargarHorasCitas();
        }else{
            layout.setVisibility(View.GONE);
            mCitaFormView.setVisibility(View.VISIBLE);

        }

    }

    public String convertir(int i) {
        String cadena = null;
        if (i < 10) {
            cadena = "0".concat(String.valueOf(i));
        }else{
            cadena = String.valueOf(i);
        }
        return cadena;
    }

    public void perfilUsuario(){
        LoginSQLiteHelper Ldbh = new LoginSQLiteHelper(this);
        db = Ldbh.getWritableDatabase();

        String[] campos = new String[] {
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERNOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDONOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERAPELLIDO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDOAPELLIDO
                };
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN+"=1";
        //String[] selectionArgs = new String[] {String.valueOf(1)};

        Cursor c = db.query(true,FeedReaderContract.FeedEntry.TABLE_NAME, campos, selection,null, null, null, null,null);

        if (c.moveToFirst()) {
            do {

                nombre1.setText(c.getString(0));
                nombre2.setText(c.getString(1));
                apellido1.setText(c.getString(2));
                apellido2.setText(c.getString(3));

            } while (c.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

    }


}
