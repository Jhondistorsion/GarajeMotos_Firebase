package com.example.garajemotos_Firebase.Activities;

import static com.example.garajemotos_Firebase.Utilidades.ImagenesBlobBitmap.decodeSampledBitmapFrombyteArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garajemotos_Firebase.Clases.Configuracion;
import com.example.garajemotos_Firebase.Clases.Moto;
import com.example.garajemotos_Firebase.Utilidades.ImagenesFirebase;

import java.util.ArrayList;
import java.util.List;

import garajemotos_Firebase.R;

public class ListaMotosAdapter extends RecyclerView.Adapter<MotoViewHolder> {

    private Context p;
    private List<Moto> listaMotos;
    private LayoutInflater mInflater;

    public void setP(Context p) {
        this.p = p;
        this.listaMotos = new ArrayList<Moto>();
    }

    public ListaMotosAdapter(Context p, List<Moto> listaMotos) {
        this.p = p;
        this.listaMotos = listaMotos;
        mInflater = LayoutInflater.from(p);
    }

    public Context getP() {
        return p;
    }

    public List<Moto> getListaMotos() {
        return listaMotos;
    }

    public void setListaMotos(List<Moto> listaMotos) {
        this.listaMotos = listaMotos;
        notifyDataSetChanged();
    }

    public ListaMotosAdapter(Context p) {
        this.p = p;
        mInflater = LayoutInflater.from(p);
    }

    @NonNull
    @Override
    public MotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_recyclerview_moto, parent, false);
        return new MotoViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MotoViewHolder holder, int position) {
        if (listaMotos != null) {
            Moto moto_actual = listaMotos.get(position);
            holder.txt_rv_Matricula.setText("Matricula: " + moto_actual.getMatricula());
            holder.txt_rv_Modelo.setText(moto_actual.getModelo());
            holder.txt_rv_Marca.setText("Marca: " + moto_actual.getMarca());
            if (moto_actual.getFoto()) {
                new ImagenesFirebase().descargarFoto(new ImagenesFirebase.FotoStatus() {
                    @Override
                    public void FotoIsDownload(byte[] bytes) {
                        if(bytes != null) {
                            Log.i("firebasedb","foto descargada correctamente");
                            Bitmap fotob = decodeSampledBitmapFrombyteArray(bytes, Configuracion.ALTO_IMAGENES_BITMAP, Configuracion.ANCHO_IMAGENES_BITMAP);
                            holder.img_rv_moto_foto.setImageBitmap(fotob);
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
                },"motos/"+moto_actual.getMatricula()+".png");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (listaMotos != null) {
            return listaMotos.size();
        } else {
            return 0;
        }
    }
}
