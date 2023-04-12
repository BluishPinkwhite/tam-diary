package com.example.tamcalendar.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface DAO_Base<T> {
    @Insert
    void insert(T item);

    @Delete
    void delete(T item);

    @Update
    void update(T item);
}
