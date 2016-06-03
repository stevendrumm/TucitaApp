package com.andret199377hotmail.learning.com.tucitaapp;

/**
 * Created by stevendrumm on 1/06/16.
 */
public class Login {

        private int TIPDOCUM;

        private String NUMDOCUM;

        private String APELLIDO1;

        private String APELLIDO2;

        private String NOMBRE1;

        private String NOMBRE2;

    public Login() {

        this.TIPDOCUM = Integer.parseInt(null);
        this.NUMDOCUM = null;
        this.NOMBRE1 = null;
        this.NOMBRE2 = null;
        this.APELLIDO1 = null;
        this.APELLIDO2 = null;

    }


    public Login( int tipodocumento, String numerodocumento, String nombre1, String nombre2, String apellido1, String apellido2) {

        this.TIPDOCUM = tipodocumento;
        this.NUMDOCUM = numerodocumento;
        this.NOMBRE1 = nombre1;
        this.NOMBRE2 = nombre2;
        this.APELLIDO1 = apellido1;
        this.APELLIDO2 = apellido2;

    }

    // Aquí los métodos get


    public int gettipo(){
        return this.TIPDOCUM;
    }
    public String getNum(){
        return this.NUMDOCUM;
    }
    public String getNombre1(){
        return this.NOMBRE1;
    }
    public String getNombre2(){
        return this.NOMBRE2;
    }
    public String getApellido1(){
        return this.APELLIDO1;
    }
    public String getApellido2(){
        return this.APELLIDO2;
    }

}


