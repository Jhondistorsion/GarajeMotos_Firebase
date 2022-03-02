package com.example.garajemotos_Firebase.Clases;

import java.io.Serializable;

public class Cliente implements Serializable {

    private String nombre, direccion, moto;
    private String idCliente;

    public Cliente(String nombre, String direccion, String moto) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.moto = moto;
    }

    public Cliente(String nombre, String direccion, String moto, String idCliente) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.moto = moto;
        this.idCliente = idCliente;
    }

    public Cliente() {
        this.nombre = "";
        this.direccion = "";
        this.moto = "";
        this.idCliente = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
}
