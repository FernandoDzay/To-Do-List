package com.fiuady.a2dolist;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.stetho.Stetho;
import com.fiuady.a2dolist.db.Tarea;
import com.fiuady.a2dolist.model.TareaListActivityModel;

import java.util.ArrayList;


public class A1_2DoList extends AppCompatActivity {

//-------------------------------------------------Declarar Objetos----------------------------------------------------------//

    // activity data model
    TareaListActivityModel model;

    //Interfaz android
    Button BtnAdd;
    ToggleButton starButton;
    ToggleButton ordenarButton;
    ToggleButton lupitaButton;
    Button terminadas;

    //Objetos
    Tarea tarea = new Tarea(0,"","",false,0,false,"","",0);
    RecyclerView recycler;

    //Variables
    int Priority;
    boolean Importance, DateCheck;

    String Title = "", Description = "";

    String Query = "";
    String Simbolo = "";
    String DateLimits = "";
    String OrderFilter = "";

//-------------------------------------------OnCreate---------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1_2_do_list);

        // inicializar Stetho, abrir en Google Chrome usando "chrome://inspect/"
        Stetho.initializeWithDefaults(this);
        recycler = findViewById(R.id.Recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        model = ViewModelProviders.of(this).get(TareaListActivityModel.class);
        recycler.setAdapter(new AdapterTareas(model,this));

        //Inicializar objetos
        BtnAdd = findViewById(R.id.Btn4_A1);
        starButton = findViewById(R.id.Btn2_A1);
        ordenarButton = findViewById(R.id.Btn1_A1);
        lupitaButton = findViewById(R.id.Btn3_A1);
        terminadas=findViewById(R.id.Btn5_A1);

        FuncionesAND();

        //---------------Listener-------------//

        //Botón añadir tarea
        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_A2();
            }
        });

        //Botón filtrar Activity
        lupitaButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Start_A3();
                return false;
            }
        });

        //Botón ordenar Activity
        ordenarButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Start_A4();
                return false;
            }
        });

        //---------------Toggles-------------//

        //Toggle Filtrar (Lupa)
        lupitaButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Query = "";
                    FuncionesAND();

                }
                else{
                    Query = "";
                    FuncionesAND();
                }
            }
        });

        //Toggle Importancia (Estrella)
        starButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    FuncionesAND();
                }
                else{
                    FuncionesAND();
                }


            }
        });

        //Toggle Ordenar (Ascendente o Descendente)
        ordenarButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ordenarButton.isChecked()) {
                    FuncionesAND();
                }
                else {
                    FuncionesAND();
                }
            }
        });
        //Boton de taraeas terminadas
        terminadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start_A5();
            }
        });


    }



//--------------------------------------------Start Activities-------------------------------------------------------
    public void Start_A2(){
        Intent intent = new Intent(A1_2DoList.this, A2_2DoList.class);
        startActivityForResult(intent, 1);
    }

    public void Start_A3(){
        Intent intent = new Intent(A1_2DoList.this, A3_2DoList.class);

        intent.putExtra("TextoFiltradoTitulo", model.FilterText);
        intent.putExtra("LogicaFiltradoPrioridad", model.LogicPriority);
        intent.putExtra("FiltradoPrioridad", model.PriorityLevel);
        intent.putExtra("DesdeFecha", model.PrevDate);
        intent.putExtra("HastaFecha", model.PostDate);
        intent.putExtra("LogicaFiltradoPrioridadSeleccion",model.LogicPriority_setSelection);
        intent.putExtra("FiltradoPrioridadSeleccion",model.PriorityLevel_setSelection);

        intent.putExtra("SwitchFiltradoTitulo",model.Title_Switch);
        intent.putExtra("SwitchFiltradoDescripcion", model.Notes_Switch);
        intent.putExtra("SwitchFiltradoPrioridad", model.Priority_Switch);
        intent.putExtra("SwitchFiltradoFecha", model.Date_Switch);

        startActivityForResult(intent, 3);
    }

    public void Start_A4(){
        Intent intent = new Intent(A1_2DoList.this, A4_2DoList.class);
        intent.putExtra("tipo", model.Tipo);
        intent.putExtra("orden", model.Orden);
        startActivityForResult(intent, 4);
    }

    public void Start_A5(){
        Intent intent = new Intent(A1_2DoList.this, A5_2DoList.class);
        startActivityForResult(intent, 5);
    }

//------------------Funciones--------------------------------
    public void FiltrarActivity(){

        switch (model.Estado) {
            case "1000"://Titulo
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Title like '%" + model.FilterText + "%' ";
                break;
            case "1100"://Titulo o descripcion
                Query = "SELECT * FROM Bank WHERE Status = 0 AND (Title like '%" + model.FilterText + "%' OR Description like '%" + model.FilterText + "%') ";
                break;
            case "1110"://(Titulo o descripcion) y prioridad
                Simbolo = getLogicPriority(model.LogicPriority);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND (Title like '%" + model.FilterText + "%' OR Description like '%" + model.FilterText + "%') AND Priority " + Simbolo + " " + model.PriorityLevel + " ";
                break;
            case "1111"://(Titulo o descripcion), prioridad y fecha
                Simbolo = getLogicPriority(model.LogicPriority);
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND (Title like '%" + model.FilterText + "%' OR Description like '%" + model.FilterText + "%') AND Priority " + Simbolo + " " + model.PriorityLevel + " AND Date " + DateLimits + " ";
                break;
            case "1010"://Titulo y prioridad
                Simbolo = getLogicPriority(model.LogicPriority);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Title like '%" + model.FilterText + "%' AND Priority " + Simbolo + " " + model.PriorityLevel + " ";
                break;
            case "1011"://Titulo, prioridad y fecha
                Simbolo = getLogicPriority(model.LogicPriority);
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Title like '%" + model.FilterText + "%' AND Priority " + Simbolo + " " + model.PriorityLevel + " AND Date " + DateLimits + " ";
                break;
            case "1001"://Titulo y fecha
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Title like '%" + model.FilterText + "%' AND Date " + DateLimits + " ";
                break;
            case "0011"://Prioridad y fecha
                Simbolo = getLogicPriority(model.LogicPriority);
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Priority " + Simbolo + " " + model.PriorityLevel + " AND Date " + DateLimits + " ";
                break;
            case "0010"://prioridad
                Simbolo = getLogicPriority(model.LogicPriority);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Priority " + Simbolo + " " + model.PriorityLevel + " ";
                break;
            case "0001"://fecha
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Date " + DateLimits + " ";
            case "1101"://(Titulo o descripcion) y fecha
                DateLimits = getDateLimits(model.PrevDate, model.PostDate);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND (Title like '%" + model.FilterText + "%' OR Description like '%" + model.FilterText + "%') AND Date " + DateLimits + " ";
                break;

            default:
                //ActualizarLista(bank.getArregloTareas());
                Query = "SELECT * FROM Bank WHERE Status = 0 ";
                break;
        }
    }

    public void FuncionesAND(){

        String Star;
        String Lupa;
        String Order;
        String ToggleBtns;

        if (ordenarButton.isChecked()) Order = "1";
        else Order = "0";

        if (starButton.isChecked()) Star = "1";
        else Star = "0";

        if (lupitaButton.isChecked()) Lupa = "1";
        else Lupa = "0";


        ToggleBtns = Order + Star + Lupa;
        //filterBank = new Bank(bank.getArregloTareas());
        switch (ToggleBtns){
            case "000": // Ninguno activado
                Query = "SELECT * FROM Bank WHERE Status = 0 ";
                break;
            case "001": // Lupa
                FiltrarActivity();
                break;
            case "010": // Estrella
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Importance = 1 ";
                break;
            case "011": // Lupa y estrella
                FiltrarActivity();
                Query = Query + "AND Importance = 1 ";
                break;
            case "100": // Ordenar
                OrderFilter = getOrderFilter(model.Tipo, model.Orden);
                Query = "SELECT * FROM Bank WHERE Status = 0 " + OrderFilter + " ";
                break;
            case "101": // Ordenar y lupa
                FiltrarActivity();
                OrderFilter = getOrderFilter(model.Tipo, model.Orden);
                Query = Query + " " + OrderFilter + " ";
                break;
            case "110": // Ordenar y estrella
                OrderFilter = getOrderFilter(model.Tipo, model.Orden);
                Query = "SELECT * FROM Bank WHERE Status = 0 AND Importance = 1 " + OrderFilter + " ";
                break;
            case "111": // Todos activados
                FiltrarActivity();
                OrderFilter = getOrderFilter(model.Tipo, model.Orden);
                Query = Query + " AND Importance = 1 " + OrderFilter + " ";
                break;
            default:
                break;
        }

        if(!Query.contains("ORDER BY")){
            Query = Query + "ORDER BY id DESC";
        }

        model.InsertQuery(Query);
        updateRecyclerDatos();
    }

    //Extras
    public void EstadoOrdenar( ){
        if (model.Tipo == 1 && model.Orden == 2) {
            Toast Aviso=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Prioridad-Descendente",Toast.LENGTH_SHORT);
            Aviso.show();
        }
        else if (model.Tipo == 1 && model.Orden == 3) {
            Toast Aviso=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Prioridad-Ascendente",Toast.LENGTH_SHORT);
            Aviso.show();
        }
        else if (model.Tipo == 2 && model.Orden == 2) {
            Toast Aviso=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Fecha-Descendente",Toast.LENGTH_SHORT);
            Aviso.show();
        } else if (model.Tipo == 2 && model.Orden == 3) {
            Toast Aviso=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Fecha-Ascendente",Toast.LENGTH_SHORT);
            Aviso.setGravity(Gravity.CENTER,0,30);
            Aviso.show();

        } else if (model.Tipo == 0 && model.Orden == 0) {
            Toast Aviso= Toast.makeText(getApplicationContext(),"¡Filtro Inactivo   Mantenca pulsado para configurar",Toast.LENGTH_SHORT);
            Aviso.show();
        }
    }

    public void EstadoFiltro( ){


        if (model.Estado!=null) {
            switch (model.Estado) {

                case "0000"://Titulo
                    Toast Aviso0=Toast.makeText(getApplicationContext(),"¡Filtro Inactivo!   Mantenga presionado para configurar",Toast.LENGTH_SHORT);
                    Aviso0.show();
                    break;
                case "1000"://Titulo
                    Toast Aviso=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo",Toast.LENGTH_SHORT);
                    Aviso.show();
                    break;

                case "1100"://Titulo o descripcion
                    Toast Aviso1=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Descripción",Toast.LENGTH_SHORT);
                    Aviso1.show();
                    break;
                case "1110"://(Titulo o descripcion) y prioridad
                    Toast Aviso2=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Descripción & Prioridad",Toast.LENGTH_SHORT);
                    Aviso2.show();
                    break;
                case "1111"://(Titulo o descripcion), prioridad y fecha
                    Toast Aviso3=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Descripción & Prioridad & Fecha",Toast.LENGTH_SHORT);
                    Aviso3.show();
                    break;
                case "1010"://Titulo y prioridad
                    Toast Aviso4=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Prioridad",Toast.LENGTH_SHORT);
                    Aviso4.show();
                    break;
                case "1011"://Titulo, prioridad y fecha
                    Toast Aviso5=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Prioridad & Fecha",Toast.LENGTH_SHORT);
                    Aviso5.show();
                    break;
                case "1101"://Titulo, prioridad y fecha
                    Toast Aviso56=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Descripción & Fecha",Toast.LENGTH_SHORT);
                    Aviso56.show();
                    break;
                case "1001"://Titulo y fecha
                    Toast Aviso6=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Titulo & Fecha",Toast.LENGTH_SHORT);
                    Aviso6.show();
                    break;
                case "0011"://Prioridad y fecha
                    Toast Aviso7=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Prioridad & Fecha",Toast.LENGTH_SHORT);
                    Aviso7.show();
                    break;
                case "0010"://prioridad
                    Toast Aviso8=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Prioridad",Toast.LENGTH_SHORT);
                    Aviso8.show();
                    break;
                case "0001"://fecha
                    Toast Aviso9=Toast.makeText(getApplicationContext(),"¡Filtro Activo!    Filtrado: Fecha",Toast.LENGTH_SHORT);
                    Aviso9.show();
                    break;
            }
        }


    }

    void updateRecyclerDatos() {

        model = ViewModelProviders.of(this).get(TareaListActivityModel.class);

        AdapterTareas adapter = new AdapterTareas(model,this);

        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public String getOrderFilter(int Tipo, int Orden){
        String string = "";
        if(Tipo == 1){
            string = "ORDER BY Priority ";
        }
        if(Tipo == 2){
            string = "ORDER BY Date ";
        }
        if(Orden == 2){
            string = string + "DESC";
        }
        if(Orden == 3){
            string = string + "";
        }
        return string;
    }

    public String getLogicPriority(String LogicPriority){
        String Logica = "";
        switch (LogicPriority){
            case "Solo":
                Logica = "=";
                break;
            case "Excepto":
                Logica = "!=";
                break;
            case "Menor o igual":
                Logica = "<=";
                break;
            case "Menor":
                Logica = "<";
                break;
            case "Mayor o igual":
                Logica = ">=";
                break;
            case "Mayor":
                Logica = ">";
                break;
            default:
                break;
        }
        return Logica;
    }

    public String getDateLimits(String desdeString, String hastaString){
        String LogicaFecha = "";
        //Tiene sólo desde

        if(!desdeString.equals("") && hastaString.equals("")){
            desdeString=desdeString.substring(0,4)+desdeString.substring(4,6)+desdeString.substring(6,8);

            LogicaFecha = ">= " + desdeString;
        }

        //Tiene sólo hasta
        if(desdeString.equals("") && !hastaString.equals("")){
            hastaString=hastaString.substring(0,4)+hastaString.substring(4,6)+hastaString.substring(6,8);
            LogicaFecha = "<= " + hastaString;
        }

        //Tiene desde y hasta
        if(!desdeString.equals("") && !hastaString.equals("")){
            desdeString=desdeString.substring(0,4)+desdeString.substring(4,6)+desdeString.substring(6,8);
            hastaString=hastaString.substring(0,4)+hastaString.substring(4,6)+hastaString.substring(6,8);

            LogicaFecha = ">= " + desdeString + " AND Date <= " + hastaString;
        }
        return LogicaFecha;
    }


//------------------Overrides--------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Agregar Tarea
        if (requestCode == 1 && resultCode == A1_2DoList.RESULT_OK) {

            Title = data.getStringExtra("Titulo");
            Description = data.getStringExtra("Descripcion");
            Importance = data.getBooleanExtra("Importancia",false);
            Priority = data.getIntExtra("Prioridad",0);
            DateCheck = data.getBooleanExtra("DateCheck",false);
            model.Hour = data.getStringExtra("Hora");
            model.Date = data.getStringExtra("Fecha");

            tarea = new Tarea(model.getNextId() + 1, Title, Description, Importance, Priority, DateCheck, model.Hour, model.Date, 0);
            model.setTarea(tarea);
            FuncionesAND();

        }

        //Modificar Tarea
        if (requestCode == 2 && resultCode == A1_2DoList.RESULT_OK) {
            model.id = data.getIntExtra("id",0);
            Title = data.getStringExtra("Titulo");
            Description = data.getStringExtra("Descripcion");
            Importance = data.getBooleanExtra("Importancia",false);
            Priority = data.getIntExtra("Prioridad",0);
            DateCheck = data.getBooleanExtra("DateCheck",false);
            model.Hour = data.getStringExtra("Hora");
            model.Date = data.getStringExtra("Fecha");
            model.LogicPriority_setSelection = data.getIntExtra("LogicaFiltradoPrioridadSeleccion",0);
            model.PriorityLevel_setSelection = data.getIntExtra("FiltradoPrioridadSeleccion",0);

            tarea = new Tarea(model.id,Title, Description, Importance, Priority, DateCheck, model.Hour, model.Date, 0);
            model.ModifyTarea(tarea);
            FuncionesAND();
        }

        //Filtro de tareas
        if (requestCode == 3 && resultCode == A1_2DoList.RESULT_OK) {

            model.Title_Switch = data.getStringExtra("SwitchFiltradoTitulo");
            model.Notes_Switch = data.getStringExtra("SwitchFiltradoDescripcion");
            model.Priority_Switch = data.getStringExtra("SwitchFiltradoPrioridad");
            model.Date_Switch = data.getStringExtra("SwitchFiltradoFecha");
            model.FilterText = data.getStringExtra("TextoFiltradoTitulo");
            model.PrevDate = data.getStringExtra("DesdeFecha");
            model.PostDate= data.getStringExtra("HastaFecha");
            model.LogicPriority = data.getStringExtra("LogicaFiltradoPrioridad");
            model.PriorityLevel = data.getIntExtra("FiltradoPrioridad",0);


            model.LogicPriority_setSelection = data.getIntExtra("LogicaFiltradoPrioridadSeleccion",0);
            model.PriorityLevel_setSelection = data.getIntExtra("FiltradoPrioridadSeleccion",0);

            model.Estado = model.Title_Switch + model.Notes_Switch + model.Priority_Switch + model.Date_Switch;
            if (model.PrevDate.length() == 10) model.PrevDate = model.PrevDate.substring(6,10) + model.PrevDate.substring(3,5) + model.PrevDate.substring(0,2);
            if (model.PostDate.length() == 10) model.PostDate = model.PostDate.substring(6,10) + model.PostDate.substring(3,5) + model.PostDate.substring(0,2);

            lupitaButton.setChecked(true);
            //EstadoFiltro();
            FuncionesAND();
        }

        //Ordenar tareas
        if (requestCode == 4 && resultCode == A1_2DoList.RESULT_OK) {

            model.Orden = data.getIntExtra("orden", 2);
            model.Tipo = data.getIntExtra("tipo",2);

            ordenarButton.setChecked(true);
            //EstadoOrdenar();
            FuncionesAND();
        }

        //Regresar tarea
        if (requestCode == 5 && resultCode == A1_2DoList.RESULT_OK) {
            FuncionesAND();
        }
    }

}




