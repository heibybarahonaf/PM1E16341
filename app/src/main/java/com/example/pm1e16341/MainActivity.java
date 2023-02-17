package com.example.pm1e16341;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e16341.configuraciones.SQLiteConexion;
import com.example.pm1e16341.transacciones.Pais;
import com.example.pm1e16341.transacciones.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
    SQLiteDatabase db;

    EditText nombre, telefono, nota;
    Spinner spPais;
    ImageView foto;
    Button btnTomarFoto;

    static final int PETICION_ACCESO_CAM = 201;
    static final int TAKE_PIC_REQUEST = 202;
    Bitmap imagen;

    ArrayList<String> listaPaises;
    ArrayList<Pais> lista;

    int codPaisSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (EditText) findViewById(R.id.Nombre);
        telefono = (EditText) findViewById(R.id.Telefono);
        nota = (EditText) findViewById(R.id.Nota);
        spPais = (Spinner)findViewById(R.id.cmbPais);
        foto = (ImageView) findViewById(R.id.Foto);

        Button btnGuardarContacto= (Button) findViewById(R.id.btnGuardar);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
        Button btnListaContactos = (Button)findViewById(R.id.btnListaContactos);
        FloatingActionButton btnPais = (FloatingActionButton) findViewById(R.id.floatAgregarPais);

        btnPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),ActivityPais.class);
                startActivity(intent);

            }
        });

        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityListaContactos.class);
                startActivity(intent);
            }
        });

        btnGuardarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validarDatos();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Tomar fotografia ",Toast.LENGTH_LONG).show();
                }

            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        ObtenerListaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPaises);
        spPais.setAdapter(adp);

        spPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String cadena = adapterView.getSelectedItem().toString();

                codPaisSelec = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),10);

    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }



    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }
    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requescode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requescode, resultCode, data);

        if(requescode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            foto.setImageBitmap(imagen);
        }else if (resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            foto.setImageURI(imageUri);


        }

    }

    private void validarDatos() {
        if (listaPaises.size() == 0){
            Toast.makeText(getApplicationContext(), "Ingrese o registre un Pais" ,Toast.LENGTH_LONG).show();
        }else  if (nombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Ingrese nombre" ,Toast.LENGTH_LONG).show();
        }else if (telefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Ingrese telefono" ,Toast.LENGTH_LONG).show();
        }else if (nota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Ingrese una nota" ,Toast.LENGTH_LONG).show();
        }else{
            guardarContacto(imagen);
        }
    }

    private void guardarContacto(Bitmap bitmap) {
        db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(Transacciones.nombreCompleto, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        valores.put(Transacciones.foto,ArrayFoto);
        valores.put(Transacciones.pais, codPaisSelec);


        Long resultado = db.insert(Transacciones.tablacontactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Ingresado correctamente",Toast.LENGTH_LONG).show();

        db.close();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void ObtenerListaPaises() {
        Pais pais = null;
        lista = new ArrayList<Pais>();
        db = conexion.getReadableDatabase();

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
        listaPaises = new ArrayList<String>();

        for (int i=0; i<lista.size();i++)
        {
            listaPaises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }
}