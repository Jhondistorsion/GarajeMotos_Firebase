package com.example.garajemotos_Firebase.Controladores;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.garajemotos_Firebase.Clases.Cliente;
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

public class ClienteFirebaseController {

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private List<Cliente> clientes;

    public interface ClienteStatus
    {
        void clienteIsLoaded(List<Cliente> clientes, List<String> keys);
        void clienteIsAdd();
        void clienteIsUpdate();
        void clienteIsDelete();
    }

    public ClienteFirebaseController() {
        this.mDatabase  = FirebaseDatabase.getInstance();
        this.myRef = mDatabase.getReference("clientes");
        this.clientes  = new ArrayList<Cliente>();
    }

    public void obtener_clientes(final ClienteFirebaseController.ClienteStatus clienteStatus)
    {
        this.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clientes.clear();
                List<String> keys = new ArrayList<String>();
                for(DataSnapshot keynode: snapshot.getChildren())
                {
                    keys.add(keynode.getKey());
                    Cliente c = keynode.getValue(Cliente.class);
                    clientes.add(c);
                }
                clienteStatus.clienteIsLoaded(clientes,keys);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //---------------------------------------------------------------------------------
    public void insertarCliente(final ClienteFirebaseController.ClienteStatus clienteStatus, Cliente c, String dni)
    {

        //this.myRef.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
        this.myRef.child(dni).setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                clienteStatus.clienteIsAdd();
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
    public void borrarCliente(final ClienteFirebaseController.ClienteStatus clienteStatus, String key)
    {
        this.myRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                clienteStatus.clienteIsDelete();
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
    public void actualizarCliente(final ClienteFirebaseController.ClienteStatus clienteStatus, String key, Cliente c)
    {
        Map<String, Object> nuevoCliente = new HashMap<String,Object>();
        nuevoCliente.put(key,c);
        myRef.updateChildren(nuevoCliente).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // si todo va bien
                clienteStatus.clienteIsUpdate();
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
