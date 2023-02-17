package com.example.pm1e16341.transacciones;

public class Pais {
    private String codigo, nombrePais;
    public Pais() {
    }

    public Pais(String codigo, String nombrePais) {
        this.codigo = codigo;
        this.nombrePais = nombrePais;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
