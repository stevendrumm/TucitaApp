package com.andret199377hotmail.learning.com.tucitaapp;

/**
 * Created by stevendrumm on 1/06/16.
 */
public class Login {

        private int id;

        private int tipodocumento;

        private int numerodocumento;

        private String nombre1;

        private String nombre2;

        private String apellido1;

        private String apellido2;




    public Login(int id, int tipodocumento, int numerodocumento, String nombre1, String nombre2, String apellido1, String apellido2) {
        this.id = id;
        this.tipodocumento = tipodocumento;
        this.numerodocumento = numerodocumento;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;

    }

    // Aquí los métodos get

    public int getId(){
        return this.id;

    }
    public int gettipo(){
        return this.tipodocumento;
    }
    public int getNum(){
        return this.numerodocumento;
    }
    public String getNombre1(){
        return this.nombre1;
    }
    public String getNombre2(){
        return this.nombre2;
    }
    public String getApellido1(){
        return this.apellido1;
    }
    public String getApellido2(){
        return this.apellido1;
    }

}


