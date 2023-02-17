package com.example.pm1e16341;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pm1e16341.configuraciones.SQLiteConexion;
import com.example.pm1e16341.transacciones.Transacciones;

import java.io.ByteArrayInputStream;

public class ActivityVerFoto extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
    ImageView imageViewFoto;
    Button btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_foto);

        btnAtras = (Button) findViewById(R.id.btnAtras);

        imageViewFoto = (ImageView) findViewById(R.id.foto);

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);
            }
        });


        Bitmap recuperarFoto = buscarImagen(getIntent().getStringExtra("codigoParaFoto"));
        imageViewFoto.setImageBitmap(recuperarFoto);


    }

    public Bitmap buscarImagen(String id) {
        SQLiteDatabase db = conexion.getWritableDatabase();

        String sql = "SELECT foto FROM contactos WHERE id =" + id;
        Cursor cursor = db.rawQuery(sql, new String[] {});
        Bitmap bitmap = null;
        if(cursor.moveToFirst()){
            byte[] blob = cursor.getBlob(0);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(bais);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return bitmap;
    }
}