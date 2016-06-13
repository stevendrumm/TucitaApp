package com.andret199377hotmail.learning.com.tucitaapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stevendrumm on 9/06/16.
 */
public class Cita {

    public String FECHA;
    public String CENTROPRODUCCION;
    public String IPS;
    public int TIPO_SOLICITUD;
    public int ESTADO;



    public Cita(String fecha, String centroproduccion, String ips, int tipo_solicitud, int estado) {
        this.FECHA = fecha;
        this.CENTROPRODUCCION = centroproduccion;
        this.IPS = ips;
        this.TIPO_SOLICITUD = tipo_solicitud;
        this.ESTADO = estado;

    }
    public String getFecha(){
        return this.FECHA;
    }
    public String getCentroProduccion(){
        return this.CENTROPRODUCCION;
    }
    public String getIps(){
        return this.IPS;
    }
    public int getTipoSolicitud(){
        return this.TIPO_SOLICITUD;
    }
    public int getEstado(){
        return this.ESTADO;
    }

}
