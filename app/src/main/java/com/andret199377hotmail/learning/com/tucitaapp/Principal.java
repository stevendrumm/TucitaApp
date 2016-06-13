package com.andret199377hotmail.learning.com.tucitaapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.andret199377hotmail.learning.com.tucitaapp.HeadlinesFragment.NOMBREFECHA;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HeadlinesFragment.OnHeadlineSelectedListener {

    private SQLiteDatabase db;
    DatePicker fecha;
    Spinner centroproduccion;
    private View mProgressView;
    private View mCitaFormView;
    ToggleButton buscar;
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
        mProgressView = findViewById(R.id.citas_progress);
        mCitaFormView = findViewById(R.id.citas_form);





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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
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
                dialogo1.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1,int id){


                    }

                });
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

                });
                dialogo2.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogo1,int id){


                    }

                });
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

    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        ArticleFragment articleFrag = (ArticleFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);


        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.firts_fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    private void cargarHorasCitas() {
        if (findViewById(R.id.firts_fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
                HeadlinesFragment firstFragment = new HeadlinesFragment();
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

                // Add the fragment to the 'fragment_container' FrameLayout

            getSupportFragmentManager().beginTransaction().replace(R.id.firts_fragment_container, firstFragment).commit();

        }

    }
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCitaFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCitaFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCitaFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCitaFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void mostrar(View v){
        if(buscar.isChecked()){

            cargarHorasCitas();
        }else{

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

}
