package com.example.garajemotos_Firebase.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Cliente;

import java.util.ArrayList;
import java.util.List;

import garajemotos_Firebase.R;

public class ListaClientesAdapter extends RecyclerView.Adapter<ClienteViewHolder> {

    private Context p;
    private List<Cliente> listaClientes;
    private LayoutInflater mInflater;

    public void setP(Context p) {
        this.p = p;
        this.listaClientes = new ArrayList<Cliente>();
    }

    public ListaClientesAdapter(Context p, List<Cliente> listaClientes) {
        this.p = p;
        this.listaClientes = listaClientes;
        mInflater = LayoutInflater.from(p);
    }

    public Context getP() {
        return p;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
        notifyDataSetChanged();
    }

    public ListaClientesAdapter(Context p) {
        this.p = p;
        mInflater = LayoutInflater.from(p);
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_recyclerview_cliente, parent, false);
        return new ClienteViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        if (listaClientes != null) {
            Cliente cliente_actual = listaClientes.get(position);
            holder.txt_rv_id_cliente.setText(String.valueOf("id Cliente: " + cliente_actual.getIdCliente()));
            holder.txt_rv_nombre.setText(cliente_actual.getNombre());
            holder.txt_rv_direccion.setText(cliente_actual.getDireccion());
        }
    }

    @Override
    public int getItemCount() {
        if (listaClientes != null) {
            return listaClientes.size();
        } else {
            return 0;
        }
    }
}
