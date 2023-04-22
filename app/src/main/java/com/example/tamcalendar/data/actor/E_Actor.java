package com.example.tamcalendar.data.actor;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Base;
import com.example.tamcalendar.spinner.ColorNameHaver;

@Entity(tableName = "actor")
public class E_Actor extends ColorNameHaver<E_Actor> {
    @PrimaryKey(autoGenerate = true)
    public long actorID;

    public E_Actor(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public E_Actor copy(E_Actor other) {
        name = other.name;
        color = other.color;
        return this;
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

    @Override
    public <DAO extends DAO_Base<E_Actor>> DAO getDAO() {
        return (DAO) MainActivity.database.daoActor();
    }
}
