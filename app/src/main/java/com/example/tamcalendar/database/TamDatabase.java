package com.example.tamcalendar.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.tamcalendar.data.action.DAO_Action;
import com.example.tamcalendar.data.actor.DAO_Actor;
import com.example.tamcalendar.data.category.DAO_Category;
import com.example.tamcalendar.data.emotion.DAO_Emotion;
import com.example.tamcalendar.data.scale.DAO_Scale;
import com.example.tamcalendar.data.value.DAO_Value;
import com.example.tamcalendar.data.action.E_Action;
import com.example.tamcalendar.data.actor.E_Actor;
import com.example.tamcalendar.data.category.E_Category;
import com.example.tamcalendar.data.emotion.E_Emotion;
import com.example.tamcalendar.data.scale.E_Scale;
import com.example.tamcalendar.data.value.E_Value;

@Database(
        entities = {E_Actor.class, E_Scale.class, E_Action.class, E_Emotion.class, E_Category.class, E_Value.class},
        version = 5,
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

    public abstract DAO_Category daoCategory();

    public abstract DAO_Value daoValue();


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
    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS categories " +
                    "(`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT)");

            database.execSQL("CREATE TABLE IF NOT EXISTS value" +
                    " (`ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`F_Category` INTEGER NOT NULL, " +
                    "`color` INTEGER NOT NULL, " +
                    "`name` TEXT)");
        }
    };
}

