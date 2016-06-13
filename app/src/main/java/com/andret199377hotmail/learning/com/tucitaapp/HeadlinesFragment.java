/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andret199377hotmail.learning.com.tucitaapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HeadlinesFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;
    final static String NOMBREFECHA = "FECHA";
    final static String CENTROPRODUCCION = "CENTROPRODUCCION";
    List<String> fechas = null;
    List<Cita> citas = new ArrayList<Cita>();
    String fecha;
    String centroproduccion;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by HeadlinesFragment when a list item is selected */
        void onArticleSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb


        // Create an array adapter for the list view, using the Ipsum headlines array
        if (savedInstanceState == null) {
            Bundle extras = getArguments();
            if (extras == null) {
                fecha = null;
                centroproduccion = null;
            } else {
                fecha = extras.getString(NOMBREFECHA);
                centroproduccion = extras.getString(CENTROPRODUCCION);
                Log.i("valores",fecha.toString().concat(" "+centroproduccion));
            }
        }

        String urlcita = "http://186.170.16.38/api/citas/cita.php";
        new CitasHoraTask().execute(fecha, centroproduccion, urlcita);

    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);
        Toast.makeText(this.getActivity(),String.valueOf(citas.get(position).getFecha()), Toast.LENGTH_SHORT).show();
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);




    }

    private class CitasHoraTask extends AsyncTask<String, Void, List<Cita>>{

        @Override
        protected List<Cita> doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            String fecha = null;
            String centroproduccion = null;
            String ips = null;
            int tipo_solicitud = 0;
            int estado = 0;



            HttpURLConnection conn = null;
            Cita response = null;

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
                        .appendQueryParameter("FECHA", params[0])
                        .appendQueryParameter("CENTROPRODUCCION", params[1]);
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

                            if (name.equals("FECHA")) {
                                fecha = reader.nextString();
                                Log.i("fecha",fecha);
                            } else if (name.equals("CENTROPROD")) {
                                centroproduccion = reader.nextString();
                                Log.i("fecha",centroproduccion);
                            } else if (name.equals("IPS")) {
                                ips = reader.nextString();
                                Log.i("fecha",ips);
                            } else if (name.equals("TIPO_SOLICITUD")) {
                                tipo_solicitud = reader.nextInt();
                                Log.i("fecha",String.valueOf(tipo_solicitud));
                            } else if (name.equals("ESTADO")){
                                estado = reader.nextInt();
                                Log.i("fecha",String.valueOf(estado));
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                        citas.add(new Cita(fecha, centroproduccion, ips, tipo_solicitud, estado ));
                    }
                    reader.endArray();
                    //return citas;



                } else {
                    Log.i("error", String.valueOf(statusCode));

                    citas = null;

                }
            } catch (IOException e) {
                e.printStackTrace();
                citas = null;
            } finally {
                if (conn != null) conn.disconnect();
            }
            return citas;
        }

        @Override
        protected void onPostExecute(List<Cita> citas) {
            super.onPostExecute(citas);
            if(citas !=null){
                String[] nombreArrayList = new String[citas.size()];
                for (int i=0;i<citas.size();i++){
                    nombreArrayList[i]=citas.get(i).FECHA;
                }
                setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,nombreArrayList));


            }else{
               Toast.makeText(getContext(),"No se encontraron resultados", Toast.LENGTH_LONG).show();
            }

        }
    }


    public List readCitaArray(JsonReader reader) throws IOException {

        reader.beginArray();
        while (reader.hasNext()) {
            citas.add(readCita(reader));
        }
        reader.endArray();
        return citas;
    }

    public Cita readCita(JsonReader reader) throws IOException {
        String fecha = null;
        String centroproduccion = null;
        String ips = null;
        int tipo_solicitud = Integer.parseInt(null);
        int estado = Integer.parseInt(null);
        Cita cita = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("FECHA")) {
                fecha = reader.nextString();
                Log.i("fecha",fecha);
            } else if (name.equals("CENTROPROD")) {
                centroproduccion = reader.nextString();
                Log.i("fecha",centroproduccion);
            } else if (name.equals("IPS")) {
                ips = reader.nextString();
                Log.i("fecha",ips);
            } else if (name.equals("TIPO_SOLICITUD")) {
                tipo_solicitud = reader.nextInt();
                Log.i("fecha",String.valueOf(tipo_solicitud));
            } else if (name.equals("ESTADO")){
                estado = reader.nextInt();
                Log.i("fecha",String.valueOf(estado));
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Cita(fecha, centroproduccion, ips, tipo_solicitud, estado );

    }

    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }
    /*
    public User readUser(JsonReader reader) throws IOException {
        String username = null;
        int followersCount = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                username = reader.nextString();
            } else if (name.equals("followers_count")) {
                followersCount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new User(username, followersCount);
    }*/
}