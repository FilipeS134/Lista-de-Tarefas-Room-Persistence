package com.example.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.listadetarefas.R;
import com.example.listadetarefas.adpter.TarefaAdpter;
import com.example.listadetarefas.helper.DbHelper;
import com.example.listadetarefas.helper.DbHelperRoomPersistence;
import com.example.listadetarefas.helper.RecyclerItemClickListener;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdpter tarefaAdpter;
    private List<Tarefa> tarefaList = new ArrayList<>();
    private Tarefa tarefaSelecionada;
    public DbHelperRoomPersistence db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        db = Room.databaseBuilder(getApplicationContext(), DbHelperRoomPersistence.class, "MeuDB").allowMainThreadQueries().build();



        // add evento d eclique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final Tarefa tarefaSelecionada = tarefaList.get(position);

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                View mMiew = getLayoutInflater().inflate(R.layout.activity_add_tarefa, null);
                                final EditText nomeTarefa = mMiew.findViewById(R.id.textTarefa);
                                nomeTarefa.setText(tarefaSelecionada.getNomeTarefa());
                                dialog.setTitle("Atualizar Tarefa");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (nomeTarefa != null) {

                                            Tarefa tarefa = new Tarefa();
                                            tarefa.setNomeTarefa(nomeTarefa.getText().toString());
                                            tarefa.setId(tarefaSelecionada.getId());
                                            db.tarefaDAO().atualizar(tarefa);
                                            tarefaList.clear();
                                            carregarTarefas();
                                            Toast.makeText(getApplicationContext(), "Sucesso ao alterar tarefa!", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(getApplicationContext(), "Erro ao excluir tarefa!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.setNegativeButton("Não", null);
                                dialog.setView(mMiew);
                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                tarefaSelecionada = tarefaList.get(position);

                                AlertDialog.Builder diaBuilder =  new AlertDialog.Builder(MainActivity.this);

                                //configurar titulo e mensagem
                                diaBuilder.setTitle("Confirmar exclusão");
                                diaBuilder.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + "?" );

                                diaBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                if (tarefaSelecionada != null) {
                                                    db.tarefaDAO().deletar(tarefaSelecionada);
                                                    tarefaList.clear();
                                                    carregarTarefas();
                                                    Toast.makeText(getApplicationContext(), "Sucesso ao excluir tarefa!", Toast.LENGTH_SHORT).show();

                                                }else {
                                                    Toast.makeText(getApplicationContext(), "Erro ao excluir tarefa!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                });

                                diaBuilder.setNegativeButton("Não", null);

                                diaBuilder.create();
                                diaBuilder.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                View mMiew = getLayoutInflater().inflate(R.layout.activity_add_tarefa, null);
                final EditText nomeTarefa = mMiew.findViewById(R.id.textTarefa);
                dialog.setTitle("Salvar Tarefa");

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (nomeTarefa != null) {
                            Tarefa tarefa = new Tarefa(nomeTarefa.getText().toString());
                            db.tarefaDAO().salvar(tarefa);
                            tarefaList.clear();
                            carregarTarefas();
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar tarefa!", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(), "Erro ao excluir tarefa!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.setNegativeButton("Não", null);
                dialog.setView(mMiew);
                dialog.create();
                dialog.show();


               // Intent intent = new Intent(getApplicationContext(), AddTarefaActivity.class);
               // startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        carregarTarefas();
        super.onStart();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void carregarTarefas(){
        tarefaList.addAll(db.tarefaDAO().lisTarefas());

        tarefaAdpter = new TarefaAdpter(tarefaList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdpter);
    }

    @Override
    protected void onStop() {
        tarefaList.clear();
        super.onStop();
    }
}
