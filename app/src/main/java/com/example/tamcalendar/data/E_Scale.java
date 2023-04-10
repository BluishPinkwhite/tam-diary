package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scale")
public class E_Scale {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public int value;
    public int color;
}
