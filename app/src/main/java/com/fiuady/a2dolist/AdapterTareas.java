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

public class AdapterTareas extends RecyclerView.Adapter<AdapterTareas.ViewHolderDatos>
{

    private Context Ctx;
    Button popup;

    public static class ViewHolderDatos extends RecyclerView.ViewHolder {

        public View popup;
        TextView titulo,hora,fecha,prioridad,descripcion;
        ImageView foto;
        private TareaListActivityModel model;
        private Tarea tarea;

        private String Date;


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

            if(!tarea.getDate().equals("0")) {
                Date = tarea.getDate().substring(6, 8) + "/" + tarea.getDate().substring(4, 6) + "/" + tarea.getDate().substring(0, 4);
            }
            else{
                Date = "";
            }
            fecha.setText(Date);
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


    private TareaListActivityModel model;
    private List<Tarea> tareas;
    private Tarea tarea;






    public AdapterTareas(TareaListActivityModel model, Context Ctx) {
        this.model = model;
        this.Ctx=Ctx;
    }



    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolderDatos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false), model);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterTareas.ViewHolderDatos holder, final int position) {
        Tarea currentTarea = model.getTareas().get(position);
        final int status = model.getTareas().get(position).getStatus();
        holder.bind(currentTarea, model);
        holder.popup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (v!= null){
                    PopupMenu popupMenu;
                    popupMenu = new PopupMenu(Ctx, holder.popup);

                    if (status==0)
                    {
                        popupMenu.inflate(R.menu.menu1);
                    }

                    if (status==1)
                    {
                        popupMenu.inflate(R.menu.menu2);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            tareas = model.getTareas();
                            tarea = tareas.get(position);


                            switch(item.getItemId()){

                                case R.id.bor:
                                    Toast.makeText(Ctx,"Borrado1",Toast.LENGTH_SHORT).show();
                                    model.DeleteTarea(tarea.getId());
                                    tareas = model.getFilterTareas();
                                    model.setTareas(tareas);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.bor2:
                                    Toast.makeText(Ctx,"Borrado2",Toast.LENGTH_SHORT).show();
                                    intent = new Intent(Ctx, A5_2DoList.class);
                                    Ctx.startActivity(intent);

                                    return true;
                                case R.id.edit:

                                    intent = new Intent(Ctx, A2_2DoList.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Tarea",tarea);
                                    intent.putExtras(bundle);
                                    intent.putExtra("Modify", 1);
                                    ((Activity) Ctx).startActivityForResult(intent,2);
                                    return true;


                                case R.id.concl:
                                    tarea.setStatus(1);
                                    tareas.set(position,tarea);
                                    model.ModifyTarea(tarea);
                                    tareas.remove(position);
                                    model.setTareas(tareas);
                                    notifyDataSetChanged();
                                    return true;
                                case R.id.dev:
                                    Toast.makeText(Ctx,"devolver",Toast.LENGTH_SHORT).show();
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
