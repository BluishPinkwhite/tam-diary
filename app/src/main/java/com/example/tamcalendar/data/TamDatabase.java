package com.example.tamcalendar.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {E_Actor.class, E_Scale.class, E_Action.class},
        version = 3,
        autoMigrations = {
                //@AutoMigration(from = 3, to = 4)
        },
        exportSchema = true
)
public abstract class TamDatabase extends RoomDatabase {

    public abstract DAO_Actor daoActor();

    public abstract DAO_Scale daoScale();

    public abstract DAO_Action daoAction();
}
