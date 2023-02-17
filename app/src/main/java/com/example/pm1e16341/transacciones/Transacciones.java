package com.example.pm1e16341.transacciones;

public class Transacciones {
    //base de datos
    public static final String NameDatabase = "PM01DB";

    //Tb paises
    public static String tblPaises = "paises";
    public static final String codigo ="codigo";
    public static final String p_pais="pais";

    public static final String CreateTablePaises = "CREATE TABLE " + tblPaises + "(codigo INTEGER PRIMARY KEY,"+"pais TEXT )";
    public static final String DropTablePaises = "DROP TABLE " + tblPaises;

    //Tb contactos
    public static final String tablacontactos = "contactos";
    public static final String id = "id";
    public static final String nombreCompleto = "nombreCompleto";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String foto = "foto";
    public static final String pais = "pais";


    public static final String createTableContact = "CREATE TABLE " + tablacontactos +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombreCompleto TEXT, telefono INTEGER, nota TEXT, foto BLOB, pais TEXT)";

    public static final String dropTableContact = "DROP TABLE IF EXIST" + tablacontactos;
}
