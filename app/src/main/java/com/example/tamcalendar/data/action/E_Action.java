package com.example.tamcalendar.data.action;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Base;
import com.example.tamcalendar.data.E_Base;

@Entity(tableName = "actions")
public class E_Action implements E_Base<E_Action> {
    @PrimaryKey(autoGenerate = true)
    public long actionID;

    public String name;
    public String description;

    public int year;
    public int month;
    public int day;
    public int dateSort;

    public long F_actor;
    public long F_scale;

    public E_Action(String name, String description, int year, int month, int day, int dateSort,
                    long F_actor, long F_scale) {
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
