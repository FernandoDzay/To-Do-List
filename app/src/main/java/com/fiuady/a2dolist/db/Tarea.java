package com.fiuady.a2dolist.db;


import java.io.Serializable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "Bank")
public class Tarea implements Serializable{

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "title")
    private String Title;
    @ColumnInfo(name = "Description")
    private String Description;
    @ColumnInfo(name = "Importance")
    private boolean Importance;
    @ColumnInfo(name = "Priority")
    private int Priority;
    @ColumnInfo(name = "DateCheck")
    private boolean DateCheck;
    @ColumnInfo(name = "Hour")
    private String Hour;
    @ColumnInfo(name = "Date")
    private String Date;
    @ColumnInfo(name = "Status")
    private int Status;





    //-------------------Constructor-----------------------//
    public Tarea(int id, String Title, String Description, boolean Importance, int Priority, boolean DateCheck, String Hour, String Date, int Status){
        this.id = id;
        this.Title = Title;
        this.Description = Description;
        this.Importance = Importance;
        this.Priority = Priority;
        this.DateCheck = DateCheck;
        this.Hour = Hour;
        this.Date = Date;
    }


    //----------------------Getters-----------------------//


    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public boolean getImportance() {
        return Importance;
    }

    public int getPriority() {
        return Priority;
    }

    public boolean getDateCheck() {
        return DateCheck;
    }

    public String getHour() {
        return Hour;
    }

    public String getDate() {
        return Date;
    }

    public int getStatus() {
        return Status;
    }

    //----------------------Setters-----------------------//


    public void setStatus(int status) {
        Status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImportance(boolean importance) {
        Importance = importance;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public void setDateCheck(boolean dateCheck) {
        DateCheck = dateCheck;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getYear(String year){

        return Integer.getInteger(year.substring(6,10));
    }

    public int getMonth(String month){

        return Integer.getInteger(month.substring(6,10));
    }

    public int getDay(String day){

        return Integer.getInteger(day.substring(6,10));
    }

    public int getIntHour(String day){

        return Integer.getInteger(day.substring(0,2));
    }

    public int getIntMinute(String day){

        return Integer.getInteger(day.substring(3,5));
    }

}
