package com.andret199377hotmail.learning.com.tucitaapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Principal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HeadlinesFragment.OnHeadlineSelectedListener {

    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    private SQLiteDatabase db;
    DatePicker fecha;
    Spinner centroproduccion;
    private View mProgressView;
    private View mCitaFormView;
    ToggleButton buscar;
    HeadlinesFragment firstFragment;
    ImageButton picbtn;
    TextView documento;
    TextView tipo;
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
        documento = (TextView) findViewById(R.id.documento);
        tipo = (TextView) findViewById(R.id.tipo);
        nombre1 = (TextView) findViewById(R.id.Nombre1);
        nombre2 = (TextView) findViewById(R.id.Nombre2);
        apellido1 = (TextView) findViewById(R.id.Apellido1);
        apellido2 = (TextView) findViewById(R.id.Apellido2);
        mImageView = (ImageView) findViewById(R.id.imagendeperfil);
        mImageBitmap = null;
        picbtn = (ImageButton) findViewById(R.id.btnIntend);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
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
        String mensaje = getString(R.string.CitaMessage);
        dialogo3.setMessage(mensaje+" "+itemAtPosition+"?" );//
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
                FeedReaderContract.FeedEntry.COLUMN_NAME_DOCUMENTO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIPO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERNOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDONOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERAPELLIDO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDOAPELLIDO,
                FeedReaderContract.FeedEntry.COLUMN_NAME_PHOTO_ROUTE
                };
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN+"=1";
        //String[] selectionArgs = new String[] {String.valueOf(1)};

        Cursor c = db.query(true,FeedReaderContract.FeedEntry.TABLE_NAME, campos, selection,null, null, null, null,null);

        if (c.moveToFirst()) {
            do {
                documento.setText(c.getString(0));
                switch (c.getString(1)){
                    case "1":
                        tipo.setText("Cedula de ciudadania");
                        break;
                    case "2":
                        tipo.setText("Tarjeta de identidad");
                        break;
                    case "3":
                        tipo.setText("Cedula de extranjeria");
                        break;
                    case "4":
                        tipo.setText("registro civil");
                        break;
                    default:
                        break;
                }
                nombre1.setText(c.getString(2));
                nombre2.setText(c.getString(3));
                apellido1.setText(c.getString(4));
                apellido2.setText(c.getString(5));
                mImageBitmap = BitmapFactory.decodeFile(c.getString(6));
                Log.i("ruta", c.getString(6));

		/* Associate the Bitmap to the ImageView */
                mImageView.setImageBitmap(mImageBitmap);

                mImageView.setVisibility(View.VISIBLE);



            } while (c.moveToNext());
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }

    }

    public void tomarFoto(View v) {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(R.string.Important);
        dialogo1.setMessage(R.string.CloseMessage);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
            }
        });
        dialogo1.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);

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
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
                    db.update(FeedReaderContract.FeedEntry.TABLE_NAME, valores, selection, null);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);

        outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);

        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(
                savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );

    }

}
