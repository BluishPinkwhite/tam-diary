package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_Category extends DAO_Base<E_Category> {

    @Query("SELECT * FROM categories")
    List<E_Category> list();

    @Query("SELECT * FROM categories " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Category get(int ID);

    @Query("SELECT * FROM categories " +
            "WHERE name LIKE :name LIMIT 1")
    E_Category getByName(String name);
}
