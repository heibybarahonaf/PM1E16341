package com.example.pm1e16341;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1e16341.configuraciones.SQLiteConexion;
import com.example.pm1e16341.transacciones.Transacciones;

public class ActivityPais extends AppCompatActivity {

    EditText Codigo, Pais;
    Button btnGuardar, btnAtras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        Codigo = (EditText) findViewById(R.id.codigo);
        Pais = (EditText) findViewById(R.id.nombre);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnAtras = (Button) findViewById(R.id.btnAtras);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertarPais();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void InsertarPais() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.codigo, Codigo.getText().toString());
        valores.put(Transacciones.p_pais, Pais.getText().toString());

        Long resultado = db.insert(Transacciones.tblPaises,Transacciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registrado Exitosamente",Toast.LENGTH_LONG).show();
        db.close();

        limpiarPantalla();

    }

    private void limpiarPantalla() {
        Pais.setText("");
        Codigo.setText("");
    }
}