package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_Value extends DAO_Base<E_Value> {

    @Query("SELECT * FROM value")
    List<E_Value> list();

    @Query("SELECT * FROM value " +
            "WHERE F_Category = :F_Category")
    List<E_Value> listByCategory(int F_Category);

    @Query("SELECT * FROM value " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Value get(int ID);

    @Query("SELECT * FROM value " +
            "WHERE name LIKE :name LIMIT 1")
    E_Value getByName(String name);
}
