package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;

@Entity(tableName = "actions")
public class E_Action implements E_Base<E_Action> {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public String name;
    public String description;

    public int year;
    public int month;
    public int day;
    public int dateSort;

    public int F_actor;
    public int F_scale;

    public E_Action(String name, String description, int year, int month, int day, int dateSort, int F_actor, int F_scale) {
        this.name = name;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateSort = dateSort;
        this.F_actor = F_actor;
        this.F_scale = F_scale;
    }

    @Override
    public <DAO extends DAO_Base<E_Action>> DAO getDAO() {
        return (DAO) MainActivity.database.daoAction();
    }

    @Override
    public E_Action copy(E_Action other) {
        this.name = other.name;
        this.description = other.description;
        this.year = other.year;
        this.month = other.month;
        this.day = other.day;
        this.dateSort = other.dateSort;
        F_actor = other.F_actor;
        F_scale = other.F_scale;
        return this;
    }
}
