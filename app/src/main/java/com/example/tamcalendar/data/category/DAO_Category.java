package com.example.tamcalendar.data.category;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;

@Dao
public interface DAO_Category extends DAO_Base<E_Category> {

    @Query("SELECT * FROM categories " +
            "ORDER BY name")
    List<E_Category> list();

    @Query("SELECT * FROM categories " +
            "ORDER BY name")
    List<FullCategory> listFull();

    @Query("SELECT * FROM categories " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Category get(int ID);

    @Query("SELECT * FROM categories " +
            "WHERE ID LIKE :ID LIMIT 1")
    FullCategory getFull(int ID);

    @Query("SELECT * FROM categories " +
            "WHERE name LIKE :name LIMIT 1")
    E_Category getByName(String name);

    @Query("DELETE FROM categories " +
            "WHERE ID = :ID")
    void deleteByID(int ID);
}

