package com.example.garajemotos_Firebase.Controladores;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.garajemotos_Firebase.Clases.Moto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotoFirebaseController {

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private List<Moto> motos;

    public interface MotoStatus
    {
        void motoIsLoaded(List<Moto> motos, List<String> keys);
        void motoIsAdd();
        void motoIsUpdate();
        void motoIsDelete();
    }

    public MotoFirebaseController() {
        this.mDatabase  = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("motos");
        this.motos  = new ArrayList<Moto>();
    }

    public void obtener_motos(final MotoStatus motoStatus)
    {
        this.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                motos.clear();
                List<String> keys = new ArrayList<String>();
                for(DataSnapshot keynode: snapshot.getChildren())
                {
                    keys.add(keynode.getKey());
                    Moto m = keynode.getValue(Moto.class);
                    motos.add(m);
                }
                motoStatus.motoIsLoaded(motos,keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //---------------------------------------------------------------------------------
    public void insertarMoto(final MotoStatus motoStatus, Moto m, String matricula)
    {

        //this.myRef.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
        this.myRef.child(matricula).setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                motoStatus.motoIsAdd();
                Log.i("firebasedb", "insercion correcta");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // si hay un fallo
                        Log.i("firebasedb", "insercion incorrecta");
                    }
                });
    }
    //---------------------------------------------------------------------------------
    public void borrarMoto(final MotoStatus motoStatus, String key)
    {
        this.myRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                motoStatus.motoIsDelete();
                Log.i("firebasedb", "borrado correcto");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // si hay un fallo
                        Log.i("firebasedb", "borrado incorrecto");
                    }
                });
    }
    //---------------------------------------------------------------------------------
    public void actualizarMoto(final MotoStatus motoStatus, String key, Moto m)
    {
        Map<String, Object> nuevaMoto = new HashMap<String,Object>();
        nuevaMoto.put(key,m);
        myRef.updateChildren(nuevaMoto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                motoStatus.motoIsUpdate();
                Log.i("firebasedb", "actualizacion correcta");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // si hay un fallo
                        Log.i("firebasedb", "actualizacion incorrecta");
                    }
                });
    }

}
