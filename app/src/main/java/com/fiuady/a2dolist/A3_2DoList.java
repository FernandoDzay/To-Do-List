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
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fiuady.a2dolist.model.TareaListActivityModel;

import java.util.ArrayList;

public class A3_2DoList extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    //Interfaz android
    Switch titulo,descripcion,Prioridad, SwitchFecha;
    EditText CadenaUsuario;
    TextView DesdeFecha,HastaFecha;
    Button DesdeBtn, HastaBtn;
    Spinner seleccion,prioridad;

    //Objetos
    ArrayList<String> arrayList1 = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter1;
    ArrayList<String> arrayList2 = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter2;
    TareaListActivityModel model;

    //Variables
    String mes, dia, ano;
    String Title_Switch="0", Notes_Switch="0", Priority_Switch="0", Date_Switch="0";
    String FilterText, PrevDate, PostDate, LogicPriority;
    boolean BtnDesdeActivado;
    int PriorityLevel, LogicPriority_setSelection, PriorityLevel_setSelection;


//-------------------------------------------OnCreate---------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a3_2_do_list);

        model = ViewModelProviders.of(this).get(TareaListActivityModel.class);

        //Inicializar objetos
        titulo = findViewById(R.id.Switch_Titulo);
        descripcion = findViewById(R.id.Switch_Notas);
        Prioridad = findViewById(R.id.Switch_Prioridad);
        CadenaUsuario = findViewById(R.id.Edit_Texto);
        DesdeFecha = findViewById(R.id.TextView_Fecha1);
        HastaFecha = findViewById(R.id.TextView_Fecha2);
        DesdeBtn = findViewById(R.id.Btn_Desde);
        HastaBtn = findViewById(R.id.Btn_Hasta);
        seleccion = findViewById(R.id.Spinner_intervalo);
        prioridad = findViewById(R.id.Spinner_Prioridad);
        SwitchFecha = findViewById(R.id.Switch_Fecha);

        //Spinners
        arrayList1.add("Baja");
        arrayList1.add("Media");
        arrayList1.add("Alta");
        arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList1);
        prioridad.setAdapter(arrayAdapter1);
        arrayList2.add("Solo");
        arrayList2.add("Excepto");
        arrayList2.add("Menor o igual");
        arrayList2.add("Menor");
        arrayList2.add("Mayor o igual");
        arrayList2.add("Mayor");
        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList2);
        seleccion.setAdapter(arrayAdapter2);

        //Lógica de botones
        seleccion.setEnabled(false);
        prioridad.setEnabled(false);

        //Recibir datos para modificar el activity
        RecibirDatos();

        //Restaurar activity
        DesdeFecha.setText(model.PrevDateLimit);
        HastaFecha.setText(model.PostDateLimit);

        //---------------Listener-------------//

        //Switch del título
        titulo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    CadenaUsuario.setEnabled(true);
                    descripcion.setEnabled(true);
                }
                else{
                    CadenaUsuario.setEnabled(false);
                    CadenaUsuario.setText("");
                    descripcion.setChecked(false);
                    descripcion.setEnabled(false);
                } }});

        //Switch de la prioridad
        Prioridad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    seleccion.setEnabled(true);
                    prioridad.setEnabled(true);
                }
                else{
                    seleccion.setEnabled(false);
                    prioridad.setEnabled(false);
                } }});

        //Switch de la fecha
        SwitchFecha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DesdeBtn.setEnabled(true);
                    HastaBtn.setEnabled(true);
                    //DesdeFecha.setText("");
                    //HastaFecha.setText("");
                }
                else{
                    DesdeBtn.setEnabled(false);
                    HastaBtn.setEnabled(false);
                    DesdeFecha.setHint("Fecha 1");
                    HastaFecha.setHint("Fecha 2");

                    model.PrevDateLimit = DesdeFecha.getText().toString();
                    model.PostDateLimit = HastaFecha.getText().toString();
                } }});

        //-------------Fecha y Hora--------------------
        DesdeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnDesdeActivado = true;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        HastaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnDesdeActivado = false;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    //------------------------------------------Funciones-----------------------------------------//

    public void Regresar2_A1(){
        Intent intent = new Intent(A3_2DoList.this, A1_2DoList.class);

        intent.putExtra("TextoFiltradoTitulo", CadenaUsuario.getText().toString());
        intent.putExtra("LogicaFiltradoPrioridad", seleccion.getSelectedItem().toString());
        intent.putExtra("FiltradoPrioridad", prioridad.getSelectedItemPosition());
        intent.putExtra("DesdeFecha", DesdeFecha.getText().toString());
        intent.putExtra("HastaFecha", HastaFecha.getText().toString());

        intent.putExtra("LogicaFiltradoPrioridadSeleccion", seleccion.getSelectedItemPosition());
        intent.putExtra("FiltradoPrioridadSeleccion", prioridad.getSelectedItemPosition());

        if(SwitchFecha.isChecked()){
            if(DesdeFecha.getText().toString().equals("") && HastaFecha.getText().toString().equals("")){
                SwitchFecha.setChecked(false);
            }
        }

        if(titulo.isChecked()){
            intent.putExtra("SwitchFiltradoTitulo", "1");
        }
        else{
            intent.putExtra("SwitchFiltradoTitulo", "0");
        }

        if(descripcion.isChecked()){
            intent.putExtra("SwitchFiltradoDescripcion", "1");
        }
        else{
            intent.putExtra("SwitchFiltradoDescripcion", "0");
        }

        if(Prioridad.isChecked()){
            intent.putExtra("SwitchFiltradoPrioridad", "1");
        }
        else{
            intent.putExtra("SwitchFiltradoPrioridad", "0");
        }

        if(SwitchFecha.isChecked()){
            intent.putExtra("SwitchFiltradoFecha", "1");
        }
        else{
            intent.putExtra("SwitchFiltradoFecha", "0");
        }

        setResult(A3_2DoList.RESULT_OK, intent);
        finish();
    }

    public void RecibirDatos(){
        Intent intent2 = getIntent();

        Title_Switch = intent2.getStringExtra("SwitchFiltradoTitulo");
        Notes_Switch = intent2.getStringExtra("SwitchFiltradoDescripcion");
        Priority_Switch = intent2.getStringExtra("SwitchFiltradoPrioridad");
        Date_Switch = intent2.getStringExtra("SwitchFiltradoFecha");
        FilterText = intent2.getStringExtra("TextoFiltradoTitulo");
        PrevDate = intent2.getStringExtra("DesdeFecha");
        PostDate = intent2.getStringExtra("HastaFecha");
        LogicPriority = intent2.getStringExtra("LogicaFiltradoPrioridad");
        PriorityLevel = intent2.getIntExtra("FiltradoPrioridad",0);

        LogicPriority_setSelection = intent2.getIntExtra("LogicaFiltradoPrioridadSeleccion",0);
        PriorityLevel_setSelection = intent2.getIntExtra("FiltradoPrioridadSeleccion",0);

        if (Title_Switch.equals("1")){
            titulo.setChecked(true);
            CadenaUsuario.setEnabled(true);
            descripcion.setEnabled(true);
        }
        if (Notes_Switch.equals("1")){
            descripcion.setEnabled(true);
            descripcion.setChecked(true);
        }
        if (Priority_Switch.equals("1")){
            Prioridad.setChecked(true);
            seleccion.setEnabled(true);
            prioridad.setEnabled(true);
        }
        if (Date_Switch.equals("1")){
            SwitchFecha.setChecked(true);
            DesdeBtn.setEnabled(true);
            HastaBtn.setEnabled(true);
        }

        seleccion.setSelection(LogicPriority_setSelection);
        prioridad.setSelection(PriorityLevel_setSelection);
        CadenaUsuario.setText(FilterText);
        DesdeFecha.setText(PrevDate);
        HastaFecha.setText(PostDate);

    }

    @Override
    public void onBackPressed() {
        Regresar2_A1();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        mes = Integer.toString((month+1));
        dia = Integer.toString((dayOfMonth));
        ano = Integer.toString(year);
        if(month+1<10){
            mes = "0"+Integer.toString((month+1));
        }


        if(dayOfMonth<10) {
            dia = "0"+Integer.toString(dayOfMonth);
        }


        if(BtnDesdeActivado) DesdeFecha.setText(dia+"/"+mes+"/"+ano);
        else HastaFecha.setText(dia+"/"+mes+"/"+ano);

        model.PrevDateLimit = DesdeFecha.getText().toString();
        model.PostDateLimit = HastaFecha.getText().toString();
        CadenaUsuario.setText(model.PrevDateLimit);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


    }


}
