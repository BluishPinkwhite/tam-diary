package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_Scale extends DAO_Base<E_Scale> {

    @Query("SELECT * FROM scale")
    List<E_Scale> list();

    @Query("SELECT * FROM scale " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Scale get(int ID);

    @Query("SELECT * FROM scale " +
            "WHERE name LIKE :name LIMIT 1")
    E_Scale getByName(String name);
}
