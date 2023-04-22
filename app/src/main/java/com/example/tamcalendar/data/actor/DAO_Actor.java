package com.example.tamcalendar.data.actor;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;

@Dao
public interface DAO_Actor extends DAO_Base<E_Actor> {

    @Query("SELECT * FROM actor " +
            "ORDER BY name")
    List<E_Actor> list();

    @Query("SELECT * FROM actor " +
            "WHERE name LIKE :name LIMIT 1")
    E_Actor getByName(String name);

    @Query("SELECT * FROM actor " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Actor get(int ID);
}
