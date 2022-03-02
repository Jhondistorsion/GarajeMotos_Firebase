package com.example.garajemotos_Firebase.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.garajemotos_Firebase.Clases.Cliente;
import com.example.garajemotos_Firebase.Clases.Moto;
import com.example.garajemotos_Firebase.Controladores.ClienteFirebaseController;
import com.example.garajemotos_Firebase.Controladores.MotoFirebaseController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import garajemotos_Firebase.R;

public class Activity_Nuevo_Modifica_Cliente extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView txt_titulo_cliente;
    EditText edt_nombre, edt_direccion, edt_dni;
    Spinner sp_motos;
    Button btn_crear_modificar;

    String modo;
    static ArrayAdapter<Moto> adapter;
    private Moto mseleccionada;
    private Cliente otroCliente;
    private Intent intent;

    private String nombre, direccion, moto, dni;

    boolean matriculaRepite = true;
    private List<Cliente> listaClientes;
    private List<Moto> listaMotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_modifica_cliente);

        txt_titulo_cliente = (TextView) findViewById(R.id.txt_titulo_cliente);
        edt_nombre = (EditText) findViewById(R.id.edt_nombre);
        edt_direccion = (EditText) findViewById(R.id.edt_direccion);
        edt_dni = (EditText) findViewById(R.id.edt_dni);
        sp_motos = (Spinner) findViewById(R.id.sp_motos);
        btn_crear_modificar = (Button) findViewById(R.id.btn_crear_modificar);

        obtenerMotos();
        obtenerClientes();
        obtenerComboMotos();
        preparaAdaptador(listaMotos);


        intent = getIntent();
        if (intent != null) {
            modo = intent.getStringExtra(activity_mostrar_detalles_cliente.EXTRA_MODO);

            if (modo == null) {
                modo = "crear";
            }

            aplicaModo(modo);

        }


    }

    private void obtenerComboMotos() {

        for (Moto m : listaMotos) {
            Iterator<Moto> iterator = listaMotos.iterator();
            while (iterator.hasNext()) {
                if (listaClientes.contains(m.getMatricula())) {
                    iterator.remove();
                }

            }

        }

    }

    private void obtenerClientes() {

        new ClienteFirebaseController().obtener_clientes(new ClienteFirebaseController.ClienteStatus() {
            @Override
            public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {
                listaClientes = clientes;
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
    }

    private void obtenerMotos() {

        new MotoFirebaseController().obtener_motos(new MotoFirebaseController.MotoStatus() {
            @Override
            public void motoIsLoaded(List<Moto> motos, List<String> keys) {
                cargaListaMotos(motos);
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
    }

    private void cargaListaMotos(List<Moto> motos) {
        listaMotos = motos;
    }

    private void preparaAdaptador(List<Moto> motos) {
        ArrayAdapter<Moto> motosAdapter;
        motosAdapter = new ArrayAdapter<>(this, R.layout.item_moto, motos);
        sp_motos.setAdapter(motosAdapter);
        sp_motos.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Moto m = (Moto) sp_motos.getItemAtPosition(i);
        mseleccionada = m;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void aplicaModo(String modo) {

        if (modo.equals("modificar")) {

            otroCliente = (Cliente) intent.getSerializableExtra(activity_mostrar_detalles_cliente.EXTRA_CLIENTE);

            txt_titulo_cliente.setText("MODIFICAR CLIENTE");
            btn_crear_modificar.setText("MODIFICAR");

            edt_nombre.setText(otroCliente.getNombre());
            edt_direccion.setText(otroCliente.getDireccion());
            edt_dni.setText(otroCliente.getIdCliente());
            edt_dni.setEnabled(false);

        } else if (modo.equals("crear")) {
            txt_titulo_cliente.setText("CREAR CLIENTE");
            btn_crear_modificar.setText("CREAR");
        }
    }

    public void crea_modifica_cliente(View view) {

        if (modo.equals("crear")) {

            //Metodo crear///////////////////////////

            comprobarMatriculaUsada();
            boolean errorDatos = obtenerInformacion();

            if (!errorDatos) {

                AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
                alerta1.setTitle("¿Deseas guardar el cliente?");
                alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Cliente nuevoCliente = new Cliente(nombre, direccion, mseleccionada.getMatricula(), dni);


                        //Insertamos el cliente en la base de datos
                        new ClienteFirebaseController().insertarCliente(new ClienteFirebaseController.ClienteStatus() {
                            @Override
                            public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {

                            }

                            @Override
                            public void clienteIsAdd() {
                                // aquí hay que poner cuando se haya insertado bien qué hacer
                                Toast.makeText(Activity_Nuevo_Modifica_Cliente.this, "Cliente insertado correctamente", Toast.LENGTH_LONG).show();
                                finish();
                                volverAlMenuPrincipal();
                            }

                            @Override
                            public void clienteIsUpdate() {

                            }

                            @Override
                            public void clienteIsDelete() {

                            }
                        }, nuevoCliente, nuevoCliente.getIdCliente());
                        //----------------------------------------------//


                    }


                });
                alerta1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mostrarMensajeCancelado();

                    }
                });
                alerta1.show();

            }

        } else if (modo.equals("modificar")) {

            //Metodo modificar////////////////////////

            boolean errorDatos = obtenerInformacion();

            if (!errorDatos) {

                AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
                alerta1.setTitle("¿Deseas modificar el cliente?");
                alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        otroCliente.setNombre(nombre);
                        otroCliente.setDireccion(direccion);
                        otroCliente.setMoto(mseleccionada.getMatricula());

                        /*
                        boolean matriculaUsada = comprobarMatriculaUsada();
                        if (!matriculaUsada) {
                            boolean insertadoOK = ClienteController.modificarCliente(otroCliente);
                            if (insertadoOK) {
                                mostrarMensajeOK();
                            } else {
                                mostrarMensajeError();
                            }

                            volverAlMenuPrincipal();
                        }

                         */

                        //Modificamos el cliente en la base de datos
                        new ClienteFirebaseController().actualizarCliente(new ClienteFirebaseController.ClienteStatus() {
                            @Override
                            public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {

                            }

                            @Override
                            public void clienteIsAdd() {

                            }

                            @Override
                            public void clienteIsUpdate() {
                                // aquí hay que poner cuando se haya modificado bien qué hacer
                                Toast.makeText(Activity_Nuevo_Modifica_Cliente.this, "Cliente modificado correctamente", Toast.LENGTH_LONG).show();
                                finish();
                                volverAlMenuPrincipal();

                            }

                            @Override
                            public void clienteIsDelete() {

                            }
                        }, dni, otroCliente);
                        //----------------------------------------------//


                    }

                });
                alerta1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mostrarMensajeCancelado();

                    }
                });
                alerta1.show();

            }
        }

    }

    private void comprobarMatriculaUsada() {

        new ClienteFirebaseController().obtener_clientes(new ClienteFirebaseController.ClienteStatus() {
            @Override
            public void clienteIsLoaded(List<Cliente> clientes, List<String> keys) {
                if (clientes.contains(mseleccionada.getMatricula())) {
                    matriculaRepite = true;
                } else {
                    matriculaRepite = false;
                }
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


    }

    private void mostrarMensajeError() {
        Toast.makeText(this, "Error al insertar/modificar cliente", Toast.LENGTH_SHORT).show();
    }

    private void mostrarMensajeOK() {
        Toast.makeText(this, "Cliente insertado/modificado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void mostrarMensajeCancelado() {
        Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show();
    }

    private void volverAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean obtenerInformacion() {

        nombre = String.valueOf(edt_nombre.getText());
        direccion = String.valueOf(edt_direccion.getText());
        dni = String.valueOf(edt_dni.getText());
        if (mseleccionada != null) {
            moto = (mseleccionada.getMatricula());
        } else {
            moto = "";
        }

        boolean error = false;

        if (nombre.isEmpty() || nombre.length() > 30) {
            edt_nombre.setError("El campo nombre no puede estar vacío o contener mas de 30 caracteres");
            error = true;
        } else if (direccion.isEmpty() || direccion.length() > 45) {
            edt_direccion.setError("El campo dirección no puede estar vacío o contener mas de 45 caracteres");
            error = true;
        } else if (moto.isEmpty()) {
            Toast.makeText(this, "Debes seleccionar o crear almenos una moto", Toast.LENGTH_SHORT).show();
            error = true;
        } else if (dni.isEmpty()) {
            Toast.makeText(this, "El campo DNI no puede estar vacío", Toast.LENGTH_SHORT).show();
            error = true;
        } else if (matriculaRepite) {
            Toast.makeText(this, "Error, la moto seleccionada se utiliza por otro cliente", Toast.LENGTH_SHORT).show();
            error = true;
        }
        return error;
    }
}