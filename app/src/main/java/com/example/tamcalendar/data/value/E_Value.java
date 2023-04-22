package com.example.tamcalendar.data.value;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Base;
import com.example.tamcalendar.spinner.ColorNameHaver;

@Entity(tableName = "value")
public class E_Value extends ColorNameHaver<E_Value> {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public int F_Category;


    public E_Value(String name, int color, int F_Category) {
        this.name = name;
        this.color = color;
        this.F_Category = F_Category;
    }

    public E_Value copy(E_Value other) {
        name = other.name;
        color = other.color;
        F_Category = other.F_Category;
        return this;
    }

    @Override
    public <DAO extends DAO_Base<E_Value>> DAO getDAO() {
        return (DAO) MainActivity.database.daoValue();
    }
}
