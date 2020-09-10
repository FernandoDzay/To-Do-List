package com.fiuady.a2dolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class A4_2DoList extends AppCompatActivity {

    //Interfaz android
    Switch prioridad, fecha, ascendente, descendente;

    //Variables
    int Tipo, Orden;


//-------------------------------------------OnCreate---------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a4_2_do_list);


        //Inicializar objetos
        prioridad = findViewById(R.id.OrderByPriority);
        fecha = findViewById(R.id.OrderByDate);
        ascendente = findViewById(R.id.Ac);
        descendente = findViewById(R.id.Dc);

        //Recibir datos para modificar el activity
        RecuperarEstado();


        //---------------Listener-------------//

        prioridad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (prioridad.isChecked()) {
                    ascendente.setEnabled(true);
                    descendente.setEnabled(true);
                    ascendente.setChecked(true);
                    fecha.setChecked(false);
                } else if (!prioridad.isChecked() && !fecha.isChecked()) {
                    ascendente.setChecked(false);
                    descendente.setChecked(false);
                    ascendente.setEnabled(false);
                    descendente.setEnabled(false);

                }

            }
        });

        fecha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fecha.isChecked()) {
                    ascendente.setEnabled(true);
                    descendente.setEnabled(true);
                    ascendente.setChecked(true);
                    prioridad.setChecked(false);
                } else if (!prioridad.isChecked() && !fecha.isChecked()) {
                    ascendente.setChecked(false);
                    descendente.setChecked(false);
                    ascendente.setEnabled(false);
                    descendente.setEnabled(false);

                }
            }
        });

        ascendente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ascendente.isChecked()) {
                    descendente.setChecked(false);

                } else {
                    descendente.setChecked(true);

                }
            }
        });


        descendente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (descendente.isChecked()) {
                    ascendente.setChecked(false);

                } else {
                    ascendente.setChecked(true);
                }
            }
        });

    }


//------------------------------------------Funciones-----------------------------------------//

    public void Regresar_A1() {

        if (prioridad.isChecked()) {
            Tipo = 1;
        }
        if (fecha.isChecked()) {
            Tipo = 2;
        }
        if (ascendente.isChecked()) {
            Orden = 3;
        }
        if (descendente.isChecked()) {
            Orden = 2;
        }

        if (!prioridad.isChecked() && !fecha.isChecked()) {
            Tipo = 0;
            Orden = 0;

        }

        Intent intent = new Intent(A4_2DoList.this, A1_2DoList.class);
        intent.putExtra("tipo", Tipo);
        intent.putExtra("orden", Orden);
        setResult(A4_2DoList.RESULT_OK, intent);
    }

    public void RecuperarEstado() {
        ascendente.setEnabled(false);
        descendente.setEnabled(false);
        ascendente.setChecked(false);
        descendente.setChecked(false);
        Intent intent = getIntent();
        Orden = intent.getIntExtra("orden", 6);
        Tipo = intent.getIntExtra("tipo", 0);

        if (Tipo == 1 && Orden == 3) {
            prioridad.setChecked(true);
            ascendente.setChecked(true);
            ascendente.setEnabled(true);
            descendente.setEnabled(true);
        } else if (Tipo == 1 && Orden == 2) {
            prioridad.setChecked(true);
            descendente.setChecked(true);
            ascendente.setEnabled(true);
            descendente.setEnabled(true);
        } else if (Tipo == 2 && Orden == 3) {
            fecha.setChecked(true);
            ascendente.setChecked(true);
            ascendente.setEnabled(true);
            descendente.setEnabled(true);
        } else if (Tipo == 2 && Orden == 2) {
            fecha.setChecked(true);
            descendente.setChecked(true);
            ascendente.setEnabled(true);
            descendente.setEnabled(true);
        } else if (Tipo == 0 && Orden == 0) {
            fecha.setChecked(false);
            descendente.setChecked(false);
            ascendente.setEnabled(false);
            descendente.setEnabled(false);

        }
    }

    public void onBackPressed() {
        Regresar_A1();
        finish();
    }
}