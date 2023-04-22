package com.example.tamcalendar.data.category;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Base;
import com.example.tamcalendar.data.E_Base;

@Entity(tableName = "categories")
public class E_Category implements E_Base<E_Category> {
    @PrimaryKey(autoGenerate = true)
    public long categoryID;

    public String name;


    public E_Category(String name) {
        this.name = name;
    }

    public E_Category copy(E_Category other) {
        name = other.name;
        return this;
    }

    @Override
    public <DAO extends DAO_Base<E_Category>> DAO getDAO() {
        return (DAO) MainActivity.database.daoCategory();
    }
}
