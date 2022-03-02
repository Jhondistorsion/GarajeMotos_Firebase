package com.example.garajemotos_Firebase.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Cliente;
import com.example.garajemotos_Firebase.Controladores.ClienteFirebaseController;

import java.util.ArrayList;
import java.util.List;

import garajemotos_Firebase.R;

public class Activity_Mostrar_Cliente extends AppCompatActivity {

    private RecyclerView rv_clientes;
    private ListaClientesAdapter mAdapter;
    private ArrayList<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);

        rv_clientes = findViewById(R.id.rv_clientes);

        mAdapter = new ListaClientesAdapter(this);

        new ClienteFirebaseController().obtener_clientes(new ClienteFirebaseController.ClienteStatus() {
            @Override
            public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {
                mAdapter.setListaClientes(clientes);
                //mAdapter.setKeys(keys);
            }

            @Override
            public void clienteIsAdd() {

            }

            @Override
            public void clienteIsUpdate() {

            }

            @Override
            public void clienteIsDelete() {

            }
        });

        rv_clientes.setAdapter(mAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv_clientes.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv_clientes.setLayoutManager(new GridLayoutManager(this, 3));
        }

    }

    public void volver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}