package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.spinner.ColorNameable;

@Entity(tableName = "actor")
public class E_Actor extends ColorNameable {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public E_Actor(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        E_Actor actor = (E_Actor) o;

        //if (ID != actor.ID) return false;
        if (!name.equals(actor.name)) return false;
        return color == actor.color;
    }
}
