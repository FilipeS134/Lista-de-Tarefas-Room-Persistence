package com.example.listadetarefas.helper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.listadetarefas.model.Tarefa;

@Database(entities = {Tarefa.class},version = 1, exportSchema = false)
public abstract class DbHelperRoomPersistence extends RoomDatabase {
    private static  DbHelperRoomPersistence INSTANCE;
    public abstract ITarefaDAO tarefaDAO();

}


