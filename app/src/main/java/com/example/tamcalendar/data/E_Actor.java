package com.example.tamcalendar.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "actor")
public class E_Actor {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    public String name;
    public int color;

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
