package com.fiuady.a2dolist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fiuady.a2dolist.model.TareaListActivityModel;

public class A5_2DoList extends AppCompatActivity {

    TareaListActivityModel model;
    RecyclerView recycler;

    String Query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a5_2_do_list);

        // referencia RecyclerView
        recycler = findViewById(R.id.Recycler2);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        model = ViewModelProviders.of(this).get(TareaListActivityModel.class);

        model.getTareasFinalizadas();
        recycler.setAdapter(new AdapterTareas2(model,this));

    }

    public void Regresar_A1(){
        Intent intent = new Intent(A5_2DoList.this, A1_2DoList.class);
        setResult(A3_2DoList.RESULT_OK, intent);
        finish();

    }



    @Override
    public void onBackPressed() {
        Regresar_A1();
    }
}
