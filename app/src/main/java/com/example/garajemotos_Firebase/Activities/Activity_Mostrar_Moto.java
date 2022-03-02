package com.example.garajemotos_Firebase.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Moto;
import com.example.garajemotos_Firebase.Controladores.MotoFirebaseController;

import java.util.ArrayList;
import java.util.List;

import garajemotos_Firebase.R;

public class Activity_Mostrar_Moto extends AppCompatActivity {


    private RecyclerView rv_motos;
    private ListaMotosAdapter mAdapter;
    private ArrayList<Moto> motos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_moto);

        rv_motos = findViewById(R.id.rv_clientes);

        mAdapter = new ListaMotosAdapter(this);
        new MotoFirebaseController().obtener_motos(new MotoFirebaseController.MotoStatus() {
            @Override
            public void motoIsLoaded(List<Moto> motos, List<String> keys) {
                mAdapter.setListaMotos(motos);
                //mAdapter.setKeys(keys);
            }

            @Override
            public void motoIsAdd() {

            }

            @Override
            public void motoIsUpdate() {

            }

            @Override
            public void motoIsDelete() {

            }
        });

        rv_motos.setAdapter(mAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv_motos.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv_motos.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    public void volver(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}