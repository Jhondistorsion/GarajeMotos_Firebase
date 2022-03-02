package com.example.garajemotos_Firebase.Activities;

import static com.example.garajemotos_Firebase.Utilidades.ImagenesBlobBitmap.bytes_to_bitmap;
import static com.example.garajemotos_Firebase.Utilidades.ImagenesBlobBitmap.decodeSampledBitmapFrombyteArray;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.garajemotos_Firebase.Clases.Configuracion;
import com.example.garajemotos_Firebase.Clases.Moto;
import com.example.garajemotos_Firebase.Controladores.MotoFirebaseController;
import com.example.garajemotos_Firebase.Utilidades.ImagenesFirebase;

import java.util.List;

import garajemotos_Firebase.R;

public class Activity_Mostrar_Detalles_Moto extends AppCompatActivity {

    public static final String EXTRA_MODO = "miModo";
    public static final String EXTRA_MOTO = "motoDetalles";
    public static final String EXTRA_FOTO = "fotoDetalles";

    TextView txt_matricula, txt_marca, txt_modelo, txt_anio;
    ImageView img_moto;

    private String modo;
    Moto m;
    byte[] fotob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalles_moto);

        txt_matricula = (TextView) findViewById(R.id.txt_id_cliente);
        txt_marca = (TextView) findViewById(R.id.txt_nombre);
        txt_modelo = (TextView) findViewById(R.id.txt_direccion);
        txt_anio = (TextView) findViewById(R.id.txt_moto_cliente);
        img_moto = (ImageView) findViewById(R.id.img_cliente);

        Intent intent = getIntent();
        if (intent != null) {

            m = (Moto) intent.getSerializableExtra(MotoViewHolder.EXTRA_OBJETO_MOTO);
            txt_matricula.setText(m.getMatricula());
            txt_marca.setText(m.getMarca());
            txt_modelo.setText(m.getModelo());
            txt_anio.setText(String.valueOf(m.getAnio()));

            if (m.getFoto()) {
                new ImagenesFirebase().descargarFoto(new ImagenesFirebase.FotoStatus() {
                    @Override
                    public void FotoIsDownload(byte[] bytes) {
                        if(bytes != null) {
                            Log.i("firebasedb","foto descargada correctamente");
                            Bitmap fotob = decodeSampledBitmapFrombyteArray(bytes, Configuracion.ALTO_IMAGENES_BITMAP, Configuracion.ANCHO_IMAGENES_BITMAP);
                            img_moto.setImageBitmap(fotob);
                        }
                        else{
                            Log.i("firebasedb","foto no descargada correctamente");
                        }
                    }
                    @Override
                    public void FotoIsUpload() {
                    }
                    @Override
                    public void FotoIsDelete() {
                    }
                },"motos/"+m.getMatricula()+".png");
            }

        }
    }

    public void volver(View view) {
        Intent intent = new Intent(this, Activity_Mostrar_Moto.class);
        startActivity(intent);
    }

    public void atras() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void editar(View view) {
        Intent intent = new Intent(this, Activity_Nueva_Modifica_Moto.class);
        modo = "modificar";
        intent.putExtra(EXTRA_MODO, modo);
        intent.putExtra(EXTRA_MOTO, m);
        intent.putExtra(EXTRA_FOTO, fotob);
        startActivity(intent);
    }

    public void eliminar(View view) {
        AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
        alerta1.setTitle("Deseas eliminar la moto?");
        alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new MotoFirebaseController().borrarMoto(new MotoFirebaseController.MotoStatus() {
                    @Override
                    public void motoIsLoaded(List<Moto> motos, List<String> keys) {

                    }

                    @Override
                    public void motoIsAdd() {

                    }

                    @Override
                    public void motoIsUpdate() {

                    }

                    @Override
                    public void motoIsDelete() {
                        // aquí hay que poner cuando se haya borrado bien qué hacer
                        Toast.makeText(Activity_Mostrar_Detalles_Moto.this, "borrado correcto", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, m.getMatricula());
                new ImagenesFirebase().borrarFoto(new ImagenesFirebase.FotoStatus() {
                    @Override
                    public void FotoIsDownload(byte[] bytes) {
                    }
                    @Override
                    public void FotoIsDelete() {
                    }
                    @Override
                    public void FotoIsUpload() {
                        Toast.makeText(Activity_Mostrar_Detalles_Moto.this, "foto eliminada correctamente", Toast.LENGTH_LONG).show();
                    }
                },"motos/"+m.getMatricula()+".png");
                atras();
            }
        });
        alerta1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarToast("Operación cancelada");
            }
        });
        alerta1.show();
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}