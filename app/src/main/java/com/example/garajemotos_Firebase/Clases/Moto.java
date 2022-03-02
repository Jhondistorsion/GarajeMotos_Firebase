package com.example.garajemotos_Firebase.Clases;


import java.io.Serializable;
import java.util.Objects;

public class Moto implements Serializable {

    private String matricula, marca, modelo;
    private int anio;
    private boolean foto;

    public Moto(String matricula, String marca, String modelo, int anio, boolean foto) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.foto = foto;
    }

    public Moto(String matricula, String marca, String modelo, int anio) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.foto = false;
    }

    public Moto() {
        this.matricula = "";
        this.marca = "";
        this.modelo = "";
        this.anio = 0;
        this.foto = false;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public boolean getFoto() {
        return foto;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return this.matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moto moto = (Moto) o;
        return Objects.equals(matricula, moto.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricula);
    }
}
