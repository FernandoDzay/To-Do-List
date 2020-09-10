package com.fiuady.a2dolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.fiuady.a2dolist.db.Tarea;
import com.fiuady.a2dolist.model.TareaListActivityModel;

import java.util.ArrayList;

public class A2_2DoList
        extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
//-------------------------------------------------Declarar Objetos----------------------------------------------------------//

    //Interfaz android
    Button Fecha;
    Button Hora;
    ImageButton Ok;
    EditText titulo;
    EditText descripcion;
    ToggleButton importancia;
    Spinner prioridad;
    Switch datecheck;
    TextView hora;
    TextView fecha;

    //Objetos
    ArrayList<String> Importancia = new ArrayList<>();
    ArrayAdapter<String> ImportanciaAdapter;
    Tarea tarea;
    TareaListActivityModel model;

    //Variables
    String mes, dia, minuto, horas;

//-------------------------------------------OnCreate---------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2_2_do_list);

        model = ViewModelProviders.of(this).get(TareaListActivityModel.class);

        //Inicializar objetos
        Fecha =  findViewById(R.id.Btn_Fecha);
        Hora = findViewById(R.id.Btn_Hora);
        Ok =  findViewById(R.id.Btn_Ok);
        titulo =  findViewById(R.id.EditText_Titulo);
        descripcion =  findViewById(R.id.EditText_Descripcion);
        importancia =  findViewById(R.id.Btn_Importante);
        prioridad =  findViewById(R.id.A2_Spinner);
        datecheck =  findViewById(R.id.Switch_Date);
        hora =  findViewById(R.id.Txt_Hora);
        fecha =  findViewById(R.id.Txt_Fecha);

        //Spinner
        Importancia.add("Baja");
        Importancia.add("Media");
        Importancia.add("Alta");
        ImportanciaAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, Importancia);
        prioridad.setAdapter(ImportanciaAdapter);


        if(!model.Date.equals("")){
            fecha.setText(model.Date);
        }

        if(!model.Hour.equals("")){
            hora.setText(model.Hour);
        }

        //Recibir datos a modificar, si es que los hay
        if(getIntent().getExtras()!=null) {
            Bundle datos = getIntent().getExtras();
            Tarea tarea = new Tarea(0,"","",false,0,false,"","",0);
            tarea = (Tarea) datos.getSerializable("Tarea");
            if(getIntent().getExtras()!=null) {
                model.id = tarea.getId();
                titulo.setText(tarea.getTitle());
                descripcion.setText(tarea.getDescription());
                importancia.setChecked(tarea.getImportance());
                prioridad.setSelection(tarea.getPriority());
                datecheck.setChecked(tarea.getDateCheck());
                hora.setText(tarea.getHour());
                fecha.setText(tarea.getDate());
            }
            if (datecheck.isChecked()){
                Hora.setEnabled(true);
                Fecha.setEnabled(true);
            }
        }


        //---------------Listener-------------//

        //Botón añadir tarea y regresar
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regresar_A1();

            }
        });

        //Botón de fecha
        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        //Botón de Hora
        Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });


        //---------------Switch--------------//
        datecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hora.setEnabled(true);
                    Fecha.setEnabled(true);
                }
                else{
                    Hora.setEnabled(false);
                    Fecha.setEnabled(false);
                    hora.setText("");
                    fecha.setText("");
                    model.Date = fecha.getText().toString();
                    model.Hour = hora.getText().toString();
                } }});
    }


    //---------------------------------------Funciones-----------------------------------------//

    public void Regresar_A1(){
        Intent intent = new Intent(A2_2DoList.this, A1_2DoList.class);
        intent.putExtra("id", model.id);
        intent.putExtra("Titulo", titulo.getText().toString());
        intent.putExtra("Descripcion", descripcion.getText().toString());
        intent.putExtra("Importancia", importancia.isChecked());
        intent.putExtra("Prioridad", prioridad.getSelectedItemPosition());
        intent.putExtra("DateCheck", datecheck.isChecked());
        intent.putExtra("Hora", hora.getText().toString());
        if(model.Date.equals("")){
            model.Date = "0";
        }
        intent.putExtra("Fecha", model.Date);
        setResult(A2_2DoList.RESULT_OK, intent);
        finish();
    }

    //--------Overrides--------//
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView EFecha = findViewById(R.id.Txt_Fecha);
        mes=Integer.toString((month+1));
        dia=Integer.toString((dayOfMonth));
        if(month+1<10)
        {
            mes = "0"+Integer.toString((month+1));
        }
        if(dayOfMonth<10)
        {
            dia = "0"+Integer.toString(dayOfMonth);
        }
        EFecha.setText(dia+"/"+mes+"/"+Integer.toString(year));
        model.Date = Integer.toString(year) + mes + dia;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView EHora = findViewById(R.id.Txt_Hora);
        if(hourOfDay<10) {
            horas = "0"+Integer.toString((hourOfDay));
        }
        else{
            horas = Integer.toString((hourOfDay));
        }
        if(minute<10) {
            minuto = "0"+Integer.toString(minute);
        }
        else{
            minuto = Integer.toString(minute);
        }
        EHora.setText(horas+":"+minuto);

        model.Hour = hora.getText().toString();

    }

}
