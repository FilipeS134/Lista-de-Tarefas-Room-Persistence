package com.example.listadetarefas.model;

import androidx.annotation.InspectableProperty;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tarefas")
public class Tarefa implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @ColumnInfo(name = "nome_tarefa")
    private String nomeTarefa;

    @Ignore
    public Tarefa(){

    }

    @Ignore
    public Tarefa(Long id, String nomeTarefa) {
        this.id = id;
        this.nomeTarefa = nomeTarefa;
    }

    public Tarefa( String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }
}
