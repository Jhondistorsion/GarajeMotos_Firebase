package com.example.garajemotos_Firebase.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Moto;

import java.util.List;

import garajemotos_Firebase.R;

public class MotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_OBJETO_MOTO = "miViewHolderMoto";
    public static final String EXTRA_OBJETO_IMG_MOTO ="miFotoMotoViewHolder";

    public TextView txt_rv_Matricula;
    public TextView txt_rv_Marca;
    public TextView txt_rv_Modelo;
    public ImageView img_rv_moto_foto;
    ListaMotosAdapter lcAdapter;

    public MotoViewHolder(@NonNull View itemView, ListaMotosAdapter lcAdapter) {
        super(itemView);
        txt_rv_Matricula = (TextView) itemView.findViewById(R.id.txt_rv_Matricula);
        txt_rv_Marca = (TextView) itemView.findViewById(R.id.txt_rv_Marca);
        txt_rv_Modelo = (TextView) itemView.findViewById(R.id.txt_rv_Modelo);
        img_rv_moto_foto = (ImageView) itemView.findViewById(R.id.img_rv_cliente);

        this.lcAdapter = lcAdapter;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int mPosition = getAdapterPosition();
        List<Moto> motos = this.lcAdapter.getListaMotos();
        Moto moto = motos.get(mPosition);
        Intent intent = new Intent(lcAdapter.getP(), Activity_Mostrar_Detalles_Moto.class);
        //Bitmap fotov = moto.getFoto();
        //Moto moto1 = new Moto(moto.getMatricula(), moto.getMarca(), moto.getModelo(), moto.getAnio(), null);

        intent.putExtra(EXTRA_OBJETO_MOTO, moto);
        lcAdapter.getP().startActivity(intent);

    }
}
