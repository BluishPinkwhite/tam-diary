package com.example.tamcalendar.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {E_Actor.class, E_Scale.class, E_Action.class}, version = 3)
public abstract class TamDatabase extends RoomDatabase {

    public abstract DAO_Actor daoActor();
    public abstract DAO_Scale daoScale();
    public abstract DAO_Action daoAction();
}
