package com.andret199377hotmail.learning.com.tucitaapp;

/**
 * Created by stevendrumm on 6/07/16.
 */
public class Profesional {
    public String NOMBRE;
    public String CODIGO;

    public Profesional(String nombre, String codigo) {
        this.NOMBRE = nombre;
        this.CODIGO = codigo;


    }
    public String getNombre(){
        return this.NOMBRE;
    }
    public String getCodigo(){
        return this.CODIGO;
    }
}
