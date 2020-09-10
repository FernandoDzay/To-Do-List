package com.fiuady.a2dolist.model;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.database.sqlite.SQLiteQuery;
import android.support.annotation.NonNull;

//import com.fiuady.todolist.db.AppDatabase;
//import com.fiuady.toolist.db.Task;

import com.fiuady.a2dolist.db.AppDatabase;
import com.fiuady.a2dolist.db.Tarea;

import java.util.ArrayList;
import java.util.List;


public class TareaListActivityModel extends AndroidViewModel {

    private AppDatabase appDatabase;
    private List<Tarea> tareas;

    //------------------------------------------- Variables ------------------------------------------------------//

    //A1
    public int Orden, Tipo;
    public String Title_Switch, Notes_Switch, Priority_Switch, Date_Switch;
    public String FilterText, PrevDate, PostDate, LogicPriority, Estado;
    public int PriorityLevel, LogicPriority_setSelection, PriorityLevel_setSelection;
    public int ItemListPosition, PosicionRealDeTarea;

    //A2
    public String Date;
    public String Hour;
    public int id;

    //A3
    public String PrevDateLimit;
    public String PostDateLimit;

    //------------------------------------------------------------------------------------------------------------//








    public TareaListActivityModel(Application application) {
        super(application);
        //A1
        Title_Switch = "0";
        Notes_Switch = "0";
        Priority_Switch = "0";
        Date_Switch = "0";
        Estado = "0000";

        //A2
        Date = "";
        Hour = "";

        //A3
        PrevDateLimit = "Fecha 1";
        PostDateLimit = "Fecha 2";

        // obtener referencia a la base de datos
        appDatabase = AppDatabase.getDatabase(this.getApplication());

        // ¿es el inicio de la aplicación?
        if (tareas == null) {
            // ¿hay datos almacenados en la base de datos?
            if (appDatabase.tareaModel().getTareasCount() > 0) {
                // recuperar información de la base de datos
                tareas = appDatabase.tareaModel().getTareas();
            } else {
                // en caso contrario se inicia con datos de prueba (TEMPORAL)
                tareas = new ArrayList<>();
                tareas.add(new Tarea(1,"Dar asesorías de matemáticas","Preparar el material para los alumnos",true,0,true,"10:30","20190505",1));
                tareas.add(new Tarea(2,"Cita con mi novia","Recordar llevarle flores y chocolates",false,2,true,"16:40","20190818",0));
                tareas.add(new Tarea(3,"Dar clases de química","Llevarle a los chicos ejercicios de práctica y material",false,2,true,"10:45","20191106",1));
                tareas.add(new Tarea(4,"Cumpleaños de Juan","Comprar su regalo y materiales para la fiesta",false,1,true,"20:30","20180520",0));
                tareas.add(new Tarea(5,"Tarea de visión","Realizar el filtrado de las imagenes del libro",false,1,true,"15:30","20181004",0));
                tareas.add(new Tarea(6, "Tarea de visión","Realizar el filtrado de las imagenes del libro",false,1,true,"15:30","20181004",0));
                tareas.add(new Tarea(7,"Concierto de metálica","Comprar boletos para toda la familia de mi novia y los chicos",false,2,true,"02:30","20180125",0));
                tareas.add(new Tarea(8,"Cita con el dentista","Me pidio que le lleve la radiografía lateral de craneo",true,1,true,"15:15","20191016",0));
                tareas.add(new Tarea(9,"Darle de comer al perro","Quererlo mucho, abrazarlo y llevarle croquetas",true,2,true,"20:31","20200520",0));
                tareas.add(new Tarea(10,"Proyecto de Android","Hacer un To Do List",true,2,true,"16:00","20181002",0));
                tareas.add(new Tarea(11,"Cumpleaños de la prima","Comprar refresco y botanas para toda la noche",true,0,true,"20:35","20181015",0));
                tareas.add(new Tarea(12,"Inicio de Cursos - Exani 2","Contratar a las personas necesarias para impartir el curoso, relizar la preparación de los materiales.",false,0,true,"02:30","20180521",0));
                // inserta datos de prueba en base de datos
                appDatabase.tareaModel().setTareas(tareas);
            }
        }
    }

    // obtener todas las tareas - sin filtro
    public List<Tarea> getTareas() {
       //tareas = appDatabase.tareaModel().getTareas();
        return tareas; // Debe realizarse copia
        //return appDatabase.tareaModel().getTareas();
    }

    public List<Tarea> getFilterTareas(){
        return tareas;
    }

    public void setTarea(Tarea tarea){
        appDatabase.tareaModel().setTarea(tarea);
        tareas.add(tarea);
    }

    public void setTareas(List<Tarea> tareas){
        this.tareas = tareas;
    }

    public String getPriorityString(int priority) {
        // este texto debería provenir de la base de datos
        switch (priority) {
            case 0:
                return "Low";

            case 1:
                return "Medium";

            case 2:
                return "High";

            default:
                throw new IllegalArgumentException("priority");
        }
    }


    public void InsertQuery(String Query){
        StringBuilder queryString = new StringBuilder();
        queryString.append(Query);
        tareas = appDatabase.tareaModel().SuperQuery(new SimpleSQLiteQuery(queryString.toString()));
    }

    public int getNextId(){
        return appDatabase.tareaModel().getLastId();
    }

    public void ModifyTarea(Tarea tarea){
        int id = tarea.getId();
        String Title = tarea.getTitle();
        String Description = tarea.getDescription();
        boolean Importance = tarea.getImportance();
        int Priority = tarea.getPriority();
        boolean DateCheck = tarea.getDateCheck();
        String Hour = tarea.getHour();
        String Date = tarea.getDate();
        int Status = tarea.getStatus();

        appDatabase.tareaModel().UpdateTarea(id, Title, Description, Importance, Priority, DateCheck, Hour, Date, Status);
        tareas = appDatabase.tareaModel().getTareas();
    }

    public void DeleteTarea(int id){
        appDatabase.tareaModel().DeleteTarea(id);
        tareas = appDatabase.tareaModel().getTareas();
    }

    public void getTareasFinalizadas(){
        tareas = appDatabase.tareaModel().getTareasFinalizadas();
        //return appDatabase.tareaModel().getTareasFinalizadas();
    }

    public void getTareasNoFinalizadas2(){
        tareas = appDatabase.tareaModel().getTareasFinalizadas();
    }

    public List<Tarea> getTareasNoFinalizadas(List<Tarea> tareas){
        List<Tarea> tareasNoFinalizadas = this.tareas;
        tareasNoFinalizadas.clear();
        for(int i=1; i<tareas.size(); i++){
            if(tareas.get(i).getStatus()==0){
                tareasNoFinalizadas.add(tareas.get(i));
            }
        }

        return tareasNoFinalizadas;
    }

}
