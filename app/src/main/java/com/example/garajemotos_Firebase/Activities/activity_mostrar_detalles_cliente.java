package com.example.garajemotos_Firebase.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.garajemotos_Firebase.Clases.Cliente;
import com.example.garajemotos_Firebase.Controladores.ClienteFirebaseController;

import java.util.List;

import garajemotos_Firebase.R;

public class activity_mostrar_detalles_cliente extends AppCompatActivity {

    public static final String EXTRA_MODO = "miModoCliente";
    public static final String EXTRA_CLIENTE = "clienteDetalles";

    private TextView txt_id_cliente, txt_nombre, txt_direccion, txt_moto_cliente;

    private String modo;
    Cliente c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_detalles_cliente);

        txt_id_cliente = (TextView) findViewById(R.id.txt_id_cliente);
        txt_nombre = (TextView) findViewById(R.id.txt_nombre);
        txt_direccion = (TextView) findViewById(R.id.txt_direccion);
        txt_moto_cliente = (TextView) findViewById(R.id.txt_moto_cliente);

        Intent intent = getIntent();
        if (intent != null) {

            c = (Cliente) intent.getSerializableExtra(ClienteViewHolder.EXTRA_OBJETO_CLIENTE);
            txt_id_cliente.setText("id Cliente: " + String.valueOf(c.getIdCliente()));
            txt_nombre.setText(c.getNombre());
            txt_direccion.setText(c.getDireccion());
            txt_moto_cliente.setText("Matrícula moto: " + c.getMoto());
        }
    }

    public void volver(View view) {
        Intent intent = new Intent(this, Activity_Mostrar_Cliente.class);
        startActivity(intent);
    }

    public void editar(View view) {
        Intent intent = new Intent(this, Activity_Nuevo_Modifica_Cliente.class);
        modo = "modificar";
        intent.putExtra(EXTRA_MODO, modo);
        intent.putExtra(EXTRA_CLIENTE, c);
        startActivity(intent);
    }

    public void eliminar(View view) {
        AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
        alerta1.setTitle("Deseas eliminar el cliente?");
        alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new ClienteFirebaseController().borrarCliente(new ClienteFirebaseController.ClienteStatus() {
                    @Override
                    public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {

                    }

                    @Override
                    public void clienteIsAdd() {

                    }

                    @Override
                    public void clienteIsUpdate() {

                    }

                    @Override
                    public void clienteIsDelete() {
                        // aquí hay que poner cuando se haya borrado bien qué hacer
                        Toast.makeText(activity_mostrar_detalles_cliente.this, "borrado correcto", Toast.LENGTH_LONG).show();
                        finish();
                        atras();
                    }
                }, c.getIdCliente());
            }
        });
        alerta1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarToast("Operación cancelada");
            }
        });
        alerta1.show();
    }

    public void atras() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}