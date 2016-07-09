package com.andret199377hotmail.learning.com.tucitaapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HeadlinesFragment.OnHeadlineSelectedListener {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final String CURRENT_TAB="tab";
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DIALOGDATEPICKER = "dialogDatepicker";
    private static final String VISIBILITY_CITAFORM = "visibilitycitaform";
    private static final String VISIBILITY_EMAILLOGINFORM = "visibilityemailloginform";
    private static final String VISIBILITY_FRAGMENT = "visibilityfragment";
    private static final String CHEKED_TOGGLE = "chekedtoggle";
    private static final String DOCUMENTO = "documento";
    private static final String TIPODOCUMENTO = "tipodocumento";
    private static final String NOMBRE_UNO = "primernombre";
    private static final String NOMBRE_DOS = "segundonombre";
    private static final String APELLIDO_UNO = "primerapellido";
    private static final String APELLIDO_DOS = "segundoapellido";
    private static final String FECHA = "fecha";


    private String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private SQLiteDatabase db;
    private TextView fecha;
    private Spinner centroproduccion;
    private Spinner profesional;
    private View login_progress;
    private View cita_form;
    private View email_login_form;
    private Spinner mTipoDocumentoView;
    private EditText mDocumentoView;
    public ToggleButton buscar;
    public HeadlinesFragment firstFragment;
    private ImageButton picbtn;
    public AsyncTask<String, Void, String> mAuthTask = null;
    public AsyncTask<String, Void, String> MyReservaTask =null;
    private LinearLayout lineascamposdatousuario;
    ArrayList<Profesional> profesionales;

    TextView documento;
    TextView tipo;
    TextView nombre1;
    TextView nombre2;
    TextView apellido1;
    TextView apellido2;
    TabHost tabs;
    Toolbar toolbar;
    DrawerLayout drawer;
    public TextView usuario;
    FrameLayout layout;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
        {

            setContentView(R.layout.activity_principal);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            configurarDrawerLayout(drawer,toolbar);
            tabs = (TabHost) findViewById(android.R.id.tabhost);
            poblartab21(tabs);

        }
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
        {

            setContentView(R.layout.app_bar_principal);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            tabs = (TabHost) findViewById(android.R.id.tabhost);
            poblartab19(tabs);
        }

        fecha = (TextView) findViewById(R.id.FechaCita);
        centroproduccion = (Spinner) findViewById(R.id.spinnerCentroProduccion);
        poblarSpinnerCentroProduc();
        profesional = (Spinner) findViewById(R.id.spinnerProfesional);


        buscar = (ToggleButton) findViewById(R.id.toggleButton);
        configurarSetOnclickListener();
        usuario = (TextView)findViewById(R.id.txtResultado);
        llenarUsuariosLogueados();
        layout = (FrameLayout) findViewById(R.id.firts_fragment_container);
        mTipoDocumentoView = (Spinner) findViewById(R.id.spinnerTipoDocumento);
        poblarSpinnerTipoDocum();

        mDocumentoView = (EditText) findViewById(R.id.numdocumento);
        configurarSetOnEditorActionListener();

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        cita_form = findViewById(R.id.cita_form);
        email_login_form = findViewById(R.id.email_login_form);
        login_progress = findViewById(R.id.login_progress);
        lineascamposdatousuario = (LinearLayout) findViewById(R.id.camposdatousuario);
        documento = (TextView) findViewById(R.id.documento);
        tipo = (TextView) findViewById(R.id.tipo);
        nombre1 = (TextView) findViewById(R.id.Nombre1);
        nombre2 = (TextView) findViewById(R.id.Nombre2);
        apellido1 = (TextView) findViewById(R.id.Apellido1);
        apellido2 = (TextView) findViewById(R.id.Apellido2);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            cita_form.setVisibility(show ? View.GONE : View.VISIBLE);
            cita_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cita_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            login_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            cita_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mDocumentoView.setError(null);
        // Store values at the time of the login attempt.
        String tipoDocumento = Integer.toString(mTipoDocumentoView.getSelectedItemPosition() + 1);
        String Documento = mDocumentoView.getText().toString();
        String login = "http://186.170.16.38/autenticacion/recurso/usuario.php";

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(Documento)) {
            mDocumentoView.setError(getString(R.string.error_field_required));
            focusView = mDocumentoView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                //Toast.makeText(LoginActivity.this, "tipodocumento="+tipoDocumento+"&documento=" + Documento, Toast.LENGTH_LONG).show();
                if (networkInfo != null && networkInfo.isConnected()) {
                    AsyncTask tarea = new UserLoginTask().execute(tipoDocumento, Documento, login);

                } else {
                    Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

    public void configurarDrawerLayout(DrawerLayout drawer, Toolbar tool){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void poblarSpinnerTipoDocum(){
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.TipoDocumento, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_view_item);
        mTipoDocumentoView.setAdapter(adapter);

    }

    public void poblarSpinnerCentroProduc(){

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.TipoDeCita, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_view_item);
        centroproduccion.setAdapter(adapter);
        centroproduccion.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        cargarProfesionales(position);
                        //Toast.makeText(Principal.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }

    public void cargarProfesionales(int  centro){
        String fechaProfesional = fecha.getText().toString();
        String centroProduccionProfesional = null;
        switch (centro) {
            case 0:
                centroProduccionProfesional = "1110";

                break;
            case 1:
                centroProduccionProfesional = "1301";
                break;
        }
        new buscarProfesional().execute(fechaProfesional, centroProduccionProfesional, "http://186.170.16.38/inc/buscar-profesional.php");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void poblartab21(final TabHost tabs){
        /*
        tabs.setup();
        TabHost.TabSpec spec= this.tabs.newTabSpec("Perfil");
        spec.setContent(R.id.tab1);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_perm_identity_white_36dp));
        this.tabs.addTab(spec);
        */
        tabs.setup();
        TabHost.TabSpec spec= this.tabs.newTabSpec("Cita");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",getApplicationContext().getDrawable(R.drawable.ic_list_white_24dp));
        this.tabs.addTab(spec);

        spec= this.tabs.newTabSpec("Configuracion");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_settings_white_36dp));
        this.tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                getSupportActionBar().setTitle(tabId);

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void poblartab19(final TabHost tabs){
        tabs.setup();
        TabHost.TabSpec spec= this.tabs.newTabSpec("Cita");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_list_white_36dp));
        this.tabs.addTab(spec);

        spec= this.tabs.newTabSpec("Configuracion");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", getResources().getDrawable(R.drawable.ic_settings_white_36dp));
        this.tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                getSupportActionBar().setTitle(tabId);

            }
        });

    }

    public void configurarSetOnEditorActionListener(){
        mDocumentoView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.documento || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

    }

    public void configurarSetOnclickListener(){
        buscar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(buscar.isChecked()){

                    cargarHorasCitas();

                    lineascamposdatousuario.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);




                }else{

                    layout.setVisibility(View.GONE);
                    lineascamposdatousuario.setVisibility(View.VISIBLE);


                }


            }


        });

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
        Log.i("hola", itemAtPosition);

        AlertDialog.Builder dialogo3 = new AlertDialog.Builder(this);
        dialogo3.setTitle(R.string.Important);
        String mensaje = getString(R.string.CitaMessage);
        dialogo3.setMessage(mensaje+" "+itemAtPosition+"?" );//
        dialogo3.setCancelable(false);
        AlertDialog.Builder builder = dialogo3.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo2, int id) {
                //Toast.makeText(getApplicationContext(),itemAtPosition,Toast.LENGTH_LONG).show();

                String urlcita = "http://186.170.16.38/api/citas/actualizarcita.php";
                String tipoDocumento = Integer.toString(mTipoDocumentoView.getSelectedItemPosition() + 1);
                String Documento = mDocumentoView.getText().toString();
                String centro = null;
                int item = centroproduccion.getSelectedItemPosition();
                switch(item){
                    case 0:
                        centro = "1110";
                        break;
                    case 1:
                        centro = "1301";
                        break;
                }

                String primernombre = nombre1.getText().toString();
                String segundonombre = nombre2.getText().toString();
                String primerapellido = apellido1.getText().toString();
                String segundoapellido = apellido2.getText().toString();
                String codigo = profesionales.get(profesional.getSelectedItemPosition()).CODIGO;
                Log.i("cita",tipoDocumento+" "+Documento+" "+itemAtPosition+" "+centro+" "+primernombre+" "+segundonombre+" "+primerapellido+" "+segundoapellido+" "+codigo);
                MyReservaTask = new ReservaCitaTask();
                MyReservaTask.execute(urlcita,tipoDocumento,Documento,itemAtPosition, centro, primernombre, segundonombre, primerapellido, segundoapellido, codigo);



                layout.setVisibility(View.GONE);
                email_login_form.setVisibility(View.VISIBLE);
                cita_form.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().remove(firstFragment).commit();
                buscar.setChecked(false);

            }

        });
        dialogo3.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogo1,int id){


            }

        });
        dialogo3.show();



    }

    public void stateListFragment(boolean state){
        layout = (FrameLayout) findViewById(R.id.firts_fragment_container);
        if(state){

            layout.setVisibility(View.VISIBLE);
            //cita_form.setVisibility(View.GONE);



        }else{
            layout.setVisibility(View.GONE);
            //getSupportFragmentManager().beginTransaction().remove(firstFragment).commit();
            lineascamposdatousuario.setVisibility(View.VISIBLE);
            buscar.setChecked(false);
        }


    }

    private void cargarHorasCitas() {
        if (findViewById(R.id.firts_fragment_container) != null) {
            firstFragment = new HeadlinesFragment();
            Bundle args = new Bundle();
            args.putString(HeadlinesFragment.NOMBREFECHA, fecha.getText().toString());
            Log.i("fecha", fecha.getText().toString());
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
            args.putString(HeadlinesFragment.PROFESIONAL, profesionales.get(profesional.getSelectedItemPosition()).CODIGO);



            firstFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.firts_fragment_container, firstFragment).commit();

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

    public void perfilUsuario(Login success){

                documento.setText(success.getNum());
                switch (success.gettipo()){
                    case "1":
                        tipo.setText(R.string.lista_cedula);
                        break;
                    case "2":
                        tipo.setText(R.string.lista_tarjeta);
                        break;
                    case "3":
                        tipo.setText(R.string.lista_extranjeria);
                        break;
                    case "4":
                        tipo.setText(R.string.lista_civil);
                        break;
                    case "5":
                        tipo.setText(R.string.lista_pasaporte);
                    default:
                        break;
                }
                nombre1.setText(success.getNombre1());
                nombre2.setText(success.getNombre2());
                apellido1.setText(success.getApellido1());
                apellido2.setText(success.getApellido2());
                lineascamposdatousuario.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
        //outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
        //outState.putParcelable(DIALOGDATEPICKER,DatePickerDialog);
        outState.putInt(VISIBILITY_CITAFORM,cita_form.getVisibility());
        outState.putInt(VISIBILITY_EMAILLOGINFORM,email_login_form.getVisibility());
        outState.putInt(VISIBILITY_FRAGMENT, layout.getVisibility());
        outState.putInt(CURRENT_TAB,tabs.getCurrentTab());
        outState.putBoolean(CHEKED_TOGGLE,buscar.isChecked());
        outState.putString(DOCUMENTO,documento.getText().toString());
        outState.putString(TIPODOCUMENTO, tipo.getText().toString());
        outState.putString(NOMBRE_UNO,nombre1.getText().toString());
        outState.putString(NOMBRE_DOS,nombre2.getText().toString());
        outState.putString(APELLIDO_UNO,apellido1.getText().toString());
        outState.putString(APELLIDO_DOS,apellido2.getText().toString());
        outState.putString(FECHA,fecha.getText().toString());


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       /* mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        if(mImageBitmap!=null){
            mImageView.setImageBitmap(mImageBitmap);
            mImageView.setVisibility(
                    savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                            ImageView.VISIBLE : ImageView.INVISIBLE
            );

        }
        */
        cita_form.setVisibility(savedInstanceState.getInt(VISIBILITY_CITAFORM));
        email_login_form.setVisibility(savedInstanceState.getInt(VISIBILITY_EMAILLOGINFORM));
        layout.setVisibility(savedInstanceState.getInt(VISIBILITY_FRAGMENT));
        tabs.setCurrentTab(savedInstanceState.getInt(CURRENT_TAB));
        documento.setText(savedInstanceState.getString(DOCUMENTO));
        tipo.setText(savedInstanceState.getString(TIPODOCUMENTO));
        nombre1.setText(savedInstanceState.getString(NOMBRE_UNO));
        nombre2.setText(savedInstanceState.getString(NOMBRE_DOS));
        apellido1.setText(savedInstanceState.getString(APELLIDO_UNO));
        apellido2.setText(savedInstanceState.getString(APELLIDO_DOS));
        fecha.setText(savedInstanceState.getString(FECHA));
        buscar.setChecked(savedInstanceState.getBoolean(CHEKED_TOGGLE));
    }

    public void mostrarDatepicker(View v){

        DatePickerDialog fechadialog = new DatePickerDialog(Principal.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //Toast.makeText(Principal.this, String.valueOf(convertir(view.getYear()))+String.valueOf(convertir(view.getMonth()+1))+String.valueOf(convertir(view.getDayOfMonth()+1)),Toast.LENGTH_LONG).show();
                String valorfecha = String.valueOf(convertir(view.getYear())) + "-" + String.valueOf(convertir(view.getMonth() + 1)) + "-" + String.valueOf(convertir(view.getDayOfMonth()));
                fecha.setText(valorfecha);
            }
        }, Integer.parseInt(obtenerFechaSistema("yyyy")), Integer.parseInt(obtenerFechaSistema("MM")) - 1, Integer.parseInt(obtenerFechaSistema("dd")));
        switch (Build.VERSION.SDK_INT){
            case Build.VERSION_CODES.LOLLIPOP:
                fechadialog.setIcon(getApplicationContext().getDrawable(R.drawable.ic_event_white_48dp));
                break;
            case Build.VERSION_CODES.KITKAT:
                fechadialog.setIcon(getResources().getDrawable(R.drawable.ic_event_white_48dp));
                break;


        }

        fechadialog.show();

    }

    private String obtenerFechaSistema(String formato){

        Locale l = new Locale("es","CO");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Colombia/Bogota"),l);
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat(formato, l);
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private class buscarProfesional extends AsyncTask<String, Void, List<Profesional>>{

        protected List<Profesional> doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            String nombre = null;
            String codigo = null;
            profesionales = new ArrayList<Profesional>();
            HttpURLConnection conn = null;
            Profesional response = null;

            try {
                URL login = new URL(params[2]);
                Log.i("error", params[2]);
                conn = (HttpURLConnection) login.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("fecha", params[0])
                        .appendQueryParameter("centro", params[1]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                int statusCode = conn.getResponseCode();
                if (statusCode == 200) {
                    InputStream respuesta = new BufferedInputStream(conn.getInputStream());
                    JsonReader reader = new JsonReader(new InputStreamReader(respuesta, "UTF-8"));
                    reader.beginArray();
                    while (reader.hasNext()) {

                        reader.beginObject();
                        while (reader.hasNext()) {
                            String name = reader.nextName();

                            switch (name) {
                                case "NOMBRE":
                                    nombre = reader.nextString();
                                    Log.i("nombre", nombre);
                                    break;
                                case "CODIGO":
                                    codigo = reader.nextString();
                                    Log.i("codigo", codigo);

                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }
                        }
                        reader.endObject();

                        profesionales.add(new Profesional(nombre,codigo));

                    }
                    reader.endArray();
                } else {
                    Log.i("error", String.valueOf(statusCode));
                    profesionales = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                profesionales = null;
            } finally {
                if (conn != null) conn.disconnect();
            }
            return profesionales;
        }

        @Override
        protected void onPostExecute(List<Profesional> profesionales) {
            super.onPostExecute(profesionales);
            if(profesionales !=null){
                String[] nombreArrayList = new String[profesionales.size()];
                for (int i=0;i<profesionales.size();i++){
                    nombreArrayList[i]=profesionales.get(i).NOMBRE;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, nombreArrayList);

                adapter.setDropDownViewResource(R.layout.spinner_view_item);

                profesional.setAdapter(adapter);

            }else{
                Toast.makeText(Principal.this,"Nada de nada", Toast.LENGTH_LONG).show();


            }

        }
    }

    private class UserLoginTask extends AsyncTask<String, Void, Login> {


        @Override
        protected Login doInBackground(String... parametros) {
            // TODO: attempt authentication against a network service.

            String tipo = null;
            String documento = null;
            String nombre1 = null;
            String nombre2 = null;
            String apellido1 = null;
            String apellido2 = null;

            HttpURLConnection conn = null;
            Login response = null;

            try {
                URL login = new URL(parametros[2]);
                Log.i("error", parametros[2]);
                conn = (HttpURLConnection) login.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("tipo", parametros[0])
                        .appendQueryParameter("documento", parametros[1]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                int statusCode = conn.getResponseCode();
                if (statusCode == 200) {
                    InputStream respuesta = new BufferedInputStream(conn.getInputStream());
                    JsonReader reader = new JsonReader(new InputStreamReader(respuesta, "UTF-8"));
                    reader.beginObject();

                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        //Log.i("mensaje",name);

                        switch (name) {
                            case "TIPDOCUM":
                                tipo = reader.nextString();
                                break;
                            case "NUMDOCUM":
                                documento = reader.nextString();
                                break;
                            case "NOMBRE1":
                                nombre1 = reader.nextString();
                                break;
                            case "NOMBRE2":
                                nombre2 = reader.nextString();
                                break;
                            case "APELLIDO1":
                                apellido1 = reader.nextString();
                                break;
                            case "APELLIDO2":
                                apellido2 = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }

                    }
                    reader.endObject();
                    reader.close();
                    response = new Login(tipo, documento, nombre1, nombre2, apellido1, apellido2);


                } else {
                    Log.i("error", String.valueOf(statusCode));
                    response = null;

                }
            } catch (IOException e) {
                e.printStackTrace();
                response = null;
            } finally {
                if (conn != null) conn.disconnect();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Login success) {
            mAuthTask = null;
            showProgress(false);

            if (success != null) {

                perfilUsuario(success);
                cita_form.setVisibility(View.VISIBLE);
                email_login_form.setVisibility(View.GONE);


            }
            else{
                Toast.makeText(Principal.this,"Usuario no encontrado", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private class ReservaCitaTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {


            HttpURLConnection conn = null;
            String resultado = null;


            try {
                URL login = new URL(params[0]);
                Log.i("error", params[0]);
                conn = (HttpURLConnection) login.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("TIPO", params[1])
                        .appendQueryParameter("DOCUMENTO", params[2])
                        .appendQueryParameter("FECHA", params[3])
                        .appendQueryParameter("CENTROPRODUCCION", params[4])
                        .appendQueryParameter("PRIMERNOMBRE", params[5])
                        .appendQueryParameter("SEGUNDONOMBRE", params[6])
                        .appendQueryParameter("PRIMERAPELLIDO", params[7])
                        .appendQueryParameter("SEGUNDOAPELLIDO", params[8])
                        .appendQueryParameter("CODIGO", params[9]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                int statusCode = conn.getResponseCode();
                if (statusCode == 200) {
                    InputStream respuesta = new BufferedInputStream(conn.getInputStream());
                    JsonReader reader = new JsonReader(new InputStreamReader(respuesta, "UTF-8"));
                    reader.beginObject();

                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        Log.i("mensaje",name);

                        switch (name) {
                            case "mensaje":
                                resultado = reader.nextString();
                                break;

                            default:
                                reader.skipValue();
                                break;
                        }

                    }
                    reader.endObject();
                    reader.close();


                } else {
                    Log.i("error", String.valueOf(statusCode));
                    resultado = null;

                }
            } catch (IOException e) {
                e.printStackTrace();
                resultado = null;
            } finally {
                if (conn != null) conn.disconnect();
            }
            return resultado;

        }
        protected void onPostExecute(String mensaje){
            //Log.i("MENSAJE", mensaje);
            Toast.makeText(getApplication().getBaseContext(),"Cita asignada :"+mensaje, Toast.LENGTH_LONG).show();
            /*if (mensaje.compareTo("actualizado")==1) {


            }
            else{
                Toast.makeText(getApplication().getBaseContext(),"No se reservo la cita", Toast.LENGTH_LONG).show();
            }*/

        }
    }





    public void tomarFoto(View v) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(R.string.Important);
        dialogo1.setMessage(R.string.CloseMessage);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

            }
        });
        dialogo1.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
            }
        });
        dialogo1.show();


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        //Toast.makeText(Principal.this, JPEG_FILE_PREFIX + timeStamp + "_"+getAlbumDir().toString(),Toast.LENGTH_LONG).show();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);

        mImageView.setVisibility(View.VISIBLE);

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        LoginSQLiteHelper Ldbh = new LoginSQLiteHelper(this);
        db = Ldbh.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHOTO_ROUTE, contentUri.getEncodedPath());
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN + " = 1";
        mCurrentPhotoPath = Uri.fromFile(f).getEncodedPath();
        //String[] selectionArgs = {1};
        db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, null);
        long num = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, null);
        Log.i("actualizar_ruta",String.valueOf(num));
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void llenarUsuariosLogueados(){
        LoginSQLiteHelper usdbh = new LoginSQLiteHelper(this);

        db = usdbh.getWritableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIPO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DOCUMENTO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERNOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDONOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERAPELLIDO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDOAPELLIDO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PHOTO_ROUTE
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry._ID + " DESC";

        Cursor c = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

        //Recorremos los resultados para mostrarlos en pantalla
        usuario.setText("");
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                String cod = c.getString(0);
                String til = c.getString(1);
                String sub = c.getString(2);
                String a = c.getString(3);
                String b = c.getString(4);
                String ca = c.getString(5);
                String d = c.getString(6);
                String e = c.getString(7);
                String f = c.getString(8);

                usuario.append(" " + cod + " " + til + " " +sub+" " + a +" " + b +" " +ca+" " + d + "  " + e + " " +f+"\n");
            } while(c.moveToNext());
					/*Cursor c = db.query(
							FeedEntry.TABLE_NAME,  // The table to query
							projection,                               // The columns to return
							selection,                                // The columns for the WHERE clause
							selectionArgs,                            // The values for the WHERE clause
							null,                                     // don't group the rows
							null,                                     // don't filter by row groups
							sortOrder                                 // The sort order
					);*/
        }
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    LoginSQLiteHelper Ldbh = new LoginSQLiteHelper(this);
                    db = Ldbh.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    valores.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PHOTO_ROUTE, Uri.fromFile(f).getEncodedPath());
                    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN + " = 1";
                    mCurrentPhotoPath = Uri.fromFile(f).getEncodedPath();
                    long num = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, null);
                    Log.i("actualizar_ruta",String.valueOf(num));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S



        } // switch
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(View.VISIBLE);


    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }



}
