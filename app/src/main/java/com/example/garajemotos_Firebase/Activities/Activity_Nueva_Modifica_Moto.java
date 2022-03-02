package com.example.garajemotos_Firebase.Activities;

import static com.example.garajemotos_Firebase.Utilidades.ImagenesBlobBitmap.decodeSampledBitmapFrombyteArray;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.garajemotos_Firebase.Clases.Configuracion;
import com.example.garajemotos_Firebase.Clases.Moto;
import com.example.garajemotos_Firebase.Controladores.MotoFirebaseController;
import com.example.garajemotos_Firebase.Utilidades.ImagenesFirebase;

import java.io.IOException;
import java.util.List;

import garajemotos_Firebase.R;

public class Activity_Nueva_Modifica_Moto extends AppCompatActivity {

    private TextView txt_titulo_moto;
    private EditText edt_marca, edt_matricula, edt_modelo, edt_anio;
    private ImageView imv_moto;
    private Button btn_crear_modificar;

    private String modo;
    private Moto nuevaMoto;
    private Moto otraMoto;
    public static final int NUEVA_IMAGEN = 1;
    Uri imagen_seleccionada = null;
    private Intent intent;

    private String estado;
    private String marca, matricula, modelo;
    private String anio;
    private int anioint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_modifica_moto);

        txt_titulo_moto = (TextView) findViewById(R.id.txt_titulo_cliente);
        edt_marca = (EditText) findViewById(R.id.edt_direccion);
        edt_matricula = (EditText) findViewById(R.id.edt_nombre);
        edt_modelo = (EditText) findViewById(R.id.edt_modelo);
        edt_anio = (EditText) findViewById(R.id.edt_anio);
        imv_moto = (ImageView) findViewById(R.id.imv_moto);
        btn_crear_modificar = (Button) findViewById(R.id.btn_crear_modificar);

        intent = getIntent();
        if (intent != null) {

            modo = intent.getStringExtra(Activity_Mostrar_Detalles_Moto.EXTRA_MODO);

            if (modo == null) {
                modo = "crear";
            }

            aplicaModo(modo);
        }

    }

    private void aplicaModo(String modo) {

        if (modo.equals("crear")) {

            txt_titulo_moto.setText("NUEVA MOTO");
            btn_crear_modificar.setText("CREAR");


        } else if (modo.equals("modificar")) {

            otraMoto = (Moto) intent.getSerializableExtra(Activity_Mostrar_Detalles_Moto.EXTRA_MOTO);

            txt_titulo_moto.setText("MODIFICAR MOTO");
            btn_crear_modificar.setText("MODIFICAR");

            edt_matricula.setText(otraMoto.getMatricula());
            edt_matricula.setEnabled(false);

            edt_marca.setText(otraMoto.getMarca());
            edt_modelo.setText(otraMoto.getModelo());
            edt_anio.setText(String.valueOf(otraMoto.getAnio()));

            if (otraMoto.getFoto()) {
                new ImagenesFirebase().descargarFoto(new ImagenesFirebase.FotoStatus() {
                    @Override
                    public void FotoIsDownload(byte[] bytes) {
                        if (bytes != null) {
                            Log.i("firebasedb", "foto descargada correctamente");
                            Bitmap fotob = decodeSampledBitmapFrombyteArray(bytes, Configuracion.ALTO_IMAGENES_BITMAP, Configuracion.ANCHO_IMAGENES_BITMAP);
                            imv_moto.setImageBitmap(fotob);
                        } else {
                            Log.i("firebasedb", "foto no descargada correctamente");
                        }
                    }

                    @Override
                    public void FotoIsUpload() {
                    }

                    @Override
                    public void FotoIsDelete() {
                    }
                }, "motos/" + otraMoto.getMatricula() + ".png");
            }

        }


    }


    public void crea_modifica_moto(View view) {

        if (modo.equals("crear")) {

            //Metodo crear///////////////////////////

            boolean errorDatos = obtenerInformacion();

            if (!errorDatos) {

                compruebaDuplicados();
                anioint = Integer.parseInt(anio);

                AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
                alerta1.setTitle("¿Deseas guardar la moto?");
                alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Insertamos la imagen en motos//
                        String modo = "moto";
                        if (imagen_seleccionada != null) {

                            new ImagenesFirebase().subirFoto(new ImagenesFirebase.FotoStatus() {
                                @Override
                                public void FotoIsDownload(byte[] bytes) {
                                }

                                @Override
                                public void FotoIsDelete() {
                                }

                                @Override
                                public void FotoIsUpload() {
                                    Toast.makeText(Activity_Nueva_Modifica_Moto.this, "moto foto subida correctamente", Toast.LENGTH_LONG).show();
                                }
                            }, matricula, imv_moto, modo);
                            ////////////////////
                            nuevaMoto = new Moto(matricula, marca, modelo, anioint, true);
                        } else {
                            nuevaMoto = new Moto(matricula, marca, modelo, anioint, false);
                        }
                        //Insertamos la moto en la base de datos
                        new MotoFirebaseController().insertarMoto(new MotoFirebaseController.MotoStatus() {
                            @Override
                            public void motoIsLoaded(List<Moto> motos, List<String> keys) {

                            }

                            @Override
                            public void motoIsAdd() {
                                // aquí hay que poner cuando se haya insertado bien qué hacer
                                Toast.makeText(Activity_Nueva_Modifica_Moto.this, "Moto insertada correctamente", Toast.LENGTH_LONG).show();
                                finish();
                                volverAlMenuPrincipal();
                            }

                            @Override
                            public void motoIsUpdate() {

                            }

                            @Override
                            public void motoIsDelete() {

                            }
                        }, nuevaMoto, nuevaMoto.getMatricula());
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
                anioint = Integer.parseInt(anio);

                AlertDialog.Builder alerta1 = new AlertDialog.Builder(this);
                alerta1.setTitle("¿Deseas modificar la moto?");
                alerta1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        otraMoto.setMarca(marca);
                        otraMoto.setModelo(modelo);
                        otraMoto.setAnio(anioint);
                        //otraMoto.setFoto(fotov);

                        //Insertamos la imagen en motos//
                        String modo = "moto";
                        if (imagen_seleccionada != null) {

                            new ImagenesFirebase().subirFoto(new ImagenesFirebase.FotoStatus() {
                                @Override
                                public void FotoIsDownload(byte[] bytes) {
                                }

                                @Override
                                public void FotoIsDelete() {
                                }

                                @Override
                                public void FotoIsUpload() {
                                    Toast.makeText(Activity_Nueva_Modifica_Moto.this, "moto foto subida correctamente", Toast.LENGTH_LONG).show();
                                }
                            }, matricula, imv_moto, modo);
                            ////////////////////
                            otraMoto.setFoto(true);
                        } else {
                            if (!otraMoto.getFoto()) {
                                otraMoto.setFoto(false);
                            }
                        }


                        //Modificamos la moto en la base de datos
                        new MotoFirebaseController().actualizarMoto(new MotoFirebaseController.MotoStatus() {
                            @Override
                            public void motoIsLoaded(List<Moto> motos, List<String> keys) {

                            }

                            @Override
                            public void motoIsAdd() {

                            }

                            @Override
                            public void motoIsUpdate() {
                                // aquí hay que poner cuando se haya modificado bien qué hacer
                                Toast.makeText(Activity_Nueva_Modifica_Moto.this, "Moto modificada correctamente", Toast.LENGTH_LONG).show();
                                finish();
                                volverAlMenuPrincipal();

                            }

                            @Override
                            public void motoIsDelete() {

                            }
                        }, matricula, otraMoto);
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

    private void compruebaDuplicados() {

        new MotoFirebaseController().obtener_motos(new MotoFirebaseController.MotoStatus() {
            @Override
            public void motoIsLoaded(List<Moto> motos, List<String> keys) {
                miObtener(motos);
                /*
                for (Moto m : motos) {
                    if (m.getMatricula().equals(matricula)) {
                        Toast.makeText(Activity_Nueva_Modifica_Moto.this, "La matricula ya existe en la base de datos", Toast.LENGTH_LONG).show();
                        volverAlMenuPrincipal();
                    }
                }

                 */
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

    private void miObtener(List<Moto> motos) {

        for (Moto m : motos) {
            if (m.getMatricula().equals(matricula)) {
                Toast.makeText(this, "La matricula ya existe en la base de datos", Toast.LENGTH_LONG).show();
                volverAlMenuPrincipal();
            }
        }

    }

    private void mostrarMensajeCancelado() {
        Toast.makeText(this, "Operación cancelada", Toast.LENGTH_SHORT).show();
    }

    private void volverAlMenuPrincipal() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private boolean obtenerInformacion() {

        matricula = String.valueOf(edt_matricula.getText());
        marca = String.valueOf(edt_marca.getText());
        modelo = String.valueOf(edt_modelo.getText());
        anio = String.valueOf(edt_anio.getText());

        boolean error = false;

        if (matricula.isEmpty() || matricula.length() > 7) {
            edt_matricula.setError("El campo matrícula no puede estar vacío o ser mayor de 7 caracteres");
            error = true;
        } else if (marca.isEmpty() || marca.length() > 25) {
            edt_marca.setError("El campo marca no puede estar vacío o ser mayor de 25 caracteres");
            error = true;
        } else if (modelo.isEmpty() || modelo.length() > 25) {
            edt_modelo.setError("El campo modelo no puede estar vacío o ser mayor de 25 caracteres");
            error = true;
        } else if (anio.isEmpty()) {
            edt_anio.setError("El campo debe contener un año");
            error = true;
        }

        return error;

    }

    //--------CODIGO PARA CAMBIAR LA IMAGEN----------------
    public void cambiar_imagen(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecciona una imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, NUEVA_IMAGEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUEVA_IMAGEN && resultCode == Activity.RESULT_OK) {
            imagen_seleccionada = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagen_seleccionada);
                imv_moto.setImageBitmap(bitmap);

                //---------------------------------------------

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}