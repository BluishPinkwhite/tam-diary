package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_Actor {

    @Query("SELECT * FROM actor")
    List<E_Actor> list();

    @Query("SELECT * FROM actor " +
            "WHERE name LIKE :name LIMIT 1")
    E_Actor getByName(String name);

    @Query("SELECT * FROM actor " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Actor get(int ID);


    @Insert
    void insert(E_Actor actor);

    @Delete
    void delete(E_Actor actor);
}
