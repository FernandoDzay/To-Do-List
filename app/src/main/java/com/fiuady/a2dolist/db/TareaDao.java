package com.fiuady.a2dolist.db;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteQuery;

import java.util.List;

@Dao
public interface TareaDao {

    @Insert
    public void setTarea(Tarea tarea);

    @Insert
    public void setTareas(List<Tarea> tareas);

    @Update
    public void updateTarea(Tarea tarea);

    @Update
    public void updateTasks(List<Tarea> tareas);


    @Query("SELECT MAX(id) FROM Bank")
    public int getLastId();

    @Query("UPDATE Bank SET Title = :Title, Description = :Description, Importance = :Importance, Priority = :Priority, DateCheck = :DateCheck, Hour = :Hour, Date = :Date, Status = :Status  WHERE [id] = :id")
    public void UpdateTarea(int id, String Title, String Description, boolean Importance, int Priority, boolean DateCheck, String Hour, String Date, int Status);

    @Query("DELETE FROM Bank")
    public void clearTareas();

    @Query("DELETE FROM Bank WHERE id = :id")
    public void DeleteTarea(int id);

    @Query("SELECT COUNT(*) FROM Bank")
    public int getTareasCount();

    @Query("SELECT * FROM Bank")
    public List<Tarea> getTareas();

    @Query("SELECT * FROM Bank WHERE Status = 1")
    public List<Tarea> getTareasFinalizadas();

    @Query("SELECT * FROM Bank WHERE Status = 0")
    public List<Tarea> getTareasNoFinalizadas();

    @RawQuery(observedEntities = Tarea.class)
    public List<Tarea> SuperQuery(SupportSQLiteQuery query);

}
