package com.example.tamcalendar.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {E_Actor.class, E_Scale.class, E_Action.class, E_Emotion.class},
        version = 4,
        autoMigrations = {
        //        @AutoMigration(from = 3, to = 4)
        },
        exportSchema = true
)
public abstract class TamDatabase extends RoomDatabase {

    public abstract DAO_Actor daoActor();

    public abstract DAO_Scale daoScale();

    public abstract DAO_Action daoAction();

    public abstract DAO_Emotion daoEmotion();


    /////////////////////

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS emotions" +
                    " (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`description` TEXT, " +
                    "`dateSort` INTEGER NOT NULL, " +
                    "`hour` INTEGER NOT NULL, " +
                    "`F_scale` INTEGER NOT NULL)");
        }
    };
}

