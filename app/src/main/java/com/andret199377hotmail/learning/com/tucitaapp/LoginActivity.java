package com.andret199377hotmail.learning.com.tucitaapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;

    // UI references.
    private Spinner mTipoDocumentoView;
    private EditText mDocumentoView;
    private View mProgressView;
    private View mLoginFormView;

    SQLiteDatabase db;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mTipoDocumentoView = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.TipoDocumento,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTipoDocumentoView.setAdapter(adapter);


        mDocumentoView = (EditText) findViewById(R.id.documento);
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

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
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
                "Login Page", // TODO: Define a title for the content shown.
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
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
                //Toast.makeText(LoginActivity.this,success.TIPDOCUM.concat(success.NUMDOCUM).concat(success.APELLIDO1).concat(success.NOMBRE1),Toast.LENGTH_LONG).show();
                LoginSQLiteHelper usdbh = new LoginSQLiteHelper(LoginActivity.this);

                db = usdbh.getWritableDatabase();
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put(FeedReaderContract.FeedEntry._ID, success.gettipo() + success.getNum());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIPO, success.gettipo());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DOCUMENTO, success.getNum());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERNOMBRE, success.getNombre1());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDONOMBRE, success.getNombre2());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_PRIMERAPELLIDO, success.getApellido1());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SEGUNDOAPELLIDO, success.getApellido2());
                nuevoRegistro.put(FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN, 1);
                long newRowid;
                newRowid = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, nuevoRegistro);
                Log.i("Hola", String.valueOf(newRowid));
                registrar();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Usuario no encontrado o puede haberse perdido conexion con el servidor", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void registrar() {
        Intent actividad = new Intent(this, Principal.class);
        startActivity(actividad);
    }
    /*
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
*/
}


