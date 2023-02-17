package com.example.pm1e16341;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e16341.configuraciones.SQLiteConexion;
import com.example.pm1e16341.transacciones.Pais;
import com.example.pm1e16341.transacciones.Transacciones;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityActualizar extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
    Button btnatras,btnEditar;
    EditText codigo, nombre, telefono, nota;
    Spinner codPais;
    ArrayList<String> lista_paises;
    ArrayList<Pais> lista;

    int codigoPaisSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);
        btnatras = (Button) findViewById(R.id.btnatras);
        codigo = (EditText)findViewById(R.id.txtActCodigo);
        nombre = (EditText)findViewById(R.id.Nombre);
        telefono = (EditText)findViewById(R.id.Telefono);
        nota = (EditText)findViewById(R.id.Nota);
        codPais = (Spinner)findViewById(R.id.cmbPais);
        btnEditar = (Button) findViewById(R.id.btnActualizar);

        codigo.setText(getIntent().getStringExtra("codigo"));
        nombre.setText(getIntent().getStringExtra("nombreContacto"));
        telefono.setText(getIntent().getStringExtra("numeroContacto"));
        nota.setText(getIntent().getStringExtra("notaContacto"));

        ObtenerListaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        codPais.setAdapter(adp);

        codPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String cadena = adapterView.getSelectedItem().toString();

                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListaContactos.class);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editar();
            }
        });
    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }


    private void ObtenerListaPaises() {
        Pais pais = null;
        lista = new ArrayList<Pais>();
        SQLiteDatabase db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tblPaises,null);

        while (cursor.moveToNext())
        {
            pais = new Pais();

            pais.setCodigo(cursor.getString(0));
            pais.setNombrePais(cursor.getString(1));

            lista.add(pais);
        }

        cursor.close();

        fillCombo();

    }

    private void fillCombo() {
        lista_paises = new ArrayList<String>();

        for (int i=0; i<lista.size();i++)
        {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }

    private void Editar() {
        SQLiteDatabase db = conexion.getWritableDatabase();

        String ObjCodigo = codigo.getText().toString();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.nombreCompleto, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        valores.put(Transacciones.pais, codigoPaisSeleccionado);

        try {
            db.update(Transacciones.tablacontactos,valores, Transacciones.id +" = "+ ObjCodigo, null);
            db.close();
            Toast.makeText(getApplicationContext(),"Actualizado correctamente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ActivityListaContactos.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No actualizado", Toast.LENGTH_SHORT).show();
        }

    }
}