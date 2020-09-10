package com.fiuady.a2dolist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.fiuady.a2dolist.db.AppDatabase;
import com.fiuady.a2dolist.db.Tarea;
import com.fiuady.a2dolist.model.TareaListActivityModel;

import java.util.ArrayList;
import java.util.List;

public class AdapterTareas2 extends RecyclerView.Adapter<AdapterTareas.ViewHolderDatos>{

    private Context Ctx;
    Button popup;
    int status;

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        public View popup;
        TextView titulo,hora,fecha,prioridad,descripcion;
        ImageView foto;
        private TareaListActivityModel model;
        private Tarea tarea;


        public ViewHolderDatos(View view, final TareaListActivityModel model) {
            super(view);

            titulo=itemView.findViewById(R.id.Titulo);
            hora=itemView.findViewById(R.id.Hora);
            fecha=itemView.findViewById(R.id.Fecha);
            prioridad=itemView.findViewById(R.id.Prioridad);
            descripcion=itemView.findViewById(R.id.Descripcion);
            foto=itemView.findViewById(R.id.Foto);
            popup=itemView.findViewById(R.id.btnmenu);
            this.model = model;
        }

        public void bind(Tarea tarea, TareaListActivityModel model) {
            this.tarea = tarea;

            titulo.setText(tarea.getTitle());
            prioridad.setText(model.getPriorityString(tarea.getPriority()));
            fecha.setText(tarea.getDate());
            hora.setText(tarea.getHour());
            descripcion.setText(tarea.getDescription());


            if(tarea.getImportance()==false)
            {
                foto.setImageResource(R.drawable.bsoff);
            }
            if(tarea.getImportance()==true)
            {
                foto.setImageResource(R.drawable.bson);
            }

        }

    }

    private AppDatabase appDatabase;
    private TareaListActivityModel model;
    private List<Tarea> tareas;
    private List<Tarea> tareasCopia;
    private Tarea tarea;






    public AdapterTareas2(TareaListActivityModel model, Context Ctx) {
        this.model = model;
        this.Ctx=Ctx;
    }



    @NonNull
    @Override
    public AdapterTareas.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new AdapterTareas.ViewHolderDatos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false), model);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterTareas.ViewHolderDatos holder, final int position) {
        Tarea currentTarea = model.getTareas().get(position);
        status = model.getTareas().get(position).getStatus();
        holder.bind(currentTarea, model);
        holder.popup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (v!= null){
                    PopupMenu popupMenu;
                    popupMenu = new PopupMenu(Ctx, holder.popup);
                    popupMenu.inflate(R.menu.menu2);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            tareas = model.getTareas();
                            tarea = tareas.get(position);


                            switch(item.getItemId()){

                                case R.id.bor2:
                                    Toast.makeText(Ctx,"Borrado2",Toast.LENGTH_SHORT).show();
                                    model.DeleteTarea(tarea.getId());
                                    model.getTareasFinalizadas();
                                    tareas = model.getTareas();
                                    model.setTareas(tareas);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.dev:
                                    tarea.setStatus(0);
                                    model.ModifyTarea(tarea);
                                    model.getTareasFinalizadas();
                                    tareas = model.getTareas();
                                    model.setTareas(tareas);
                                    notifyDataSetChanged();
                                    return true;

                            }

                            return false;
                        }
                    });
                    popupMenu.show();
                }}

        });




    }

    @Override
    public int getItemCount() {
        return model.getTareas().size();
    }





}

