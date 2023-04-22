package com.example.tamcalendar.data.category;

import androidx.room.Dao;
import androidx.room.MapInfo;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;
import java.util.Map;

@Dao
public interface DAO_Category extends DAO_Base<E_Category> {

    @Query("SELECT * FROM categories " +
            "ORDER BY name")
    List<E_Category> list();

    @Transaction
    @Query("SELECT * FROM categories " +
            "ORDER BY name")
    List<FullCategory> listFull();

    @MapInfo(keyColumn = "categoryID")
    @Query("SELECT * FROM categories ")
    Map<Long, E_Category> listMapByID();

    @Query("SELECT * FROM categories " +
            "WHERE categoryID LIKE :ID LIMIT 1")
    E_Category get(long ID);

    @Transaction
    @Query("SELECT * FROM categories " +
            "WHERE categoryID LIKE :ID LIMIT 1")
    FullCategory getFull(long ID);

    @Query("SELECT * FROM categories " +
            "WHERE name LIKE :name LIMIT 1")
    E_Category getByName(String name);

    @Query("DELETE FROM categories " +
            "WHERE categoryID = :ID")
    void deleteByID(long ID);
}

