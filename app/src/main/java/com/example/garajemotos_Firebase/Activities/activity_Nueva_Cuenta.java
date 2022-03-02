package com.example.garajemotos_Firebase.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.garajemotos_Firebase.Utilidades.ImagenesFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import garajemotos_Firebase.R;

public class activity_Nueva_Cuenta extends AppCompatActivity {

    private EditText edt_newcorreo, edt_newclave;
    private ImageView img_avatar;
    private FirebaseAuth mAuth;

    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cuenta);

        edt_newcorreo = (EditText) findViewById(R.id.edt_newcorreo);
        edt_newclave = (EditText) findViewById(R.id.edt_newclave);
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        mAuth = FirebaseAuth.getInstance();
    }

    public void volver(View view) {
        Intent intent = new Intent(this,activity_login.class);
        startActivity(intent);
    }

    private boolean compruebaDatos(String correo, String clave){
        if(correo.isEmpty()){
            edt_newcorreo.setError("El campo correo no puede estar vacío");
            return false;
        }else if(clave.isEmpty()){
            edt_newclave.setError("El campo clave no puede estar vacío");
            return false;
        }else{
            return true;
        }
    }

    public void nueva_cuenta(View view) {
        String email = String.valueOf(edt_newcorreo.getText()).trim();
        String password = String.valueOf(edt_newclave.getText());
        boolean datosOK = compruebaDatos(email,password);
        if(datosOK){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("firebasedb", "createUserWithEmail:success");
                                Toast.makeText(activity_Nueva_Cuenta.this, "Nuevo usuario creado", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                // updateUI(user);


                                //Ahora iniciamos sesion con el nuevo usuario//
                                mAuth.signInWithEmailAndPassword(email, password);
                                //---------------------------------------------//

                                //Ahora insertamos la imagen en la cuenta actual//
                                String modo = "usuario";
                                if(imagen_seleccionada != null)
                                {
                                    String email =  mAuth.getCurrentUser().getEmail();
                                    new ImagenesFirebase().subirFoto(new ImagenesFirebase.FotoStatus() {
                                        @Override
                                        public void FotoIsDownload(byte[] bytes) {
                                        }
                                        @Override
                                        public void FotoIsDelete() {
                                        }
                                        @Override
                                        public void FotoIsUpload() {
                                            Toast.makeText(activity_Nueva_Cuenta.this,"avatar subido correctamente",Toast.LENGTH_LONG).show();
                                        }
                                    },email, img_avatar, modo);
                                }
                                //----------------------------------------------//

                                //Cerramos la sesion del usuario//
                                FirebaseAuth.getInstance().signOut();
                                //----------------------------------------------//


                                Intent intent = new Intent(activity_Nueva_Cuenta.this, activity_login.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("firebasedb", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(activity_Nueva_Cuenta.this, "Creación de usuario fallida", Toast.LENGTH_SHORT).show();
                                //  updateUI(null);
                            }
                        }
                    });
        }

    }


    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------
    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                img_avatar.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}