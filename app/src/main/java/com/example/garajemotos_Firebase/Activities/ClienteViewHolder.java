package com.example.garajemotos_Firebase.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Cliente;

import java.util.List;

import garajemotos_Firebase.R;

public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public static final String EXTRA_OBJETO_CLIENTE = "miViewHolderCliente";

    public TextView txt_rv_id_cliente;
    public TextView txt_rv_nombre;
    public TextView txt_rv_direccion;
    ListaClientesAdapter lcAdapter;

    public ClienteViewHolder(@NonNull View itemView, ListaClientesAdapter lcAdapter) {
        super(itemView);
        txt_rv_id_cliente = (TextView) itemView.findViewById(R.id.txt_rv_id_cliente);
        txt_rv_nombre = (TextView) itemView.findViewById(R.id.txt_rv_nombre);
        txt_rv_direccion = (TextView) itemView.findViewById(R.id.txt_rv_direccion);

        this.lcAdapter = lcAdapter;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int mPosition = getAdapterPosition();
        List<Cliente> clientes = this.lcAdapter.getListaClientes();
        Cliente cliente = clientes.get(mPosition);
        Intent intent = new Intent(lcAdapter.getP(), activity_mostrar_detalles_cliente.class);
        Cliente cliente1 = new Cliente(cliente.getNombre(),cliente.getDireccion(),cliente.getMoto(),cliente.getIdCliente());

        intent.putExtra(EXTRA_OBJETO_CLIENTE, cliente1);
        lcAdapter.getP().startActivity(intent);

    }
}
