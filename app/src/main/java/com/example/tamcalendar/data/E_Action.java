package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "actions")
public class E_Action {
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
}
