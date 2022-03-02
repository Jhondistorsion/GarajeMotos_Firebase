package com.example.garajemotos_Firebase.Activities;

import static com.example.garajemotos_Firebase.Utilidades.ImagenesBlobBitmap.decodeSampledBitmapFrombyteArray;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.garajemotos_Firebase.Clases.Configuracion;
import com.example.garajemotos_Firebase.Utilidades.ImagenesFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import garajemotos_Firebase.R;

public class MainActivity extends AppCompatActivity {

    private TextView txt_usuario;
    private ImageView avatar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_usuario = (TextView) findViewById(R.id.txt_usuario);
        avatar = (ImageView) findViewById(R.id.avatar);
        mAuth = FirebaseAuth.getInstance();
        mostrarUsuario();
    }

    private void mostrarUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            txt_usuario.setText(mAuth.getCurrentUser().getEmail());
            String fotoRef = mAuth.getCurrentUser().getEmail() + "/avatar.png";
            new ImagenesFirebase().descargarFoto(new ImagenesFirebase.FotoStatus() {
                @Override
                public void FotoIsDownload(byte[] bytes) {
                    if(bytes != null) {
                        Log.i("firebasedb","foto descargada correctamente");
                        Bitmap fotob = decodeSampledBitmapFrombyteArray(bytes, Configuracion.ALTO_IMAGENES_BITMAP, Configuracion.ANCHO_IMAGENES_BITMAP);
                        avatar.setImageBitmap(fotob);
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
            },fotoRef);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    public void nuevaMoto(View view) {
        Intent intent = new Intent(this,Activity_Nueva_Modifica_Moto.class);
        startActivity(intent);
    }

    public void nuevoCliente(View view) {
        Intent intent = new Intent(this,Activity_Nuevo_Modifica_Cliente.class);
        startActivity(intent);
    }

    public void mostrarMotos(View view) {
        Intent intent = new Intent(this,Activity_Mostrar_Moto.class);
        startActivity(intent);
    }

    public void mostrarClientes(View view) {
        Intent intent = new Intent(this,Activity_Mostrar_Cliente.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,activity_login.class);
        startActivity(intent);
    }
}