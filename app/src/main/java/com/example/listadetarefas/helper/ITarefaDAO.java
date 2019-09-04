package com.example.listadetarefas.helper;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.listadetarefas.model.Tarefa;

import java.util.List;

@Dao
public interface ITarefaDAO {

    @Insert
    public void salvar(Tarefa tarefa);
    @Update
    public void atualizar(Tarefa tarefa);
    @Delete
    public void deletar(Tarefa tarefa);
    @Query("SELECT * FROM tarefas")
    public List<Tarefa> lisTarefas();
}
