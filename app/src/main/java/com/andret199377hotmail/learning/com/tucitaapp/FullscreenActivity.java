package com.andret199377hotmail.learning.com.tucitaapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    SQLiteDatabase db;
    private View mProgressView;
    public static int MILISEGUNDOS_ESPERA = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        mProgressView = findViewById(R.id.login_progress);
        showProgress(true);
        esperarYCerrar(MILISEGUNDOS_ESPERA);



    }
    public void esperarYCerrar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                if (consultarusuario()) {
                    showProgress(false);
                    Intent actividad = new Intent(FullscreenActivity.this, Principal.class);
                    startActivity(actividad);
                } else {
                    showProgress(false);
                    Intent actividad = new Intent(FullscreenActivity.this, LoginActivity.class);
                    startActivity(actividad);
                }
            }
        }, milisegundos);
    }

    private boolean consultarusuario() {
        LoginSQLiteHelper usdbh = new LoginSQLiteHelper(this);
        db = usdbh.getWritableDatabase();

        boolean logueado = false;
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_STATE_LOGIN

        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry._ID + " DESC";

        Cursor c = db.query(FeedReaderContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

        //Recorremos los resultados para mostrarlos en pantalla
        if(c!=null){
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya m�s registros
                do {
                    int cod = c.getInt(1);
                    if(cod == 1){
                        logueado = true;
                    }



                } while (c.moveToNext()&&!logueado);

            }


        }
        return logueado;

    }
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);



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

        }
    }
}
