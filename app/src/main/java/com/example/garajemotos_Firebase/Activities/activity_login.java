package com.example.garajemotos_Firebase.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import garajemotos_Firebase.R;

public class activity_login extends AppCompatActivity {

    private EditText edt_correo, edt_clave;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_correo = (EditText) findViewById(R.id.edt_correo);
        edt_clave = (EditText) findViewById(R.id.edt_clave);
        mAuth = FirebaseAuth.getInstance();
    }

    public void nuevaCuenta(View view) {
        Intent intent = new Intent(this,activity_Nueva_Cuenta.class);
        startActivity(intent);
    }

    public void login(View view) {
        String email = String.valueOf(edt_correo.getText());
        String password = String.valueOf(edt_clave.getText());
        boolean datosOK = compruebaDatos(email,password);
        if(datosOK){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.i("firebasedb", "signInWithEmail:success");
                                Toast.makeText(activity_login.this, "Acceso correcto", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Intent intent = new Intent(activity_login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("firebasedb", "signInWithEmail:failure", task.getException());
                                Toast.makeText(activity_login.this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
        }

    }

    private boolean compruebaDatos(String correo, String clave){
        if(correo.isEmpty()){
            edt_correo.setError("El campo correo no puede estar vacío");
            return false;
        }else if(clave.isEmpty()){
            edt_clave.setError("El campo clave no puede estar vacío");
            return false;
        }else{
            return true;
        }
    }
}