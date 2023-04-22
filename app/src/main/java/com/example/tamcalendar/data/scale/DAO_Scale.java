package com.example.tamcalendar.data.scale;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;

@Dao
public interface DAO_Scale extends DAO_Base<E_Scale> {

    @Query("SELECT * FROM scale " +
            "ORDER BY name ASC")
    List<E_Scale> list();

    @Query("SELECT * FROM scale " +
            "WHERE scaleID LIKE :ID LIMIT 1")
    E_Scale get(long ID);

    @Query("SELECT * FROM scale " +
            "WHERE name LIKE :name LIMIT 1")
    E_Scale getByName(String name);
}
