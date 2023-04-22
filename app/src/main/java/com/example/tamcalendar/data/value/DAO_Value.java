package com.example.tamcalendar.data.value;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;

@Dao
public interface DAO_Value extends DAO_Base<E_Value> {

    @Query("SELECT * FROM value " +
            "ORDER BY name")
    List<E_Value> list();

    @Query("SELECT * FROM value " +
            "WHERE F_Category = :F_Category " +
            "ORDER BY name")
    List<E_Value> listByCategory(long F_Category);

    @Query("SELECT * FROM value " +
            "WHERE valueID LIKE :ID LIMIT 1")
    E_Value get(long ID);

    @Query("SELECT * FROM value " +
            "WHERE name LIKE :name LIMIT 1")
    E_Value getByName(String name);
}
