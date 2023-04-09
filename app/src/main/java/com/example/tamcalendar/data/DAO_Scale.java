package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO_Scale {

    @Query("SELECT * FROM scale")
    List<E_Scale> list();

    @Query("SELECT * FROM scale " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Scale get(int ID);

    @Insert
    void insert(E_Scale scale);

    @Delete
    void delete(E_Scale scale);
}
