package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.spinner.ColorNameHaver;

@Entity(tableName = "scale")
public class E_Scale extends ColorNameHaver<E_Scale> {
    @PrimaryKey(autoGenerate = true)
    public int ID;


    public E_Scale(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public E_Scale copy(E_Scale other) {
        name = other.name;
        color = other.color;
        return this;
    }

    @Override
    public <DAO extends DAO_Base<E_Scale>> DAO getDAO() {
        return (DAO) MainActivity.database.daoScale();
    }
}
